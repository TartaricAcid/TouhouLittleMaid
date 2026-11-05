package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * 游戏侧的日程与随机自发行为。
 * 日程（WabPlan）和自发设置（WabAutonomy）都写进女仆的持久化数据，跟着存档走；
 * 但只有 WAB 会话在的时候才执行——断开或换成上游模组，女仆就回到原本的样子。
 */
public final class WabAutonomy {
    private final WabBridge bridge;
    private final Random random = new Random();
    private final Map<UUID, Map<String, Long>> lastStepRun = new HashMap<>();
    private final Map<UUID, Long> lastIdleAt = new HashMap<>();
    private final Map<UUID, String> autonomyRaw = new HashMap<>();
    private final Map<UUID, JsonObject> autonomyConf = new HashMap<>();
    private final Map<UUID, Deque<Long>> idleMoments = new HashMap<>();
    private int tickCounter;

    public WabAutonomy(WabBridge bridge) {
        this.bridge = bridge;
    }

    public void notePlanChanged(EntityMaid maid) {
        UUID id = maid.getUUID();
        lastStepRun.remove(id);
        autonomyRaw.remove(id);
        autonomyConf.remove(id);
        idleMoments.remove(id);
    }

    public void tick() {
        if (!WabConfig.autonomyEnabled()) return;
        // 没有 WAB 会话时女仆保持上游原样：日程和自发行动都等连上了再跑，
        // 免得换了模组、没开 WAB 的玩家发现女仆自己在动。
        if (!bridge.connected()) return;
        tickCounter++;
        if (tickCounter % 40 != 0) return;
        for (EntityMaid maid : bridge.ownedMaids()) {
            if (bridge.dispatcher().hasJob(maid) || !maid.level().isLoaded(maid.blockPosition())) continue;
            runPlan(maid);
            maybeIdle(maid);
        }
    }

