package com.github.tartaricacid.touhoulittlemaid.entity.task;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
// import com.github.tartaricacid.touhoulittlemaid.compat.tacz.TacCompat;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

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
        manager.add(IDLE_TASK);
        manager.add(new TaskAttack());
        manager.add(new TaskBowAttack());
        manager.add(new TaskCrossBowAttack());
        manager.add(new TaskDanmakuAttack());

        // TODO TacZ 兼容
        // TacCompat.initAndAddGunTask(manager);

        manager.add(new TaskNormalFarm());
        manager.add(new TaskSugarCane());
        manager.add(new TaskMelon());
        manager.add(new TaskCocoa());
        manager.add(new TaskHoney());
        manager.add(new TaskGrass());
        manager.add(new TaskSnow());
        manager.add(new TaskFeedOwner());
        manager.add(new TaskShears());
        manager.add(new TaskMilk());
        manager.add(new TaskTorch());
        manager.add(new TaskFeedAnimal());
        manager.add(new TaskExtinguishing());
        manager.add(new TaskBoardGames());
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.addMaidTask(manager);
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
    public void add(IMaidTask task) {
        TASK_MAP.put(task.getUid(), task);
        TASK_INDEX.add(task);
    }
}
