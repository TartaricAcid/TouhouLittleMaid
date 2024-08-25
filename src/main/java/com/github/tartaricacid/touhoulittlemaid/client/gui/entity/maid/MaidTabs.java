package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.IBackpackContainerScreen;
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
        MaidTabButton main = new MaidTabButton(leftPos + 94, topPos + 5, 107, (b) ->
                PacketDistributor.sendToServer(new ToggleTabPackage(entityId, TabIndex.MAIN)));
        if (screen instanceof IBackpackContainerScreen) {
            main.active = false;
        }

        MaidTabButton config = new MaidTabButton(leftPos + 219, topPos + 5, 232, (b) ->
                PacketDistributor.sendToServer(new ToggleTabPackage(entityId, TabIndex.CONFIG)));
        if (screen instanceof MaidConfigContainerGui) {
            config.active = false;
        }

        return new MaidTabButton[]{main, config};
    }
}