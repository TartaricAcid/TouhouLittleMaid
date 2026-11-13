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
        check(WabRules.mayUseMlgWater(true, false, true), "落地水无桶可自动打水");
        check(!WabRules.mayUseMlgWater(true, false, false), "落地水无桶且不许打水");
        check(WabRules.toolAllowed("maid_status", Set.of("maid_status")), "工具白名单放行");
        check(!WabRules.toolAllowed("maid_drop_all", Set.of("maid_status")), "工具白名单拒绝");

        // R18（F01）讨摸上下文硬闸门：战斗中 / 未掉血一律不请求
        check(WabRules.headpatContextAllowed(false, true), "R18 未战斗且掉过血才允许");
        check(!WabRules.headpatContextAllowed(true, true), "R18 战斗中即使掉血也不请求");
        check(!WabRules.headpatContextAllowed(false, false), "R18 没掉血不请求");
        check(!WabRules.headpatContextAllowed(true, false), "R18 战斗且没掉血");
        check(!WabRules.shouldRequestHeadpat(WabRules.headpatContextAllowed(true, false), 1.0, 0.0),
                "R18 闸门不过时概率再高也不发起");

        // R19（F02）投喂后讨摸：0 = 关闭，独立概率
        check(!WabRules.headpatOnFeed(0.0, 0.0), "R19 概率 0 表示关闭");
        check(WabRules.headpatOnFeed(0.05, 0.01), "R19 低概率命中");
        check(!WabRules.headpatOnFeed(0.05, 0.5), "R19 概率未命中");
        check(!WabRules.headpatOnFeed(-1, 0.0), "R19 负值钳到 0");
        check(!WabRules.headpatOnFeed(Double.NaN, 0.0), "R19 NaN 视为关闭");

        // R20（F13）岩浆规避：不主动进入岩浆
        check(!WabRules.mayStepInto(true, false, false), "R20 目标格是岩浆一律不进");
        check(!WabRules.mayStepInto(true, true, true), "R20 玩家显式指路也不进岩浆");
        check(!WabRules.mayStepInto(false, true, false), "R20 紧邻岩浆且非玩家指令时绕开");
        check(WabRules.mayStepInto(false, true, true), "R20 玩家显式指路可邻近");
        check(WabRules.mayStepInto(false, false, false), "R20 周围安全正常走");

        // R21（F14）陡坡下降：能走就走，掉血落差优先落地水，失败才瞬移
        eq(WabRules.descentPlan(true, 20, 4, true), 0, "R21 能走下去就照常走");
        eq(WabRules.descentPlan(false, 2, 4, true), 0, "R21 落差不足不掉血照常走");
        eq(WabRules.descentPlan(false, 6, 4, true), 1, "R21 掉血且可用落地水优先落地水");
        eq(WabRules.descentPlan(false, 6, 4, false), 2, "R21 无落地水才回落瞬移");

        // F09 筷子燃料：空筷子 = 木棍 100 tick 的两倍；盛着一口食物时不可作燃料
        eq(WabRules.CHOPSTICK_BURN_TICKS, 200, "F09 筷子燃料是木棍两倍");
        eq(WabRules.chopstickBurnTicks(false), 200, "F09 空筷子可烧 200 tick");
        eq(WabRules.chopstickBurnTicks(true), 0, "F09 盛着食物的筷子不作燃料");

        // ===== B5 行为决策 =====

        // F05 玩家抱睡同意：好感足够 + 心情不差 → 同意；好感不足 → 拒绝；低心情小概率勉强同意
        eq(WabRules.sleepConsent(false, 0.9, 0.5, 0.3, 0.0), 0, "F05 关闭则拒绝");
        eq(WabRules.sleepConsent(true, 0.3, 0.5, 0.3, 0.0), 0, "F05 好感不足拒绝");
        eq(WabRules.sleepConsent(true, 0.9, 0.5, 0.4, 0.0), 1, "F05 好感高且心情好同意");
        eq(WabRules.sleepConsent(true, 0.9, 0.5, -0.9, 0.99), 0, "F05 低心情且不掷中拒绝");
        eq(WabRules.sleepConsent(true, 0.9, 0.5, -0.9, 0.01), 2, "F05 低心情小概率勉强同意");
        eq(WabRules.sleepConsent(true, 1.0, 1.0, 0.5, 0.0), 1, "F05 满分好感通过高阈值");

        // F06 自主坐下：空闲未达阈值不坐；达阈值命中掷骰
        check(!WabRules.mayAutoSit(true, 1.0, 5.0, 0.1, 0.0), "F06 空闲不足不坐");
        check(WabRules.mayAutoSit(true, 9.0, 5.0, 0.1, 0.01), "F06 空闲足够且命中");
        check(!WabRules.mayAutoSit(false, 9.0, 5.0, 1.0, 0.0), "F06 关闭不坐");

        // F07 偷吃融合优先级
        eq(WabRules.stealFoodPlan(false, true, true, 0, 3000, 0.1, 0.0), 0, "F07 关闭不动");
        eq(WabRules.stealFoodPlan(true, true, true, 0, 3000, 0.1, 0.0), 1, "F07 饿+主人在→先投喂");
        eq(WabRules.stealFoodPlan(true, true, true, 4000, 3000, 0.1, 0.0), 2, "F07 投喂超时→自吃");
        eq(WabRules.stealFoodPlan(true, true, false, 0, 3000, 0.1, 0.0), 2, "F07 饿+主人不在→自吃");
        eq(WabRules.stealFoodPlan(true, false, true, 0, 3000, 0.5, 0.01), 3, "F07 不饿按概率自吃命中");
        eq(WabRules.stealFoodPlan(true, false, true, 0, 3000, 0.5, 0.9), 0, "F07 不饿概率未命中");

        // F10 自吃 / 中毒自救
        check(WabRules.shouldSelfEat(true, false), "F10 饿且主人不在→自吃");
        check(!WabRules.shouldSelfEat(true, true), "F10 饿但主人在→不自吃（先投喂）");
        eq(WabRules.poisonResponse(false, true), 0, "F10 没中毒无需");
        eq(WabRules.poisonResponse(true, true), 1, "F10 中毒且有牛奶→喝");
        eq(WabRules.poisonResponse(true, false), 2, "F10 中毒无牛奶→求助主人");

        // F12 火逃生：身边有水跳、无水面落地水、皆无不动（维持原版）
        eq(WabRules.fireEscapePlan(false, true, true), 0, "F12 没着火不动");
        eq(WabRules.fireEscapePlan(true, true, true), 1, "F12 着火且身边有水→跳水");
        eq(WabRules.fireEscapePlan(true, false, true), 2, "F12 着火无水面→落地水");
        eq(WabRules.fireEscapePlan(true, false, false), 0, "F12 着火无水无桶→维持原版");
        check(WabRules.shouldRetrieveMlg(true, true), "F12 落地水后灭火→收回");
        check(!WabRules.shouldRetrieveMlg(true, false), "F12 火未灭不提前收回");

        // F17 驯服生物保护优先级
        eq(WabRules.tameProtectPriority(true, false, false), 3, "F17 本女仆驯服最高");
        eq(WabRules.tameProtectPriority(false, true, false), 2, "F17 玩家其次");
        eq(WabRules.tameProtectPriority(false, false, true), 1, "F17 其余女仆再次");
        eq(WabRules.tameProtectPriority(false, false, false), 0, "F17 无主最低");

        // F18 建造原点：AI 显式给原点才用
        check(WabRules.useAiProvidedOrigin(true), "F18 用 AI 给的原点");
        check(!WabRules.useAiProvidedOrigin(false), "F18 否则用女仆当前坐标");

        System.out.println("WabRules 边界自测通过: " + passed + " 条断言");
    }
}
