package com.github.tartaricacid.touhoulittlemaid.api.task;

/**
 * @author Snownee
 * @date 2019/7/25 21:41
 */
public enum Trend {
    // 恢复的饱食度超过当前玩家饥饿值的食物，暂时不会进行喂食
    GOOD,
    // 恢复的饱食度低于当前玩家饥饿值的食物，优先级次之
    BAD,
    // 恰好匹配当前饥饿值的食物，也可用于一些具有特殊效果的东西，优先级最高
    EXACT,
    // 不属于上述分类的食物
    NONE
}
