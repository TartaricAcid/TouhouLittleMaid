package com.github.tartaricacid.touhoulittlemaid.internal;

import java.util.List;
import java.util.Map;

import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI.ILittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.api.util.ItemDefinition;
import com.github.tartaricacid.touhoulittlemaid.internal.task.TaskIdle;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.util.ResourceLocation;

public class LittleMaidAPIImpl implements ILittleMaidAPI
{
    private IMaidTask IDLE_TASK;

    private final Map<ItemDefinition, IMaidBauble> baubles = Maps.newHashMap();
    private final Map<ResourceLocation, IMaidTask> taskMap = Maps.newHashMap();
    private final List<IMaidTask> tasks = Lists.newArrayList();

    @Override
    public IMaidBauble registerBauble(ItemDefinition item, IMaidBauble bauble)
    {
        return baubles.put(item, bauble);
    }

    @Override
    public IMaidBauble getBauble(ItemDefinition item)
    {
        return baubles.get(item);
    }

    @Override
    public List<IMaidTask> getTasks()
    {
        return tasks;
    }

    @Override
    public void registerTask(IMaidTask task)
    {
        if (!tasks.isEmpty() && task.getUid().equals(TaskIdle.UID))
        {
            tasks.add(0, task);
            IDLE_TASK = task;
        }
        else
        {
            tasks.add(task);
        }
        taskMap.put(task.getUid(), task);
    }

    @Override
    public Optional<IMaidTask> findTask(ResourceLocation uid)
    {
        return Optional.fromNullable(taskMap.get(uid));
    }

    @Override
    public IMaidTask getIdleTask()
    {
        return IDLE_TASK;
    }

}
