package com.github.tartaricacid.touhoulittlemaid.internal;

import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI.ILittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.api.util.ItemDefinition;
import com.github.tartaricacid.touhoulittlemaid.internal.task.TaskIdle;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;

import java.util.List;
import java.util.Map;

/**
 * 本模组自己对 API 的实现
 *
 * @author Snownee
 * @date 2019/7/24 02:31
 */
public class LittleMaidAPIImpl implements ILittleMaidAPI {
    /**
     * 物品 -> 饰品 的映射
     */
    private final Map<ItemDefinition, IMaidBauble> baubles = Maps.newHashMap();
    /**
     * 模式 ID -> IMaidTask 对象的映射
     */
    private final Map<ResourceLocation, IMaidTask> taskMap = Maps.newHashMap();
    /**
     * 目前可用的 IMaidTask 对象
     */
    private final List<IMaidTask> tasks = Lists.newArrayList();
    /**
     * 默认的 IMaidTask 对象
     */
    private IMaidTask IDLE_TASK;

    @Override
    public IMaidBauble registerBauble(ItemDefinition item, IMaidBauble bauble) {
        return baubles.put(item, bauble);
    }

    @Override
    public IMaidBauble getBauble(ItemDefinition item) {
        return baubles.get(item);
    }

    @Override
    public List<IMaidTask> getTasks() {
        return tasks;
    }

    @Override
    public void registerTask(IMaidTask task) {
        if (!tasks.isEmpty() && task.getUid().equals(TaskIdle.UID)) {
            tasks.add(0, task);
            IDLE_TASK = task;
        } else {
            tasks.add(task);
        }
        taskMap.put(task.getUid(), task);
    }

    @Override
    public Optional<IMaidTask> findTask(ResourceLocation uid) {
        return Optional.fromNullable(taskMap.get(uid));
    }

    @Override
    public IMaidTask getIdleTask() {
        return IDLE_TASK;
    }

}
