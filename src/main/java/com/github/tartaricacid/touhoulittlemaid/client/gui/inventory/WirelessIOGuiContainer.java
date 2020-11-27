package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.WirelessIOGuiMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class WirelessIOGuiContainer extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation("textures/gui/container/dispenser.png");
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/wireless_io.png");
    private ItemStack wirelessIO;
    private GuiButtonToggle ioModeToggle;
    private GuiButtonToggle filterModeToggle;
    private boolean isInput;
    private boolean isBlacklist;

    public WirelessIOGuiContainer(IInventory playerInventory, ItemStack wirelessIO) {
        super(new WirelessIOContainer(playerInventory, wirelessIO));
        this.wirelessIO = wirelessIO;
        isInput = ItemWirelessIO.isInputMode(wirelessIO);
        isBlacklist = ItemWirelessIO.isBlacklist(wirelessIO);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();

        ioModeToggle = new GuiButtonToggle(1, guiLeft + 26, guiTop + 35, 12, 16, isInput);
        ioModeToggle.initTextureValues(44, 0, -12, 16, ICON);
        filterModeToggle = new GuiButtonToggle(2, guiLeft + 136, guiTop + 35, 16, 16, isBlacklist);
        filterModeToggle.initTextureValues(72, 0, -16, 16, ICON);

        addButton(ioModeToggle);
        addButton(filterModeToggle);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        String ioModeText = isInput ? "到箱子" : "到女仆";
        String filterModeText = isBlacklist ? "黑名单" : "白名单";
        fontRenderer.drawString(ioModeText, guiLeft - fontRenderer.getStringWidth(ioModeText) - 5, guiTop + 5, 0xffffff);
        fontRenderer.drawString(filterModeText, guiLeft - fontRenderer.getStringWidth(filterModeText) - 5, guiTop + 15, 0xffffff);

        super.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 1) {
            isInput = !isInput;
            ioModeToggle.setStateTriggered(isInput);
            CommonProxy.INSTANCE.sendToServer(new WirelessIOGuiMessage(isInput, isBlacklist));
        }
        if (button.id == 2) {
            isBlacklist = !isBlacklist;
            filterModeToggle.setStateTriggered(isBlacklist);
            CommonProxy.INSTANCE.sendToServer(new WirelessIOGuiMessage(isInput, isBlacklist));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        // 绘制主背景
        GlStateManager.color(1, 1, 1);
        mc.getTextureManager().bindTexture(BACKGROUND);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        mc.getTextureManager().bindTexture(ICON);
        this.drawTexturedModalRect(guiLeft + 24, guiTop + 17, 0, 0, 16, 16);
        this.drawTexturedModalRect(guiLeft + 24, guiTop + 53, 16, 0, 16, 16);
    }
}
