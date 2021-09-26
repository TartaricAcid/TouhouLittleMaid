package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class TaskManager {
    private static Map<ResourceLocation, IMaidTask> TASK_MAP;
    private static List<IMaidTask> TASK_INDEX;
    private static IMaidTask IDLE_TASK;

    private TaskManager() {
        IDLE_TASK = new TaskIdle();
        TASK_MAP = Maps.newHashMap();
        TASK_INDEX = Lists.newArrayList();
    }

    public static void init() {
        TaskManager manager = new TaskManager();
        manager.registerTask(IDLE_TASK);
        manager.registerTask(new TaskAttack());
        manager.registerTask(new TaskBowAttack());
        manager.registerTask(new TaskDanmakuAttack());
        manager.registerTask(new TaskNormalFarm());
        manager.registerTask(new TaskSugarCane());
        manager.registerTask(new TaskMelon());
        manager.registerTask(new TaskCocoa());
        manager.registerTask(new TaskGrass());
        manager.registerTask(new TaskSnow());
        manager.registerTask(new TaskFeedOwner());
        manager.registerTask(new TaskShears());
        manager.registerTask(new TaskMilk());
        manager.registerTask(new TaskTorch());
        manager.registerTask(new TaskFeedAnimal());
        manager.registerTask(new TaskExtinguishing());
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.registerMaidTask(manager);
        }
        TASK_MAP = ImmutableMap.copyOf(TASK_MAP);
        TASK_INDEX = ImmutableList.copyOf(TASK_INDEX);
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

    public static Map<ResourceLocation, IMaidTask> getTaskMap() {
        return TASK_MAP;
    }

    public static List<IMaidTask> getTaskIndex() {
        return TASK_INDEX;
    }

    /**
     * 注册 Task
     */
    public void registerTask(IMaidTask task) {
        TASK_MAP.put(task.getUid(), task);
        TASK_INDEX.add(task);
    }
}
