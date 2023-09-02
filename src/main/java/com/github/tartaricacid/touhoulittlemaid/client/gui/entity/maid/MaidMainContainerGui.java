package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MaidMainContainerGui extends AbstractMaidContainerGui<MaidMainContainer> {
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_gui_backpack.png");
    private final EntityMaid maid;

    public MaidMainContainerGui(MaidMainContainer screenContainer, Inventory inv, Component titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = 256;
        this.imageWidth = 256;
        this.maid = menu.getMaid();
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);
        this.drawBackpackGui(matrixStack);
    }

    private void drawBackpackGui(PoseStack matrixStack) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKPACK);
        blit(matrixStack, leftPos + 85, topPos + 36, 0, 0, 165, 122);

        int level = maid.getBackpackLevel();
        if (level < BackpackLevel.SMALL) {
            fill(matrixStack, leftPos + 142, topPos + 58, leftPos + 250, topPos + 76, 0xaa222222);
            blit(matrixStack, leftPos + 190, topPos + 62, 165, 0, 11, 11);
        }
        if (level < BackpackLevel.MIDDLE) {
            fill(matrixStack, leftPos + 142, topPos + 81, leftPos + 250, topPos + 117, 0xaa222222);
            blit(matrixStack, leftPos + 190, topPos + 92, 165, 0, 11, 11);
        }
        if (level < BackpackLevel.BIG) {
            fill(matrixStack, leftPos + 142, topPos + 122, leftPos + 250, topPos + 158, 0xaa222222);
            blit(matrixStack, leftPos + 190, topPos + 133, 165, 0, 11, 11);
        }
    }
}