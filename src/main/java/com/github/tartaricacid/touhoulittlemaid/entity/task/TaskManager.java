package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.entity.task.instance.TaskAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.task.instance.TaskIdle;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.Map;
import java.util.Optional;

public final class TaskManager {
    private static final Map<ResourceLocation, IMaidTask> TASK_MAP = Maps.newHashMap();
    private static final IMaidTask IDLE_TASK = new TaskIdle();

    /**
     * 注册 Task
     */
    public static void registerTask(IMaidTask task) {
        TASK_MAP.put(task.getUid(), task);
    }

    /**
     * 获取 Task
     */
    public static Optional<IMaidTask> findTask(ResourceLocation uid) {
        return Optional.ofNullable(TASK_MAP.get(uid));
    }

    /**
     * 默认 Task
     */
    public static IMaidTask getIdleTask() {
        return IDLE_TASK;
    }

    void register() {
        registerTask(IDLE_TASK);
        registerTask(new TaskAttack());
    }
}
