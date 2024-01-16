package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.TankBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.util.MaidFluidRender;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
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
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKPACK);
        blit(matrixStack, leftPos + 85, topPos + 36, 0, 0, 165, 128);

        RenderSystem.enableBlend();
        MaidFluidRender.drawFluid(matrixStack, leftPos + 200, topPos + 108, 29, 50, maid.getBackpackFluid(), this.menu.getFluidCount(), TankBackpackData.CAPACITY);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.disableBlend();

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKPACK);
        blit(matrixStack, leftPos + 197, topPos + 104, 165, 0, 34, 50);

        boolean xInRange = leftPos + 196 <= x && x <= leftPos + 196 + 29;
        boolean yInRange = topPos + 108 <= y && y <= topPos + 108 + 50;
        if (xInRange && yInRange) {
            MutableComponent fluidInfo = new TranslatableComponent("tooltips.touhou_little_maid.tank_backpack.fluid",
                    MaidFluidRender.getFluidName(maid.getBackpackFluid(), this.menu.getFluidCount()),
                    this.menu.getFluidCount()).withStyle(ChatFormatting.GRAY);
            MutableComponent capacityInfo = new TranslatableComponent("tooltips.touhou_little_maid.tank_backpack.capacity", TankBackpackData.CAPACITY)
                    .withStyle(ChatFormatting.GRAY);
            renderComponentTooltip(matrixStack, Lists.newArrayList(fluidInfo, capacityInfo), x, y);
        }
    }
}
