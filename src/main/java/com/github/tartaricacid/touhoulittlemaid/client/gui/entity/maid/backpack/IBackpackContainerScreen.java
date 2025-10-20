package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.TabIndex;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenMaidGuiMessage;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

public interface IBackpackContainerScreen {
    ResourceLocation BAUBLE_BUTTON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/bauble_button.png");

    default ImageButton getBaubleButton(EntityMaid maid, int leftPos, int topPos) {
        return new ImageButton(leftPos + 85, topPos + 97, 54, 63,
                0, 0, 0, BAUBLE_BUTTON, (btn) -> {
            OpenMaidGuiMessage message = new OpenMaidGuiMessage(maid.getId(), TabIndex.BAUBLE);
            NetworkHandler.CHANNEL.sendToServer(message);
        });
    }
}
