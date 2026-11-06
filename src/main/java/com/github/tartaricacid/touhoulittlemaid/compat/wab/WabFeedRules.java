package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 投喂口味规则：挑食表 / 喜爱表 / 吃腻计数，由 WAB 下发（feed_rules 帧）。
 * 表只在桥连着的时候有效——断桥 clear()，之后判定一律返回 NORMAL，
 * 「不连 WAB 行为与原版一致」这条不变量靠这一点守住。
 * 计数在游戏侧本地累加（下一次投喂立刻就能判），宿主仍是权威，会在收到 feed 事件后重推一份。
 */
public final class WabFeedRules {
    public enum Verdict { FAVORITE, NORMAL, PICKY, BORED }

    private static final List<String> PICKY = new ArrayList<>();
    private static final List<String> FAVORITE = new ArrayList<>();
    private static final Map<String, Integer> COUNTS = new HashMap<>();
    private static volatile int boredomLimit = 5;

    private WabFeedRules() {
    }

    /**
     * feed_rules 帧：{"v":1,"type":"feed_rules","picky":[...],"favorite":[...],"boredomLimit":5,"counts":{"id":n}}
     * 表项支持两种写法：物品注册名（minecraft:rotten_flesh）或物品标签（#forge:candy）。
     */
    public static void apply(JsonObject frame) {
        List<String> picky = new ArrayList<>();
        List<String> favorite = new ArrayList<>();
        for (var e : WabJson.arr(frame, "picky")) {
            if (e.isJsonPrimitive()) picky.add(e.getAsString());
        }
        for (var e : WabJson.arr(frame, "favorite")) {
            if (e.isJsonPrimitive()) favorite.add(e.getAsString());
        }
        synchronized (WabFeedRules.class) {
            PICKY.clear();
            PICKY.addAll(picky);
            FAVORITE.clear();
            FAVORITE.addAll(favorite);
            COUNTS.clear();
            for (String key : WabJson.obj(frame, "counts").keySet()) {
                COUNTS.put(key, WabJson.integer(WabJson.obj(frame, "counts"), key, 0));
            }
            boredomLimit = WabJson.integer(frame, "boredomLimit", boredomLimit);
        }
        TouhouLittleMaid.LOGGER.debug("[WAB] feed rules updated: picky={}, favorite={}, counts={}, limit={}",
                PICKY.size(), FAVORITE.size(), COUNTS.size(), boredomLimit);
    }

    public static void clear() {
        synchronized (WabFeedRules.class) {
            PICKY.clear();
            FAVORITE.clear();
            COUNTS.clear();
        }
    }

    public static Verdict classify(ItemStack food) {
        String id = BuiltInRegistries.ITEM.getKey(food.getItem()).toString();
        synchronized (WabFeedRules.class) {
            if (matches(food, FAVORITE)) return Verdict.FAVORITE;
            if (matches(food, PICKY)) return Verdict.PICKY;
            if (COUNTS.getOrDefault(id, 0) >= boredomLimit) return Verdict.BORED;
            return Verdict.NORMAL;
        }
    }

    /** 接受投喂后本地记一笔，让下一口立刻判得出腻不腻；宿主随后会重推权威计数。 */
    public static int note(ItemStack food) {
        String id = BuiltInRegistries.ITEM.getKey(food.getItem()).toString();
        synchronized (WabFeedRules.class) {
            return COUNTS.merge(id, 1, Integer::sum);
        }
    }

    public static int count(ItemStack food) {
        String id = BuiltInRegistries.ITEM.getKey(food.getItem()).toString();
        synchronized (WabFeedRules.class) {
            return COUNTS.getOrDefault(id, 0);
        }
    }

    public static boolean hasRules() {
        synchronized (WabFeedRules.class) {
            return !PICKY.isEmpty() || !FAVORITE.isEmpty() || !COUNTS.isEmpty();
        }
    }

    public static int pickyCount() {
        synchronized (WabFeedRules.class) {
            return PICKY.size();
        }
    }

    public static int favoriteCount() {
        synchronized (WabFeedRules.class) {
            return FAVORITE.size();
        }
    }

    public static int boredomLimit() {
        return boredomLimit;
    }

    public static int trackedCount() {
        synchronized (WabFeedRules.class) {
            return COUNTS.size();
        }
    }

    private static boolean matches(ItemStack food, List<String> entries) {
        for (String entry : entries) {
            if (entry == null || entry.isEmpty()) continue;
            if (entry.startsWith("#")) {
                ResourceLocation rl = ResourceLocation.tryParse(entry.substring(1));
                if (rl == null) continue;
                if (food.is(TagKey.create(Registries.ITEM, rl))) return true;
                continue;
            }
            if (entry.equals(BuiltInRegistries.ITEM.getKey(food.getItem()).toString())) return true;
        }
        return false;
    }
}
