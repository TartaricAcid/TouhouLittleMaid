package com.github.tartaricacid.touhoulittlemaid.internal;

import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidAPI.ILittleMaidAPI;
import com.github.tartaricacid.touhoulittlemaid.api.task.FarmHandler;
import com.github.tartaricacid.touhoulittlemaid.api.task.FeedHandler;
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

    private final List<FarmHandler> farmHandlers = Lists.newArrayList();

    private final List<FeedHandler> feedHandlers = Lists.newArrayList();
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
        taskMap.put(task.getUid(), task);
        if (task.getUid().equals(TaskIdle.UID)) {
            IDLE_TASK = task;
            if (!tasks.isEmpty()) {
                tasks.add(0, task);
                return;
            }
        }
        tasks.add(task);
    }

    @Override
    public Optional<IMaidTask> findTask(ResourceLocation uid) {
        return Optional.fromNullable(taskMap.get(uid));
    }

    @Override
    public IMaidTask getIdleTask() {
        return IDLE_TASK;
    }

    @Override
    public void registerFarmHandler(FarmHandler handler) {
        farmHandlers.add(handler);
    }

    @Override
    public List<FarmHandler> getFarmHandlers() {
        return farmHandlers;
    }

    @Override
    public void registerFeedHandler(FeedHandler handler) {
        feedHandlers.add(handler);
    }

    @Override
    public List<FeedHandler> getFeedHandlers() {
        return feedHandlers;
    }

}
