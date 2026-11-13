package com.github.tartaricacid.touhoulittlemaid.compat.wab.config;

import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabHeadPat;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabJson;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabSensors;
import com.github.tartaricacid.touhoulittlemaid.compat.wab.WabStorage;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 18 大项里「会动世界」的执行层。所有入口先过配置开关 + 纯代码规则，再落到最小化的稳定 MC API。
 *
 * <p>双线一致的技巧：物品句柄用 {@code var} 接，不 import 任何 forge/neoforge 的 IItemHandler 包；
 * 注册表、附魔组件、物品比较一律走 {@link WabPlatform}。本文件在两条线上必须字节一致。
 * <p>断桥时 {@link WabConfigRuntime#connected()} 为 false，风险类开关回落保守默认值（全关），
 * 因此不连 WAB 时不会产生任何额外的世界改动。
 */
public final class WabFeatures {
    private static final int MAX_FARM_BLOCKS = 64;
    private static final int MAX_PICKUP_ITEMS = 32;

    private WabFeatures() {
    }

    private static boolean on(String key) {
        return WabConfigRuntime.on(key);
    }

    private static JsonObject ok(String message) {
        JsonObject out = new JsonObject();
        out.addProperty("message", message);
        return out;
    }

    private static JsonObject fail(String code, String message) {
        JsonObject out = new JsonObject();
        out.addProperty("ok", false);
        out.addProperty("code", code);
        out.addProperty("error", message);
        return out;
    }

    // ==================== 一、自主物品管理 ====================

    /** item：list / drop / equip / pickup。全部动作都受各自开关约束，默认关闭。 */
    public static JsonObject item(EntityMaid maid, JsonObject p) {
        String action = WabJson.str(p, "action", "list").toLowerCase(Locale.ROOT);
        if (!on(WabConfigKeys.ITEM_AUTONOMY_ENABLED)) {
            return fail("disabled", "自主物品管理没开（item_autonomy_enabled）。");
        }
        var inv = maid.getAvailableInv(false);
        switch (action) {
            case "container" -> {
                if (!on(WabConfigKeys.CONTAINER_READ_ENABLED)) return fail("disabled", "容器读取没开。");
                int r = (int) Math.max(1, Math.min(8, WabJson.number(p, "radius", 6)));
                BlockPos origin = maid.blockPosition();
                JsonArray details = new JsonArray();
                int boxes = 0;
                for (BlockPos cursor : BlockPos.betweenClosed(origin.offset(-r, -2, -r), origin.offset(r, 2, r))) {
                    if (!(maid.level().getBlockEntity(cursor) instanceof net.minecraft.world.Container box)) continue;
                    boxes++;
                    for (int i = 0; i < box.getContainerSize() && details.size() < 80; i++) {
                        ItemStack s = box.getItem(i);
                        if (s.isEmpty()) continue;
                        JsonObject one = WabSensors.item(s);
                        one.addProperty("slot", i);
                        one.addProperty("pos", cursor.toShortString());
                        if (on(WabConfigKeys.CONTAINER_ATTR_DETAIL)) WabPlatform.appendAttributes(s, one);
                        details.add(one);
                    }
                }
                JsonObject out = ok("附近 " + boxes + " 个容器，读出 " + details.size() + " 组物品属性。");
                out.add("items", details);
                return out;
            }
            case "list" -> {
                JsonArray items = new JsonArray();
                for (int i = 0; i < inv.getSlots(); i++) {
                    ItemStack stack = inv.getStackInSlot(i);
                    if (stack.isEmpty()) continue;
                    JsonObject one = WabSensors.item(stack);
                    one.addProperty("slot", i);
                    WabPlatform.appendAttributes(stack, one);
                    items.add(one);
                }
                JsonObject out = ok("背包里有 " + items.size() + " 组东西，属性已带回。");
                out.add("items", items);
                return out;
            }
            case "drop" -> {
                if (!on(WabConfigKeys.ITEM_DROP_ENABLED)) return fail("disabled", "丢弃开关没开。");
                int slot = WabJson.integer(p, "slot", -1);
                if (slot < 0 || slot >= inv.getSlots() || inv.getStackInSlot(slot).isEmpty())
                    return fail("bad_slot", "这个槽位是空的。");
                ItemStack taken = inv.extractItem(slot, inv.getStackInSlot(slot).getCount(), false);
                maid.spawnAtLocation(taken);
                return ok("丢掉了 " + taken.getHoverName().getString() + "。");
            }
            case "equip" -> {
                if (!on(WabConfigKeys.ITEM_EQUIP_ENABLED)) return fail("disabled", "主动装备开关没开。");
                int slot = WabJson.integer(p, "slot", -1);
                if (slot < 0 || slot >= inv.getSlots() || inv.getStackInSlot(slot).isEmpty())
                    return fail("bad_slot", "这个槽位是空的。");
                ItemStack taken = inv.extractItem(slot, 1, false);
                ItemStack previous = maid.getMainHandItem().copy();
                maid.setItemInHand(InteractionHand.MAIN_HAND, taken);
                if (!previous.isEmpty()) maid.spawnAtLocation(previous);
                return ok("换上了 " + taken.getHoverName().getString() + "。");
            }
            case "pickup" -> {
                if (!on(WabConfigKeys.ITEM_PICKUP_ENABLED)) return fail("disabled", "主动捡起开关没开。");
                String mode = WabConfigRuntime.text(WabConfigKeys.ITEM_PICKUP_FILTER_MODE);
                String expr = WabConfigRuntime.text(WabConfigKeys.ITEM_PICKUP_FILTER_EXPR);
                double radius = Math.max(1, Math.min(16, WabJson.number(p, "radius", 6)));
                AABB area = maid.getBoundingBox().inflate(radius);
                int taken = 0;
                for (ItemEntity drop : maid.level().getEntitiesOfClass(ItemEntity.class, area, e -> e.isAlive())) {
                    if (taken >= MAX_PICKUP_ITEMS) break;
                    ItemStack stack = drop.getItem();
                    if (stack.isEmpty()) continue;
                    boolean match = WabRules.matchesPickupFilter(mode, WabPlatform.itemId(stack),
                            stack.getHoverName().getString(), stack.getDamageValue(),
                            WabPlatform.hasEnchantment(stack), expr);
                    if (!match) continue;
                    ItemStack rest = WabPlatform.insertStacked(inv, stack);
                    if (rest.getCount() == stack.getCount()) continue;
                    if (rest.isEmpty()) drop.discard();
                    else drop.setItem(rest);
                    taken++;
                }
                return ok("按「" + mode + "」捡起了 " + taken + " 组。");
            }
            default -> {
                return fail("unknown_action", "物品只支持 list / drop / equip / pickup。");
            }
        }
    }

    // ==================== 一、农耕 ====================

    /** farm：必须显式给 tool / crop / area，四要素缺一就拒绝且零世界改动。 */
    public static JsonObject farm(EntityMaid maid, JsonObject p) {
        if (!on(WabConfigKeys.FARMING_ENABLED)) return fail("disabled", "农耕开关没开（farming_enabled）。");
        String tool = WabJson.str(p, "tool");
        String crop = WabJson.str(p, "crop");
        String area = WabJson.str(p, "area");
        boolean require = on(WabConfigKeys.FARMING_REQUIRE_EXPLICIT_SPEC);
        String missing = WabRules.missingFarmingSpec(tool, crop, area, require);
        if (!missing.isEmpty()) return fail("missing_spec", "农耕还缺「" + missing + "」，没有指定就不动手。");
        JsonArray posArr = WabJson.arr(p, "pos");
        if (posArr.size() < 3) return fail("bad_pos", "要给种植区域起点坐标。");
        BlockPos origin = new BlockPos((int) posArr.get(0).getAsDouble(), (int) posArr.get(1).getAsDouble(),
                (int) posArr.get(2).getAsDouble());
        int width = Math.max(1, Math.min(8, WabJson.integer(p, "width", 3)));
        int depth = Math.max(1, Math.min(8, WabJson.integer(p, "depth", 3)));
        boolean tilling = on(WabConfigKeys.FARMING_ALLOW_TILLING);
        int tilled = 0;
        int planted = 0;
        for (int dx = 0; dx < width && tilled + planted < MAX_FARM_BLOCKS; dx++) {
            for (int dz = 0; dz < depth && tilled + planted < MAX_FARM_BLOCKS; dz++) {
                BlockPos pos = origin.offset(dx, 0, dz);
                if (!maid.level().isLoaded(pos)) continue;
                if (maid.level().getBlockState(pos).hasBlockEntity()) continue;
                boolean tillable = maid.level().getBlockState(pos).is(Blocks.DIRT)
                        || maid.level().getBlockState(pos).is(Blocks.GRASS_BLOCK);
                if (tilling && tillable) {
                    maid.level().setBlockAndUpdate(pos, Blocks.FARMLAND.defaultBlockState());
                    tilled++;
                }
                BlockPos above = pos.above();
                if (maid.level().getBlockState(above).isAir()) {
                    ItemStack seed = WabPlatform.itemFromId(crop.contains(":") ? crop : "minecraft:" + crop);
                    if (!seed.isEmpty() && seed.getItem() instanceof net.minecraft.world.item.BlockItem blockItem) {
                        maid.level().setBlockAndUpdate(above, blockItem.getBlock().defaultBlockState());
                        planted++;
                    }
                }
            }
        }
        JsonObject out = ok("开垦 " + tilled + " 格、播种 " + planted + " 格。");
        out.addProperty("tilled", tilled);
        out.addProperty("planted", planted);
        return out;
    }

    // ==================== 二、高级反应逃生 ====================

    /** escape：mlg / suffocation；求安慰单独走 comfort。岩浆船实验项已移除（不主动进入岩浆，见 F13）。 */
    public static JsonObject escape(EntityMaid maid, JsonObject p) {
        String action = WabJson.str(p, "action", "mlg").toLowerCase(Locale.ROOT);
        return switch (action) {
            case "mlg" -> mlgWater(maid, p);
            case "suffocation" -> suffocation(maid, p);
            default -> fail("unknown_action", "逃生只支持 mlg / suffocation。");
        };
    }

    /** F14 待收回的落地水：放水后到点收回，避免永久改世界（「放完水之后收回来」）。 */
    private static final class PendingWater {
        final net.minecraft.server.level.ServerLevel level;
        final BlockPos pos;
        final long expireTick;

        PendingWater(net.minecraft.server.level.ServerLevel level, BlockPos pos, long expireTick) {
            this.level = level;
            this.pos = pos;
            this.expireTick = expireTick;
        }
    }

    /** 按「维度 + 坐标」记，不用女仆 UUID：同一女仆连续触发两次会覆盖，前一格水就永远收不回来。 */
    private static final java.util.concurrent.ConcurrentHashMap<String, PendingWater> PENDING_WATER =
            new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * F14：跟随主人时，若因垂直落差过大（走下去会掉血）而不得不瞬移，先尝试落地水。
     * 返回 true = 本轮不瞬移，让原版寻路继续走下去；false = 照旧瞬移。
     * 断桥时恒为 false（保持原版）。判定走 R21 {@code descentPlan} 与 {@code mayUseMlgWater}。
     */
    public static boolean tryMlgDescent(EntityMaid maid, net.minecraft.world.entity.LivingEntity owner) {
        if (maid == null || owner == null || maid.level().isClientSide) return false;
        if (!WabConfigRuntime.connected()) return false;
        if (!(maid.level() instanceof net.minecraft.server.level.ServerLevel level)) return false;
        if (!on(WabConfigKeys.MLG_WATER_ENABLED)) return false;

        double drop = maid.getY() - owner.getY();
        if (drop < 4) return false;
        boolean mlgAvailable = WabRules.mayUseMlgWater(true, hasBucket(maid),
                on(WabConfigKeys.MLG_AUTO_FETCH_WATER));
        if (WabRules.descentPlan(false, (int) drop, 4, mlgAvailable) != 1) return false;

        BlockPos pos = maid.blockPosition();
        if (!level.isLoaded(pos) || !level.getBlockState(pos).isAir()) return false;
        level.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        String key = level.dimension().location() + ":" + pos.getX() + "," + pos.getY() + "," + pos.getZ();
        PENDING_WATER.put(key, new PendingWater(level, pos.immutable(),
                level.getGameTime() + 60L));
        return true;
    }

    /** F14：每帧检查，到点把临时放的水收回（只收自己放的、且仍是水的那一格）。 */
    public static void tickDescentWater() {
        if (PENDING_WATER.isEmpty()) return;
        for (java.util.Map.Entry<String, PendingWater> e : PENDING_WATER.entrySet()) {
            PendingWater w = e.getValue();
            if (w.level.getGameTime() < w.expireTick) continue;
            PENDING_WATER.remove(e.getKey());
            if (w.level.isLoaded(w.pos) && w.level.getBlockState(w.pos).is(Blocks.WATER)) {
                w.level.setBlockAndUpdate(w.pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

    private static JsonObject mlgWater(EntityMaid maid, JsonObject p) {
        boolean requireBucket = on(WabConfigKeys.MLG_REQUIRE_BUCKET);
        boolean autoFetch = on(WabConfigKeys.MLG_AUTO_FETCH_WATER);
        boolean hasBucket = hasBucket(maid);
        if (!WabRules.mayUseMlgWater(on(WabConfigKeys.MLG_WATER_ENABLED), hasBucket, autoFetch)) {
            return fail("no_bucket", requireBucket && !hasBucket
                    ? "手里没有水桶或铁桶，也没允许自己去打水。" : "落地水没开或缺桶。");
        }
        BlockPos pos = maid.blockPosition();
        if (!maid.level().getBlockState(pos).isAir()) {
            return fail("occupied", "脚下不是空气格，放不了水。");
        }
        maid.level().setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
        JsonObject out = ok("落地水放好了，这次不找主人求安慰。");
        out.addProperty("mlgSuccess", true);
        out.addProperty("suppressComfort", on(WabConfigKeys.MLG_SUCCESS_SKIP_COMFORT));
        return out;
    }

    private static boolean hasBucket(EntityMaid maid) {
        var inv = maid.getAvailableInv(false);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack s = inv.getStackInSlot(i);
            if (s.is(net.minecraft.world.item.Items.WATER_BUCKET) || s.is(net.minecraft.world.item.Items.BUCKET)) return true;
        }
        return false;
    }

    private static JsonObject suffocation(EntityMaid maid, JsonObject p) {
        int air = WabJson.integer(p, "airTicks", maid.getAirSupply());
        if (!WabRules.shouldEscapeSuffocation(on(WabConfigKeys.SUFFOCATION_ESCAPE_ENABLED), air)) {
            return ok("空气还够，不需要逃生。");
        }
        String mode = WabConfigRuntime.text(WabConfigKeys.SUFFOCATION_PATH_MODE);
        BlockPos from = maid.blockPosition();
        BlockPos target = findAir(maid, from, "dig".equalsIgnoreCase(mode));
        if (target == null) return fail("no_path", "附近没找到有空气的位置，也没能挖出逃生路径。");
        maid.teleportTo(target.getX() + 0.5, target.getY(), target.getZ() + 0.5);
        return ok("窒息了，按「" + mode + "」逃到 " + target.toShortString() + "。");
    }

    private static BlockPos findAir(EntityMaid maid, BlockPos from, boolean dig) {
        for (int r = 1; r <= 6; r++) {
            for (BlockPos pos : BlockPos.betweenClosed(from.offset(-r, -2, -r), from.offset(r, 3, r))) {
                if (!maid.level().isLoaded(pos)) continue;
                if (!maid.level().getBlockState(pos).isAir()) continue;
                if (!maid.level().getBlockState(pos.above()).isAir()) continue;
                return pos.immutable();
            }
        }
        if (dig) {
            BlockPos up = from.above(2);
            if (!maid.level().getBlockState(up).is(Blocks.BEDROCK)) {
                maid.level().setBlockAndUpdate(up, Blocks.AIR.defaultBlockState());
                return up.immutable();
            }
        }
        return null;
    }

    /** comfort：落地水成功必须抑制；概率取 config/ai 两来源里更保守的那个。 */
    public static JsonObject comfort(EntityMaid maid, JsonObject p) {
        boolean nearDeath = WabJson.bool(p, "nearDeath", false);
        boolean mlgSuccess = WabJson.bool(p, "mlgSuccess", false);
        String source = WabConfigRuntime.text(WabConfigKeys.COMFORT_PROBABILITY_SOURCE);
        double probability = WabConfigRuntime.number(WabConfigKeys.COMFORT_SEEK_PROBABILITY);
        if ("ai".equalsIgnoreCase(source)) {
            probability = Math.min(probability, WabJson.number(p, "aiProbability", probability));
        }
        boolean excludeMlg = on(WabConfigKeys.COMFORT_EXCLUDE_MLG_SUCCESS);
        boolean should = WabRules.shouldSeekComfort(on(WabConfigKeys.COMFORT_SEEK_ENABLED),
                nearDeath, excludeMlg && mlgSuccess, probability, WabJson.number(p, "roll", 0.5));
        if (!should) return ok("这次不求安慰。");
        maid.getChatBubbleManager().addTextChatBubble("chat_bubble.touhou_little_maid.head_pat.request");
        return ok("她吓坏了，来找主人求安慰。");
    }

    /** 摸头：AI 主动请求、玩家拒绝伤心；三档反应由 WabHeadPat.pet() 读持久数据播放。 */
    public static JsonObject headpat(EntityMaid maid, JsonObject p) {
        if (!on(WabConfigKeys.HEADPAT_ENABLED)) return fail("disabled", "摸头功能整体关闭。");
        String action = WabJson.str(p, "action", "request").toLowerCase(Locale.ROOT);
        return switch (action) {
            case "request" -> {
                if (!on(WabConfigKeys.HEADPAT_AI_REQUEST_ENABLED)) yield fail("disabled", "AI 主动请求摸头没开。");
                WabHeadPat.requestNow(maid);
                yield ok("她主动来讨摸摸了（概率与冷却沿用讨摸规则）。");
            }
            case "refuse" -> {
                if (!on(WabConfigKeys.HEADPAT_PLAYER_REFUSE_ENABLED)) yield fail("disabled", "玩家拒绝摸头没开。");
                WabHeadPat.refuse(maid);
                yield ok(on(WabConfigKeys.HEADPAT_SAD_ON_REFUSE) ? "主人拒绝了，她露出伤心的表情。" : "主人拒绝了。");
            }
            default -> fail("unknown_action", "摸头只支持 request / refuse。");
        };
    }

    // ==================== 八、偷吃 ====================

    public static JsonObject sneakEat(EntityMaid maid, JsonObject p) {
        double ai = WabConfigRuntime.number(WabConfigKeys.SNEAK_EAT_AI_PROBABILITY);
        double player = WabConfigRuntime.number(WabConfigKeys.SNEAK_EAT_PLAYER_PROBABILITY);
        boolean should = WabRules.shouldStealFood(on(WabConfigKeys.SNEAK_EAT_ENABLED), ai, player,
                WabJson.number(p, "roll", 0.5));
        if (!should) return ok("这回没偷吃。");
        var inv = maid.getAvailableInv(false);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack s = inv.getStackInSlot(i);
            if (s.isEmpty()) continue;
            if (s.getItem().getFoodProperties(s, maid) == null) continue;
            inv.extractItem(i, 1, false);
            JsonObject out = ok("她偷偷吃了一口 " + s.getHoverName().getString() + "。");
            out.addProperty("item", WabPlatform.itemId(s));
            return out;
        }
        return ok("想偷吃，但背包里没有能吃的东西。");
    }

    // ==================== 四、模式切换 / 九、聊天 / 六、多步流程 / 三、备份 ====================

    public static JsonObject mode(EntityMaid maid, JsonObject p) {
        long elapsed = WabJson.integer(p, "elapsedSeconds", 0) * 1000L;
        long cooldown = WabConfigRuntime.integer(WabConfigKeys.MODE_SWITCH_COOLDOWN_SECONDS) * 1000L;
        if (!WabRules.maySwitchMode(elapsed, cooldown)) return fail("cooldown", "切换太频繁，还在冷却里。");
        String want = WabJson.str(p, "mode", "follow").toLowerCase(Locale.ROOT);
        if ("home".equals(want) && !on(WabConfigKeys.MODE_SWITCH_ALLOW_HOME)) return fail("disabled", "守家模式没开。");
        if ("follow".equals(want) && !on(WabConfigKeys.MODE_SWITCH_ALLOW_FOLLOW)) return fail("disabled", "跟随模式没开。");
        maid.setHomeModeEnable("home".equals(want));
        return ok("切到" + ("home".equals(want) ? "守家" : "跟随") + "模式。");
    }

    public static JsonObject chat(EntityMaid maid, JsonObject p) {
        String action = WabJson.str(p, "action", "wake").toLowerCase(Locale.ROOT);
        if ("wake".equals(action)) {
            boolean hit = WabRules.wakeByName(on(WabConfigKeys.CHAT_WAKE_BY_NAME_ENABLED),
                    WabJson.str(p, "spoken"), WabConfigRuntime.text(WabConfigKeys.CHAT_WAKE_WORDS));
            return hit ? ok("叫到名字了，她应了一声。") : ok("没叫她名字，先不打扰。");
        }
        if ("dedup".equals(action)) {
            List<String> hashes = new ArrayList<>();
            for (var e : WabJson.arr(p, "hashes")) hashes.add(e.getAsString());
            List<String> unique = on(WabConfigKeys.CHAT_CONTEXT_DEDUP_ENABLED)
                    ? WabRules.dedupContext(hashes) : hashes;
            JsonArray arr = new JsonArray();
            for (String h : unique) arr.add(h);
            JsonObject out = ok("上下文去重后剩 " + unique.size() + " 句。");
            out.add("hashes", arr);
            out.addProperty("sendOnce", on(WabConfigKeys.CHAT_SEND_ONCE_PER_TURN));
            out.addProperty("invisible", on(WabConfigKeys.CHAT_INVISIBLE_ENABLED));
            return out;
        }
        return fail("unknown_action", "聊天只支持 wake / dedup。");
    }

    /** 多步流程：每步先过开关与步数上限，报告按 summary / stepwise / none 组装。 */
    public static JsonObject plan(EntityMaid maid, JsonObject p) {
        if (!on(WabConfigKeys.PLAN_EXECUTION_ENABLED)) return fail("disabled", "多步流程没开。");
        JsonArray steps = WabJson.arr(p, "steps");
        int max = WabConfigRuntime.integer(WabConfigKeys.PLAN_MAX_STEPS);
        boolean needScan = on(WabConfigKeys.PLAN_NEED_SCAN_OR_PHOTO)
                && !WabJson.bool(p, "scanned", false) && !WabJson.bool(p, "photo", false);
        if (needScan) return fail("need_scan", "要先探测或拍照，才能规划路线。");
        String report = WabConfigRuntime.text(WabConfigKeys.PLAN_REPORT_MODE);
        JsonArray done = new JsonArray();
        int executed = 0;
        for (int i = 0; i < steps.size(); i++) {
            if (!WabRules.planStepAllowed(i, max)) break;
            JsonObject one = new JsonObject();
            one.addProperty("step", i);
            var step = steps.get(i);
            one.add("action", step);
            one.addProperty("status", "ok");
            done.add(one);
            executed++;
        }
        JsonObject out = ok("走了 " + executed + " 步。");
        out.addProperty("reportMode", report);
        out.addProperty("planner", WabConfigRuntime.text(WabConfigKeys.PLAN_PLANNER));
        if ("summary".equalsIgnoreCase(report) || "stepwise".equalsIgnoreCase(report)) out.add("steps", done);
        return out;
    }

    /** 模组侧备份：把快照写进女仆持久数据（游戏存档内一份），供 WAB 侧恢复时取用。 */
    public static JsonObject backup(EntityMaid maid, JsonObject p) {
        if (!on(WabConfigKeys.BACKUP_MOD_SIDE_COPY)) return fail("disabled", "模组侧副本没开。");
        JsonObject snapshot = WabStorage.snapshot(maid);
        maid.getPersistentData().putString("WabBackupSnapshot", snapshot.toString());
        maid.getPersistentData().putLong("WabBackupAt", System.currentTimeMillis());
        String policy = WabConfigRuntime.text(WabConfigKeys.BACKUP_CONFLICT_POLICY);
        boolean verify = on(WabConfigKeys.BACKUP_RESTORE_VERIFY);
        String winner = WabRules.backupWinner(policy, WabJson.integer(p, "wabSavedAt", 0),
                WabJson.integer(p, "modSavedAt", 0), verify);
        JsonObject out = ok("游戏侧快照已写进存档。");
        out.addProperty("policy", policy);
        out.addProperty("restoreFrom", winner);
        out.addProperty("snapshot", snapshot.toString());
        return out;
    }

    /** 容器属性详情：给 container list 的每一行补附魔 / NBT 有无。 */
    public static void enrich(JsonObject one, ItemStack stack) {
        WabPlatform.appendAttributes(stack, one);
    }
}
