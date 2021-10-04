package com.github.tartaricacid.touhoulittlemaid.api;

import com.github.tartaricacid.touhoulittlemaid.block.multiblock.MultiBlockManager;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;

public interface ILittleMaid {
    /**
     * 为物品绑定女仆饰品属性
     *
     * @param manager 注册器
     */
    default void bindMaidBauble(BaubleManager manager) {
    }

    /**
     * 添加女仆的 Task
     *
     * @param manager 注册器
     */
    default void addMaidTask(TaskManager manager) {
    }

    /**
     * 添加多方块结构
     *
     * @param manager 注册器
     */
    default void addMultiBlock(MultiBlockManager manager) {
    }
}
