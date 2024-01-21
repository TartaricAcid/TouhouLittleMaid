package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.TankBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.util.MaidFluidRender;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.anti_ad.mc.ipn.api.IPNButton;
import org.anti_ad.mc.ipn.api.IPNGuiHint;
import org.anti_ad.mc.ipn.api.IPNPlayerSideOnly;

@IPNPlayerSideOnly
@IPNGuiHint(button = IPNButton.SORT, horizontalOffset = -36, bottom = -12)
@IPNGuiHint(button = IPNButton.SORT_COLUMNS, horizontalOffset = -24, bottom = -24)
@IPNGuiHint(button = IPNButton.SORT_ROWS, horizontalOffset = -12, bottom = -36)
@IPNGuiHint(button = IPNButton.SHOW_EDITOR, horizontalOffset = -5)
@IPNGuiHint(button = IPNButton.SETTINGS, horizontalOffset = -5)
public class TankBackpackContainerScreen extends AbstractMaidContainerGui<TankBackpackContainer> implements IBackpackContainerScreen {
    private static final ResourceLocation BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_tank.png");
    private final EntityMaid maid;

    public TankBackpackContainerScreen(TankBackpackContainer container, PlayerInventory inv, ITextComponent titleIn) {
        super(container, inv, titleIn);
        this.imageHeight = 256;
        this.imageWidth = 256;
        this.maid = menu.getMaid();
    }

    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        super.renderBg(matrixStack, partialTicks, x, y);
        getMinecraft().textureManager.bind(BACKPACK);
        blit(matrixStack, leftPos + 85, topPos + 36, 0, 0, 165, 128);

        RenderSystem.enableBlend();
        RenderSystem.enableAlphaTest();
        MaidFluidRender.drawFluid(matrixStack, leftPos + 200, topPos + 108, 29, 50, maid.getBackpackFluid(), this.menu.getFluidCount(), TankBackpackData.CAPACITY);
        RenderSystem.color4f(1, 1, 1, 1);
        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();

        getMinecraft().textureManager.bind(BACKPACK);
        blit(matrixStack, leftPos + 197, topPos + 104, 165, 0, 34, 50);

        boolean xInRange = leftPos + 196 <= x && x <= leftPos + 196 + 29;
        boolean yInRange = topPos + 108 <= y && y <= topPos + 108 + 50;
        if (xInRange && yInRange) {
            IFormattableTextComponent fluidInfo = new TranslationTextComponent("tooltips.touhou_little_maid.tank_backpack.fluid",
                    MaidFluidRender.getFluidName(maid.getBackpackFluid(), this.menu.getFluidCount()),
                    this.menu.getFluidCount()).withStyle(TextFormatting.GRAY);
            IFormattableTextComponent capacityInfo = new TranslationTextComponent("tooltips.touhou_little_maid.tank_backpack.capacity", TankBackpackData.CAPACITY)
                    .withStyle(TextFormatting.GRAY);
            renderComponentTooltip(matrixStack, Lists.newArrayList(fluidInfo, capacityInfo), x, y);
        }
    }
}
