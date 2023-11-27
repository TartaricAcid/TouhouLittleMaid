package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class MaidMainContainerGui extends AbstractMaidContainerGui<MaidMainContainer> {
    private final EntityMaid maid;

    public MaidMainContainerGui(MaidMainContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 256;
        this.imageWidth = 256;
        this.maid = menu.getMaid();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderBg(graphics, partialTicks, x, y);
        this.maid.getMaidBackpackType().drawBackpackScreen(graphics, maid, leftPos, topPos);
    }
}