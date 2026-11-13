package com.github.tartaricacid.touhoulittlemaid.compat.wab.config;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * 纯代码规则引擎（PRD §8.1 的 R01–R17 + 逃生/兼容补充规则）。
 *
 * <p>这些判断默认全部走代码，不调模型：关闭所有「可选 AI」后功能仍然可用且零模型请求。
 * 硬约束：本类不得出现 WabBridge / EntityMaid / 网络 / {@code currentTimeMillis()} /
 * {@code new Random()} / 任何模型调用。时间与随机一律由调用方以参数注入，方便边界单测。
 */
public final class WabRules {
    private WabRules() {
    }

    /** R01 深度思考自动关闭：热度低于阈值就别开深度思考，省 Token。 */
    public static boolean autoDisableDeepThink(double heat, double threshold) {
        return heat < threshold;
    }

    /** R02 思考模式：返回 0=off / 1=fast / 2=deep。auto 由热度决定，模型不支持时永远不返回 deep。 */
    public static int resolveThinkMode(String mode, boolean modelSupportsDeep, double heat, double threshold) {
        String m = mode == null ? "off" : mode.toLowerCase(Locale.ROOT);
        return switch (m) {
            case "fast" -> 1;
            case "deep" -> modelSupportsDeep ? 2 : 1;
            case "auto" -> modelSupportsDeep && !autoDisableDeepThink(heat, threshold) ? 2 : 1;
            default -> 0;
        };
    }

    /** R03 求安慰：落地水成功不触发；仅濒死掷骰，概率由配置给。 */
    public static boolean shouldSeekComfort(boolean enabled, boolean nearDeath, boolean mlgSuccess,
                                            double probability, double roll) {
        if (!enabled || !nearDeath || mlgSuccess) return false;
        return roll < clamp01(probability);
    }

    /** R04 可选 AI 只有「开了且可用」才走模型，否则回落纯代码。 */
    public static boolean shouldUseAi(boolean aiEnabled, boolean aiAvailable) {
        return aiEnabled && aiAvailable;
    }

    /** R05 自主行动间隔（毫秒）：fixed 用固定值，random_range 用注入的 roll 在 [min,max] 内插值。 */
    public static long autonomyIntervalMs(String mode, double fixedMinutes, double minMinutes,
                                          double maxMinutes, double roll) {
        double minutes;
        if ("random_range".equalsIgnoreCase(mode == null ? "" : mode)) {
            double lo = Math.max(0.1, Math.min(minMinutes, maxMinutes));
            double hi = Math.max(lo, maxMinutes);
            minutes = lo + (hi - lo) * clamp01(roll);
        } else {
            minutes = Math.max(0.1, fixedMinutes);
        }
        return Math.round(minutes * 60_000.0);
    }

    /** R06 每小时自发行为上限；maxPerHour <= 0 表示不设上限。 */
    public static boolean withinHourlyCap(int actionsInLastHour, int maxPerHour) {
        return maxPerHour <= 0 || actionsInLastHour < maxPerHour;
    }

    /**
     * R07 捡起筛选：按 item_id / name / attribute / durability / enchant 五种口径，
     * 外加 {@code damage<=N} 与 {@code enchant:xxx} 两种表达式。表达式为空表示不筛选。
     */
    public static boolean matchesPickupFilter(String mode, String itemId, String name,
                                              int damage, boolean enchanted, String expr) {
        String m = (mode == null ? "item_id" : mode).toLowerCase(Locale.ROOT);
        boolean base = switch (m) {
            case "name" -> name != null && !name.isBlank();
            case "attribute" -> damage >= 0 || enchanted;
            case "durability" -> damage >= 0;
            case "enchant" -> enchanted;
            default -> itemId != null && !itemId.isBlank();
        };
        if (!base) return false;
        if (expr == null || expr.isBlank()) return true;
        String e = expr.trim().toLowerCase(Locale.ROOT);
        if (e.startsWith("damage<=")) {
            try {
                return damage <= Integer.parseInt(e.substring(8).trim());
            } catch (NumberFormatException ex) {
                return false;
            }
        }
        if (e.startsWith("enchant:")) return enchanted;
        return e.equals(itemId) || e.equals(name == null ? "" : name.toLowerCase(Locale.ROOT));
    }

    /** R08 农耕四要素强校验：返回第一个缺失项的中文名，全部齐备返回空串。未指定时不自动执行。 */
    public static String missingFarmingSpec(String tool, String crop, String area, boolean requireExplicit) {
        if (!requireExplicit) return "";
        if (tool == null || tool.isBlank()) return "工具";
        if (crop == null || crop.isBlank()) return "种植物";
        if (area == null || area.isBlank()) return "种植区域";
        return "";
    }

    /** R09 输棋哭。 */
    public static boolean shouldCryOnLose(boolean enabled, double probability, double roll) {
        return enabled && roll < clamp01(probability);
    }

    /** R10 偷吃：AI 侧与玩家侧概率取较小值，双方都调得保守才生效。 */
    public static boolean shouldStealFood(boolean enabled, double aiProbability,
                                          double playerProbability, double roll) {
        if (!enabled) return false;
        return roll < clamp01(Math.min(aiProbability, playerProbability));
    }

    /** R11 讨摸：默认 0.05，总开关关闭时永不请求。 */
    public static boolean shouldRequestHeadpat(boolean enabled, double probability, double roll) {
        return enabled && roll < clamp01(probability);
    }

