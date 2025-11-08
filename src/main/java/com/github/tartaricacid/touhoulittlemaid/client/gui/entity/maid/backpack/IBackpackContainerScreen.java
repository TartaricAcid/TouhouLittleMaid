package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack;

import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.BaubleButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.TabIndex;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenMaidGuiPackage;
import net.neoforged.neoforge.network.PacketDistributor;

public interface IBackpackContainerScreen {
    default BaubleButton getBaubleButton(EntityMaid maid, int leftPos, int topPos) {
        return new BaubleButton(leftPos, topPos, false, btn -> {
            OpenMaidGuiPackage message = new OpenMaidGuiPackage(maid.getId(), TabIndex.BAUBLE);
            PacketDistributor.sendToServer(message);
        });
    }
}
