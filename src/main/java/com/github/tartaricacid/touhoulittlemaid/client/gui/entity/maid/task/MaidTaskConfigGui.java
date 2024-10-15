package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.SideTab;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.TaskConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.network.message.RefreshMaidBrainPackage;
import com.github.tartaricacid.touhoulittlemaid.network.message.ToggleSideTabPackage;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public abstract class MaidTaskConfigGui<T extends TaskConfigContainer> extends AbstractMaidContainerGui<T> {
    public MaidTaskConfigGui(T screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
    }

    @Override
    protected void taskButtonPressed(IMaidTask maidTask, boolean enable) {
        super.taskButtonPressed(maidTask, enable);
        if (this.maid != null) {
            PacketDistributor.sendToServer(new ToggleSideTabPackage(this.maid.getId(), SideTab.TASK_CONFIG.getIndex(), maidTask.getUid()));
        }
    }

    @Override
    public void onClose() {
        // 重置女仆 Brain，让女仆重新读取任务相关数据
        if (this.maid != null) {
            PacketDistributor.sendToServer(new RefreshMaidBrainPackage(maid.getId()));
        }
        super.onClose();
    }
}