    /**
     * R18（F01）讨摸的上下文硬闸门：战斗中一律不请求；没掉过血也不请求。
     * 用户口径 ——「女仆不会在战斗中请求摸摸，没有掉血的时候也不会请求摸摸」。
     * 这条是安全约束，与 R11 的概率无关：闸门不过，概率再高也不发起。
     * {@code inCombat} 由调用方按「有攻击目标 / 处于战斗任务 / 最近受伤」注入，本类不碰实体。
     */
    public static boolean headpatContextAllowed(boolean inCombat, boolean hasLostHealth) {
        return !inCombat && hasLostHealth;
    }

    /**
     * R19（F02）投喂后讨摸：独立概率，0 表示关闭（「可在设置里关闭」）。
     * 与「干完活 / 击杀」共用的是 R11 那条概率，这里单独一条，互不影响。
     */
    public static boolean headpatOnFeed(double probability, double roll) {
        double p = clamp01(probability);
        return p > 0 && roll < p;
    }

    /** R12 摸头档位：配置档位 1/2；第三档要另过一次低概率掷骰。 */
    public static int headpatTier(int configuredTier, double tier3Probability, double roll) {
        int tier = Math.max(1, Math.min(3, configuredTier));
        if (tier == 3 && roll >= clamp01(tier3Probability)) return 2;
        return tier;
    }

    /** R13 守家/跟随切换冷却：手动指令始终优先（由调用方在外部判断）。 */
    public static boolean maySwitchMode(long elapsedMs, long cooldownMs) {
        return elapsedMs >= cooldownMs;
    }

    /** R14 名字唤醒：叫名字或任一称呼语即可，不再要求面对面。 */
    public static boolean wakeByName(boolean enabled, String spoken, String wakeWords) {
        if (!enabled || spoken == null || spoken.isBlank()) return false;
        String s = spoken.toLowerCase(Locale.ROOT);
        if (wakeWords == null || wakeWords.isBlank()) return true;
        for (String word : wakeWords.split("[,，;；]")) {
            String w = word.trim().toLowerCase(Locale.ROOT);
            if (!w.isEmpty() && s.contains(w)) return true;
        }
        return false;
    }

    /** R15 上下文去重：保序、每句哈希只留一次。每回合只发一遍由 R16 的幂等键在宿主侧保证。 */
    public static List<String> dedupContext(List<String> hashes) {
        return new ArrayList<>(new LinkedHashSet<>(hashes == null ? List.of() : hashes));
    }

    /** R16 多步流程：步数上限，超出就截断并回报。 */
    public static boolean planStepAllowed(int stepIndex, int maxSteps) {
        int cap = maxSteps <= 0 ? 8 : Math.min(maxSteps, 8);
        return stepIndex >= 0 && stepIndex < cap;
    }

    /** R17 双写备份冲突：返回应从哪一端恢复（"wab" / "mod" / "none"）。 */
    public static String backupWinner(String policy, long wabSavedAt, long modSavedAt, boolean verifyPassed) {
        if (!verifyPassed) return "none";
        String p = (policy == null ? "wab_wins" : policy).toLowerCase(Locale.ROOT);
        return switch (p) {
            case "mod_wins" -> "mod";
            case "newest" -> wabSavedAt >= modSavedAt ? "wab" : "mod";
            default -> "wab";
        };
    }

    /** 窒息逃生：空气不足时启用，路径由 auto / air / dig 决定，不调模型。 */
    public static boolean shouldEscapeSuffocation(boolean enabled, int airTicks) {
        return enabled && airTicks <= 0;
    }

    /** 落地水：须持桶；允许自动打水时无桶也放行让女仆自己去取。 */
    public static boolean mayUseMlgWater(boolean enabled, boolean hasBucket, boolean allowFetchWater) {
        return enabled && (hasBucket || allowFetchWater);
    }

    /**
     * R20（F13）岩浆规避：目标格是岩浆，或紧邻岩浆，都不主动进入。
     * 「不主动进入」是硬约束，但玩家显式指路时由调用方决定（本函数只判路径代价）。
     * 返回 true 表示这一格可以踩；false 表示必须绕。
     */
    public static boolean mayStepInto(boolean targetLava, boolean adjacentLava, boolean playerOrdered) {
        if (targetLava) return false;
        return playerOrdered || !adjacentLava;
    }

    /**
     * R21（F14）陡坡下降：原本「不能走下去就瞬移」，现在若跌落会掉血，
     * 先尝试落地水（须持桶 / 允许打水），失败才回落到瞬移。
     * 返回 0=照常走下去，1=落地水，2=瞬移。
     */
    public static int descentPlan(boolean walkable, int fallDistance, int damagingFallDistance,
                                  boolean mlgAvailable) {
        if (walkable || fallDistance < damagingFallDistance) return 0;
        return mlgAvailable ? 1 : 2;
    }

    /** 小模型工具白名单：解析出来的工具名必须过白名单，未列出的直接拒绝。 */
    public static boolean toolAllowed(String tool, Set<String> whitelist) {
        return tool != null && !tool.isBlank() && whitelist != null && whitelist.contains(tool);
    }

    private static double clamp01(double value) {
        if (Double.isNaN(value)) return 0;
        return Math.max(0, Math.min(1, value));
    }
}
