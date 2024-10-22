package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.IBackpackContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.config.MaidConfigContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task.MaidTaskConfigGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidTabButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.TabIndex;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.network.message.ToggleTabPackage;
import net.neoforged.neoforge.network.PacketDistributor;

public class MaidTabs<T extends AbstractMaidContainer> {
    private final int entityId;
    private final int leftPos;
    private final int topPos;

    public MaidTabs(int entityId, int leftPos, int topPos) {
        this.entityId = entityId;
        this.leftPos = leftPos;
        this.topPos = topPos;
    }

    public MaidTabButton[] getTabs(AbstractMaidContainerGui<T> screen) {
        MaidTabButton main = new MaidTabButton(leftPos + 94, topPos + 5, 107, "main",
                (b) -> PacketDistributor.sendToServer(new ToggleTabPackage(entityId, TabIndex.MAIN)));
        if (screen instanceof IBackpackContainerScreen) {
            main.active = false;
        }

        MaidTabButton taskConfig = new MaidTabButton(leftPos + 119, topPos + 5, 132, "task_config",
                (b) -> PacketDistributor.sendToServer(new ToggleTabPackage(entityId, TabIndex.TASK_CONFIG)));
        if (screen instanceof MaidTaskConfigGui<?>) {
            taskConfig.active = false;
        }

        MaidTabButton maidConfig = new MaidTabButton(leftPos + 144, topPos + 5, 157, "maid_config",
                (b) -> PacketDistributor.sendToServer(new ToggleTabPackage(entityId, TabIndex.MAID_CONFIG)));
        if (screen instanceof MaidConfigContainerGui) {
            maidConfig.active = false;
        }

        return new MaidTabButton[]{main, taskConfig, maidConfig};
    }
}