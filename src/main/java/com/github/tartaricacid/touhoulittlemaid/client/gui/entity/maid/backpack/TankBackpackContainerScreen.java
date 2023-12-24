package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.TankBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.util.MaidFluidRender;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class TankBackpackContainerScreen extends AbstractMaidContainerGui<TankBackpackContainer> implements IBackpackContainerScreen {
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_tank.png");
    private final EntityMaid maid;

    public TankBackpackContainerScreen(TankBackpackContainer container, Inventory inv, Component titleIn) {
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

        RenderSystem.enableBlend();
        MaidFluidRender.drawFluid(graphics, leftPos + 197, topPos + 104, 34, 50, maid.getBackpackFluid(), this.menu.getFluidCount(), TankBackpackData.CAPACITY);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();
        graphics.blit(BACKPACK, leftPos + 197, topPos + 104, 165, 0, 34, 50);


        graphics.drawString(font, this.menu.getFluidCount() + " mB", leftPos + 145, topPos + 130, 0x000000, false);
        graphics.drawString(font, MaidFluidRender.getFluidName(maid.getBackpackFluid(), this.menu.getFluidCount()), leftPos + 145, topPos + 140, 0x000000, false);
    }
}
