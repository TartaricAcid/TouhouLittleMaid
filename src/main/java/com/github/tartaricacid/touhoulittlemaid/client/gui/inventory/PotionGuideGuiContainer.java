package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.PotionGuideContainer;
import com.github.tartaricacid.touhoulittlemaid.item.ItemPotionGuide;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.PotionGuideIndexMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;

public class PotionGuideGuiContainer extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/potion_guide.png");
    private int index;

    public PotionGuideGuiContainer(IInventory playerInventory, ItemStack guide) {
        super(new PotionGuideContainer(playerInventory, guide));
        this.xSize = 176;
        this.ySize = 133;
        this.index = ItemPotionGuide.getGuideIndex(guide);
    }

    @Override
    public void initGui() {
        super.initGui();
        addButton(new GuiButton(1, guiLeft + 117, guiTop + 10, 52, 20,
                I18n.format("gui.touhou_little_maid.potion_guide.clear_index.return")));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.color(1, 1, 1);
        mc.renderEngine.bindTexture(BACKGROUND);
        drawTexturedModalRect(guiLeft + 7 + 18 * index, guiTop + 29, 7 + 18 * index, 133, 18, 18);
        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    @ParametersAreNonnullByDefault
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            index = 0;
            CommonProxy.INSTANCE.sendToServer(new PotionGuideIndexMessage(index));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 176, 133);
    }
}
