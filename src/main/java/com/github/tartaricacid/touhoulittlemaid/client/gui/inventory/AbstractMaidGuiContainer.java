package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidMode;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.ChangeGuiMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.ChangeHomeDataMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.ChangeMaidModeMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.ChangePickupDataMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public abstract class AbstractMaidGuiContainer extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/inventory_main.png");
    EntityMaid entityMaid;
    private int guiId;
    private GuiButtonToggle togglePickup;
    private GuiButtonToggle toggleHome;

    public AbstractMaidGuiContainer(Container inventorySlotsIn, EntityMaid entityMaid, int guiId) {
        super(inventorySlotsIn);
        this.guiId = guiId;
        this.entityMaid = entityMaid;
    }

    public abstract void drawCustomBackground(int mouseX, int mouseY, float partialTicks);

    public abstract void drawCustomScreen(int mouseX, int mouseY, float partialTicks);

    @Override
    public void initGui() {
        super.initGui();
        int i = this.guiLeft;
        int j = this.guiTop;

        // 切换是否拾起物品的按钮
        togglePickup = new GuiButtonToggle(0, i + 143, j + 63, 26, 16, entityMaid.isPickup());
        togglePickup.initTextureValues(178, 0, 28, 18, BACKGROUND);
        this.buttonList.add(togglePickup);

        // 不同标签页切换按钮
        this.buttonList.add(new GuiButtonImage(1, i + 3, j - 25, 22,
                26, 234, 234, 0, BACKGROUND));
        this.buttonList.add(new GuiButtonImage(2, i + 31, j - 25, 22,
                26, 234, 234, 0, BACKGROUND));
        this.buttonList.add(new GuiButtonImage(3, i + 59, j - 25, 22,
                26, 234, 234, 0, BACKGROUND));

        // 模式切换按钮
        this.buttonList.add(new GuiButtonImage(10, i - 28, j, 28,
                26, 225, 230, 0, BACKGROUND));

        // 切换是否开启 home 模式的按钮
        toggleHome = new GuiButtonToggle(11, i + 116, j + 63, 26, 16, entityMaid.isHome());
        toggleHome.initTextureValues(178, 36, 28, 18, BACKGROUND);
        this.buttonList.add(toggleHome);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 0) {
            if (entityMaid.isPickup()) {
                togglePickup.setStateTriggered(false);
                CommonProxy.INSTANCE.sendToServer(new ChangePickupDataMessage(entityMaid.getUniqueID(), false));
            } else {
                togglePickup.setStateTriggered(true);
                CommonProxy.INSTANCE.sendToServer(new ChangePickupDataMessage(entityMaid.getUniqueID(), true));
            }
        }

        if (button.id == 1) {
            CommonProxy.INSTANCE.sendToServer(new ChangeGuiMessage(mc.player.getUniqueID(), entityMaid.getEntityId(), 1));
        }

        if (button.id == 2) {
            CommonProxy.INSTANCE.sendToServer(new ChangeGuiMessage(mc.player.getUniqueID(), entityMaid.getEntityId(), 2));
        }
        if (button.id == 3) {
            CommonProxy.INSTANCE.sendToServer(new ChangeGuiMessage(mc.player.getUniqueID(), entityMaid.getEntityId(), 3));
        }

        if (button.id == 10) {
            int modeIndex = (entityMaid.getMode().getModeIndex() + 1 > MaidMode.getLength() - 1) ? 0 : (entityMaid.getMode().getModeIndex() + 1);
            CommonProxy.INSTANCE.sendToServer(new ChangeMaidModeMessage(entityMaid.getUniqueID(), MaidMode.getMode(modeIndex)));
        }

        if (button.id == 11) {
            if (entityMaid.isHome()) {
                toggleHome.setStateTriggered(false);
                CommonProxy.INSTANCE.sendToServer(new ChangeHomeDataMessage(entityMaid.getUniqueID(), false));
            } else {
                toggleHome.setStateTriggered(true);
                CommonProxy.INSTANCE.sendToServer(new ChangeHomeDataMessage(entityMaid.getUniqueID(), true));
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        // 绘制自定义前景
        this.drawCustomScreen(mouseX, mouseY, partialTicks);

        // 绘制物品的文本提示
        this.renderHoveredToolTip(mouseX, mouseY);

        int i = this.guiLeft;
        int j = this.guiTop;
        // 绘制模式上方的文字提示
        if (((i - 28) < mouseX && mouseX < i) && (j < mouseY && mouseY < (j + 26))) {
            this.drawHoveringText(String.format("This Maid Is In %s Mode", entityMaid.getMode().name().toUpperCase()), mouseX, mouseY);
        }
    }


    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        int i = this.guiLeft;
        int j = this.guiTop;

        this.drawDefaultBackground();

        // 绘制选择图标背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(i, j - 28, 0, 193, 112, 32);

        // 绘制主背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        // 绘制自定义背景
        drawCustomBackground(mouseX, mouseY, partialTicks);

        // 绘制选择图标前景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(i + 28 * (guiId - 1), j - 28, 28 * (guiId - 1), 224, 28, 32);

        // 绘制模式图标背景
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(i - 28, j, 0, 167, 31, 26);

        // 绘制模式图标
        this.drawItemStack(Items.WRITABLE_BOOK.getDefaultInstance(), i + 6, j - 19, "");
        this.drawItemStack(ItemBlock.getItemFromBlock(Blocks.CHEST).getDefaultInstance(), i + 34, j - 19, "");
        this.drawItemStack(Items.NETHER_WART.getDefaultInstance(), i + 62, j - 19, "");
        this.drawItemStack(Items.DIAMOND_SWORD.getDefaultInstance(), i + 90, j - 19, "");

        // 绘制模式图标
        this.drawItemStack(entityMaid.getMode().getItemIcon().getDefaultInstance(), i - 20, j + 5, "");

        // 绘制女仆样子
        GuiInventory.drawEntityOnScreen(i + 51, j + 70, 30,
                (float) (i + 51) - mouseX, (float) (j + 70 - 45) - mouseY, this.entityMaid);
    }

    private void drawItemStack(ItemStack stack, int x, int y, String altText) {
        RenderHelper.enableGUIStandardItemLighting();

        GlStateManager.translate(0.0F, 0.0F, 32.0F);
        this.zLevel = 200.0F;
        this.itemRender.zLevel = 200.0F;
        net.minecraft.client.gui.FontRenderer font = stack.getItem().getFontRenderer(stack);
        if (font == null) font = fontRenderer;
        this.itemRender.renderItemAndEffectIntoGUI(stack, x, y);
        this.itemRender.renderItemOverlayIntoGUI(font, stack, x, y, altText);
        this.zLevel = 0.0F;
        this.itemRender.zLevel = 0.0F;

        RenderHelper.disableStandardItemLighting();
    }
}
