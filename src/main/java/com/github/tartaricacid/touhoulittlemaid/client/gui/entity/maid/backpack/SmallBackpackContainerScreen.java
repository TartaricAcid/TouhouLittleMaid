package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.SmallBackpackContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
//import org.anti_ad.mc.ipn.api.IPNButton;
//import org.anti_ad.mc.ipn.api.IPNGuiHint;
//import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;
//
//@IPNPlayerSideOnly
//@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
//@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
//@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
//@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
//@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
//TODO INP兼容
public class SmallBackpackContainerScreen extends AbstractMaidContainerGui<SmallBackpackContainer> implements IBackpackContainerScreen {
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_backpack.png");
    private final EntityMaid maid;

    public SmallBackpackContainerScreen(SmallBackpackContainer container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.imageHeight = 256;
        this.imageWidth = 256;
        this.maid = menu.getMaid();
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        super.renderBg(graphics, partialTicks, x, y);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKPACK);
        graphics.blit(BACKPACK, leftPos + 85, topPos + 36, 0, 0, 165, 128);
        graphics.fill(leftPos + 142, topPos + 81, leftPos + 250, topPos + 117, 0xaa222222);
        graphics.blit(BACKPACK, leftPos + 190, topPos + 92, 165, 0, 11, 11);
        graphics.fill(leftPos + 142, topPos + 122, leftPos + 250, topPos + 158, 0xaa222222);
        graphics.blit(BACKPACK, leftPos + 190, topPos + 133, 165, 0, 11, 11);
    }
}
