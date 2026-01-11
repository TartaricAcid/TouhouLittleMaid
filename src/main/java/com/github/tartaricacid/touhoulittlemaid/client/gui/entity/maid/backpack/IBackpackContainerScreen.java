package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack;

import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.BaubleButton;
import com.github.tartaricacid.touhoulittlemaid.compat.curios.client.CuriosButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.TabIndex;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenMaidGuiMessage;

public interface IBackpackContainerScreen {
    default BaubleButton getBaubleButton(EntityMaid maid, int leftPos, int topPos) {
        return new BaubleButton(leftPos, topPos, false, btn -> {
            OpenMaidGuiMessage message = new OpenMaidGuiMessage(maid.getId(), TabIndex.BAUBLE);
            NetworkHandler.CHANNEL.sendToServer(message);
        });
    }

    default CuriosButton getCuriosButton(EntityMaid maid, int leftPos, int topPos) {
        return new CuriosButton(leftPos, topPos, false, btn -> {
            OpenMaidGuiMessage message = new OpenMaidGuiMessage(maid.getId(), TabIndex.CURIOS);
            NetworkHandler.CHANNEL.sendToServer(message);
        });
    }
}
