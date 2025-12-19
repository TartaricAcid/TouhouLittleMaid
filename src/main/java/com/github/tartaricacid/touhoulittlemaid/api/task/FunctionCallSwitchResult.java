package com.github.tartaricacid.touhoulittlemaid.api.task;

import org.jetbrains.annotations.ApiStatus;

/**
 * 通过 Function Call 进行任务切换的结果枚举。
 */
@ApiStatus.AvailableSince("1.4.7")
public enum FunctionCallSwitchResult {
    /**
     * 切换成功且准备就绪。
     */
    OK,
    /**
     * 已处于目标状态，无需变更。
     */
    NO_CHANGE,
    /**
     * 已切换，但缺少关键物品。
     */
    MISSING_REQUIRED_ITEM,
    /**
     * 切换完成但仅部分满足条件（例如有替代方案可用）。
     */
    PARTIAL_OK
}
