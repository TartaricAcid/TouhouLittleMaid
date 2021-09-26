package com.github.tartaricacid.touhoulittlemaid.api;

import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;

public interface ILittleMaid {
    /**
     * 注册女仆的 Task
     *
     * @param manager 注册器
     */
    default void registerMaidTask(TaskManager manager) {
    }
}