    private void runPlan(EntityMaid maid) {
        String raw = maid.getPersistentData().getString("WabPlan");
        if (raw.isEmpty()) return;
        JsonArray steps;
        try {
            JsonElement parsed = JsonParser.parseString(raw);
            if (parsed.isJsonArray()) {
                steps = parsed.getAsJsonArray();
            } else if (parsed.isJsonObject()) {
                JsonObject obj = parsed.getAsJsonObject();
                steps = obj.has("steps") ? obj.getAsJsonArray("steps") : new JsonArray();
            } else {
                return;
            }
        } catch (Exception e) {
            return;
        }
        if (steps.isEmpty()) return;
        String now = WabSensors.clock(maid.level().getDayTime());
        long day = maid.level().getDayTime() / 24000L;
        Map<String, Long> stepDays = lastStepRun.computeIfAbsent(maid.getUUID(), k -> new HashMap<>());

        // 同一时段可能排了好几步：按 weight × random 加权抽一条，而不是每步各自掷骰子、
        // 一小时内把「挖矿」「喂饭」「抱一下」全做一遍。
        List<Integer> due = new ArrayList<>();
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < steps.size(); i++) {
            if (!steps.get(i).isJsonObject()) continue;
            JsonObject step = steps.get(i).getAsJsonObject();
            if (!WabJson.str(step, "at").equals(now)) continue;
            Long lastDay = stepDays.get(String.valueOf(i));
            if (lastDay != null && lastDay == day) continue;
            double probability = Math.max(0, Math.min(1, WabJson.number(step, "random", 1.0)));
            double weight = Math.max(0, WabJson.number(step, "weight", 1.0));
            if (probability <= 0 || weight <= 0) continue;
            due.add(i);
            weights.add(probability * weight);
        }
        if (due.isEmpty()) return;
        double total = 0;
        for (double weight : weights) total += weight;
        double pick = random.nextDouble() * total;
        int chosen = due.size() - 1;
        for (int i = 0; i < due.size(); i++) {
            pick -= weights.get(i);
            if (pick <= 0) {
                chosen = i;
                break;
            }
        }
        int index = due.get(chosen);
        stepDays.put(String.valueOf(index), day);
        executeStep(maid, steps.get(index).getAsJsonObject());
    }

    private void executeStep(EntityMaid maid, JsonObject step) {
        String doWhat = WabJson.str(step, "do").toLowerCase(Locale.ROOT);
        if (!bridge.allowWorldActions() && WabGate.stepNeedsWorldActions(doWhat)) {
            TouhouLittleMaid.LOGGER.debug("[WAB] 世界操作没有开放，跳过日程里的 {}", doWhat);
            return;
        }
        JsonObject params = new JsonObject();
        params.addProperty("maid", maid.getUUID().toString());
        switch (doWhat) {
            case "wander", "home", "follow" -> {
                params.addProperty("mode", doWhat);
                WabActions.go(bridge, maid, params);
            }
            case "mine", "chop", "harvest", "scoop" -> {
                params.addProperty("action", doWhat);
                params.addProperty("filter", WabJson.str(step, "filter"));
                params.addProperty("count", String.valueOf(WabJson.integer(step, "count", 16)));
                // 协议里的 hours 是这一步打算持续多久（游戏内小时）；换算成 tick 当预算，缺省 1 小时。
                int budget = Math.max(200, Math.min(24000, WabJson.integer(step, "hours", 1) * 1000));
                WabActions.Job job = WabActions.startGather(bridge, maid, params, budget, true, null);
                bridge.dispatcher().noteJob(job);
            }
            case "collect" -> {
                params.addProperty("filter", WabJson.str(step, "filter"));
                params.addProperty("count", String.valueOf(WabJson.integer(step, "count", 16)));
                WabActions.collect(bridge, maid, params);
            }
            case "gesture", "say", "sit", "stand", "sleep", "look" -> {
                params.addProperty("action", doWhat);
                params.addProperty("arg", WabJson.str(step, "arg"));
                params.addProperty("text", WabJson.str(step, "text"));
                params.addProperty("target", WabJson.str(step, "target"));
                params.addProperty("duration", String.valueOf(WabJson.number(step, "duration", 2.0)));
                WabActions.act(bridge, maid, params);
            }
            case "feed_player" -> {
                params.addProperty("action", "feed_player");
                params.addProperty("item", WabJson.str(step, "item"));
                WabActions.hold(bridge, maid, params);
            }
            default -> {
            }
        }
    }

    private void maybeIdle(EntityMaid maid) {
        if (bridge.dispatcher().hasJob(maid)) return;
        JsonObject conf = autonomyOf(maid);
        if (!WabJson.bool(conf, "enabled", true)) return;
        long idleMillis = (long) (Math.max(1, WabJson.number(conf, "idleMinutes", 5)) * 60_000L);
        long now = System.currentTimeMillis();
        Long last = lastIdleAt.get(maid.getUUID());
        if (last != null && now - last < idleMillis) return;
        if (!withinHourlyCap(maid, conf)) return;
        if (!random.nextBoolean()) return;
        lastIdleAt.put(maid.getUUID(), now);
        idleMoments.computeIfAbsent(maid.getUUID(), k -> new ArrayDeque<>()).addLast(now);
        String action = switch (random.nextInt(4)) {
            case 0 -> "wander";
            case 1 -> "gesture";
            case 2 -> "look";
            default -> "home";
        };
        JsonObject params = new JsonObject();
        params.addProperty("maid", maid.getUUID().toString());
        // go 的 mode 缺省会落到 "to"，而 "to" 解析不到目标时会把跟随挂到主人身上，
        // 之后每 20 tick 重新寻路，把在家模式压掉——所以这里必须把意图显式带上。
        params.addProperty("mode", action);
        switch (action) {
            case "wander" -> {
                params.addProperty("radius", Math.max(1, WabJson.number(conf, "wanderRadius", 8.0)));
                WabActions.go(bridge, maid, params);
            }
            case "gesture" -> {
                params.addProperty("action", "gesture");
                params.addProperty("arg", random.nextBoolean() ? "wave" : "hug");
                WabActions.act(bridge, maid, params);
            }
            case "look" -> {
                params.addProperty("action", "look");
                WabActions.act(bridge, maid, params);
            }
            default -> WabActions.go(bridge, maid, params);
        }
    }

    /**
     * maid_plan 的 autonomy 段以前只写进存档、没人读，所以 idleMinutes /
     * maxActionsPerHour / wanderRadius 三项设了也不生效；这里按原样接回来，缺省值维持旧行为。
     */
    private JsonObject autonomyOf(EntityMaid maid) {
        String raw = maid.getPersistentData().getString("WabAutonomy");
        UUID id = maid.getUUID();
        JsonObject cached = autonomyConf.get(id);
        if (cached != null && raw.equals(autonomyRaw.get(id))) return cached;
        JsonObject conf = new JsonObject();
        if (!raw.isEmpty()) {
            try {
                JsonElement parsed = JsonParser.parseString(raw);
                if (parsed.isJsonObject()) conf = parsed.getAsJsonObject();
            } catch (Exception ignored) {
            }
        }
        autonomyRaw.put(id, raw);
        autonomyConf.put(id, conf);
        return conf;
    }

    /** 滚动一小时窗口内的自发次数；maxActionsPerHour 不填或填 0 表示不设上限。 */
    private boolean withinHourlyCap(EntityMaid maid, JsonObject conf) {
        int max = WabJson.integer(conf, "maxActionsPerHour", 0);
        if (max <= 0) return true;
        Deque<Long> window = idleMoments.computeIfAbsent(maid.getUUID(), k -> new ArrayDeque<>());
        long now = System.currentTimeMillis();
        while (!window.isEmpty() && now - window.peekFirst() > 3_600_000L) {
            window.pollFirst();
        }
        return window.size() < max;
    }

    public void flush() {
        lastStepRun.clear();
        lastIdleAt.clear();
        autonomyRaw.clear();
        autonomyConf.clear();
        idleMoments.clear();
    }
}
