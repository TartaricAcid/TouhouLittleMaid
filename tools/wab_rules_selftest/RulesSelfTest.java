import com.github.tartaricacid.touhoulittlemaid.compat.wab.config.WabRules;

import java.util.List;
import java.util.Set;

/**
 * 纯代码规则引擎的边界自测。不依赖 MC、不需要 JUnit，javac + java 直接跑：
 *   javac -d out RulesSelfTest.java <源码>/WabRules.java && java -cp out RulesSelfTest
 * 断言失败抛 AssertionError 并以非 0 退出，CI 据此拦下。
 */
public final class RulesSelfTest {
    private static int passed;

    private static void check(boolean condition, String name) {
        if (!condition) throw new AssertionError("规则断言失败: " + name);
        passed++;
    }

    private static void eq(Object actual, Object expected, String name) {
        if (expected == null ? actual != null : !expected.equals(actual))
            throw new AssertionError("规则断言失败: " + name + " 期望 " + expected + " 实际 " + actual);
        passed++;
    }

    public static void main(String[] args) {
        // R01/R02 深度思考与思考模式
        check(WabRules.autoDisableDeepThink(0.2, 0.5), "R01 低热度自动关深度思考");
        check(!WabRules.autoDisableDeepThink(0.8, 0.5), "R01 高热度保留");
        eq(WabRules.resolveThinkMode("off", true, 1.0, 0.5), 0, "R02 off");
        eq(WabRules.resolveThinkMode("deep", false, 1.0, 0.5), 1, "R02 模型不支持时不返回 deep");
        eq(WabRules.resolveThinkMode("auto", true, 0.9, 0.5), 2, "R02 auto 高热度走 deep");
        eq(WabRules.resolveThinkMode("auto", true, 0.1, 0.5), 1, "R02 auto 低热度走 fast");

        // R03 求安慰：落地水成功必须抑制
        check(!WabRules.shouldSeekComfort(true, true, true, 1.0, 0.0), "R03 落地水成功抑制求安慰");
        check(WabRules.shouldSeekComfort(true, true, false, 0.05, 0.01), "R03 濒死低概率命中");
        check(!WabRules.shouldSeekComfort(true, true, false, 0.05, 0.5), "R03 概率未命中");
        check(!WabRules.shouldSeekComfort(false, true, false, 1.0, 0.0), "R03 总开关关闭");

        // R04/R05/R06 可选 AI、间隔、节流
        check(!WabRules.shouldUseAi(true, false), "R04 AI 不可用回落代码");
        check(WabRules.withinHourlyCap(2, 3), "R06 未达上限");
        check(!WabRules.withinHourlyCap(3, 3), "R06 达到上限");
        check(WabRules.withinHourlyCap(99, 0), "R06 0 表示不限");
        eq(WabRules.autonomyIntervalMs("fixed", 5, 3, 8, 0.5), 300000L, "R05 固定 5 分钟");
        eq(WabRules.autonomyIntervalMs("random_range", 5, 3, 8, 0.0), 180000L, "R05 随机下界");
        eq(WabRules.autonomyIntervalMs("random_range", 5, 3, 8, 1.0), 480000L, "R05 随机上界");

        // R07 捡起筛选
        check(WabRules.matchesPickupFilter("item_id", "minecraft:stick", "木棍", 0, false, ""), "R07 默认按 id");
        check(WabRules.matchesPickupFilter("item_id", "minecraft:stick", "木棍", 10, false, "damage<=50"), "R07 damage 表达式");
        check(!WabRules.matchesPickupFilter("item_id", "minecraft:stick", "木棍", 60, false, "damage<=50"), "R07 damage 超限");
        check(WabRules.matchesPickupFilter("enchant", "x", "x", 0, true, "enchant:fortune"), "R07 附魔表达式");
        check(!WabRules.matchesPickupFilter("enchant", "x", "x", 0, false, ""), "R07 无附魔不收");

        // R08 农耕四要素
        eq(WabRules.missingFarmingSpec("hoe", "wheat", "9x9", true), "", "R08 四要素齐备");
        eq(WabRules.missingFarmingSpec("", "wheat", "9x9", true), "工具", "R08 缺工具");
        eq(WabRules.missingFarmingSpec("hoe", "", "9x9", true), "种植物", "R08 缺种植物");
        eq(WabRules.missingFarmingSpec("", "", "", false), "", "R08 未强制时不拦");

        // R09–R12 摸头、偷吃、下棋
        check(!WabRules.shouldCryOnLose(false, 1.0, 0.0), "R09 关闭时不哭");
        check(WabRules.shouldStealFood(true, 0.05, 0.2, 0.01), "R10 取较小概率命中");
        check(!WabRules.shouldStealFood(true, 0.05, 0.2, 0.1), "R10 取较小概率未命中");
        check(!WabRules.shouldRequestHeadpat(false, 1.0, 0.0), "R11 摸头总开关关闭");
        check(WabRules.shouldRequestHeadpat(true, 0.05, 0.01), "R11 讨摸低概率命中");
        eq(WabRules.headpatTier(3, 0.05, 0.9), 2, "R12 三档未掷中回落二档");
        eq(WabRules.headpatTier(3, 0.05, 0.01), 3, "R12 三档掷中");

        // R13–R17 冷却、唤醒、去重、步数、备份
        check(!WabRules.maySwitchMode(1000, 2000), "R13 冷却未过");
        check(WabRules.maySwitchMode(2000, 2000), "R13 冷却刚好过");
        check(WabRules.wakeByName(true, "小灵过来", "小灵,灵灵"), "R14 称呼语命中");
        check(!WabRules.wakeByName(false, "小灵过来", "小灵"), "R14 开关关闭");
        eq(WabRules.dedupContext(List.of("a", "b", "a", "c", "b")), List.of("a", "b", "c"), "R15 保序去重");
        check(WabRules.planStepAllowed(7, 8), "R16 第 8 步允许");
        check(!WabRules.planStepAllowed(8, 8), "R16 超步数截断");
        check(!WabRules.planStepAllowed(9, 0), "R16 默认上限 8");
        eq(WabRules.backupWinner("wab_wins", 1, 999, true), "wab", "R17 wab_wins");
        eq(WabRules.backupWinner("newest", 1, 999, true), "mod", "R17 newest 取模组");
        eq(WabRules.backupWinner("newest", 1, 999, false), "none", "R17 校验失败拒绝恢复");

        // 逃生与兼容补充规则
        check(WabRules.shouldEscapeSuffocation(true, 0), "窒息空气耗尽触发");
        check(!WabRules.shouldEscapeSuffocation(false, 0), "窒息开关关闭");
        check(!WabRules.mayUseLavaBoat(true, true, false), "岩浆船仅濒死");
        check(WabRules.mayUseMlgWater(true, false, true), "落地水无桶可自动打水");
        check(!WabRules.mayUseMlgWater(true, false, false), "落地水无桶且不许打水");
        check(WabRules.toolAllowed("maid_status", Set.of("maid_status")), "工具白名单放行");
        check(!WabRules.toolAllowed("maid_drop_all", Set.of("maid_status")), "工具白名单拒绝");

        System.out.println("WabRules 边界自测通过: " + passed + " 条断言");
    }
}
