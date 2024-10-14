package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.RefreshMaidBrainMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class MaidTaskConfigGui<T extends TaskConfigContainer> extends AbstractMaidContainerGui<T> {
    public MaidTaskConfigGui(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    public void onClose() {
        // 重置女仆 Brain，让女仆重新读取任务相关数据
        if (this.maid != null) {
            NetworkHandler.CHANNEL.sendToServer(new RefreshMaidBrainMessage(maid.getId()));
        }
        super.onClose();
    }
}