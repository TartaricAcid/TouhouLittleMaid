package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.FurnaceBackpackContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FurnaceBackpackContainerScreen extends AbstractMaidContainerGui<FurnaceBackpackContainer> implements IBackpackContainerScreen {
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_furnace.png");

    public FurnaceBackpackContainerScreen(FurnaceBackpackContainer container, Inventory inv, Component titleIn) {
        super(container, inv, titleIn);
        this.imageHeight = 256;
        this.imageWidth = 256;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float partialTicks, int x, int y) {
        super.renderBg(poseStack, partialTicks, x, y);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKPACK);
        blit(poseStack, leftPos + 85, topPos + 36, 0, 0, 165, 128);
        if (this.menu.isLit()) {
            int litProgress = this.menu.getLitProgress();
            blit(poseStack, leftPos + 161, topPos + 122 + 12 - litProgress, 165, 12 - litProgress, 14, litProgress + 1);
        }
        int burnProgress = this.menu.getBurnProgress();
        blit(poseStack, leftPos + 184, topPos + 120, 165, 14, burnProgress + 1, 16);
    }
}
