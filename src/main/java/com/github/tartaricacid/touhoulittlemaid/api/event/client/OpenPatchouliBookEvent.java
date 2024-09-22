package com.github.tartaricacid.touhoulittlemaid.api.event.client;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.eventbus.api.Event;

/**
 * 用于侧边栏帕秋莉按钮打开记忆中的幻想乡手册
 * <br>默认打开手册上一回所停留的页面
 * <br>可自定义打开手册任意页面
 * <br>详见{#PatchouliAPI}
 */
public class OpenPatchouliBookEvent extends Event {
    private final EntityMaid maid;
    private final IMaidTask task;

    public OpenPatchouliBookEvent(EntityMaid maid, IMaidTask task) {
        this.maid = maid;
        this.task = task;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public IMaidTask getTask() {
        return task;
    }
}
