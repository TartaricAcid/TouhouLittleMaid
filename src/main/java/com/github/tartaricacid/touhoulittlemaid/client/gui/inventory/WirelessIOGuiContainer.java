package com.github.tartaricacid.touhoulittlemaid.client.gui.inventory;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.WirelessIOGuiMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.WirelessIOSlotConfigMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class WirelessIOGuiContainer extends GuiContainer {
    private static final ResourceLocation BACKGROUND = new ResourceLocation("textures/gui/container/dispenser.png");
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/wireless_io.png");
    private static final ResourceLocation SLOT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/wireless_io_slot_config.png");

    private static final int SLOT_NUM = 47;
    private GuiButtonToggle ioModeToggle;
    private GuiButtonToggle filterModeToggle;
    private boolean isInput;
    private boolean isBlacklist;

    public WirelessIOGuiContainer(IInventory playerInventory, ItemStack wirelessIO) {
        super(new WirelessIOContainer(playerInventory, wirelessIO));
        isInput = ItemWirelessIO.isInputMode(wirelessIO);
        isBlacklist = ItemWirelessIO.isBlacklist(wirelessIO);
    }

    public static boolean[] bytes2Booleans(byte[] bytes) {
        if (bytes == null) {
            bytes = new byte[SLOT_NUM];
        }
        boolean[] out = new boolean[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            out[i] = (bytes[i] != 0);
        }
        return out;
    }

    public static byte[] booleans2Bytes(boolean[] booleans) {
        byte[] out = new byte[booleans.length];
        for (int i = 0; i < booleans.length; i++) {
            out[i] = (byte) (booleans[i] ? 1 : 0);
        }
        return out;
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();

        ioModeToggle = new GuiButtonToggle(1, guiLeft + 26, guiTop + 35, 12, 16, isInput);
        ioModeToggle.initTextureValues(44, 0, -12, 16, ICON);
        filterModeToggle = new GuiButtonToggle(2, guiLeft + 136, guiTop + 26, 16, 16, isBlacklist);
        filterModeToggle.initTextureValues(72, 0, -16, 16, ICON);
        GuiButtonImage configButton = new GuiButtonImage(3, guiLeft + 136, guiTop + 44, 16, 16,
                88, 0, 16, ICON);

        addButton(ioModeToggle);
        addButton(filterModeToggle);
        addButton(configButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        String ioModeText = isInput ?
                I18n.format("tooltips.touhou_little_maid.wireless_io.io_mode.input") :
                I18n.format("tooltips.touhou_little_maid.wireless_io.io_mode.output");
        String filterModeText = isBlacklist ?
                I18n.format("tooltips.touhou_little_maid.wireless_io.filter_mode.blacklist") :
                I18n.format("tooltips.touhou_little_maid.wireless_io.filter_mode.whitelist");
        fontRenderer.drawString(ioModeText, guiLeft - fontRenderer.getStringWidth(ioModeText) - 5, guiTop + 5, 0xffffff);
        fontRenderer.drawString(filterModeText, guiLeft - fontRenderer.getStringWidth(filterModeText) - 5, guiTop + 15, 0xffffff);

        boolean xInRange;
        boolean yInRange;

        xInRange = (guiLeft + 26) < mouseX && mouseX < (guiLeft + 38);
        yInRange = (guiTop + 35) < mouseY && mouseY < (guiTop + 51);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.wireless_io.io_mode"), mouseX, mouseY);
        }

        xInRange = (guiLeft + 136) < mouseX && mouseX < (guiLeft + 152);
        yInRange = (guiTop + 26) < mouseY && mouseY < (guiTop + 42);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.wireless_io.filter_mode"), mouseX, mouseY);
        }

        xInRange = (guiLeft + 136) < mouseX && mouseX < (guiLeft + 152);
        yInRange = (guiTop + 44) < mouseY && mouseY < (guiTop + 60);
        if (xInRange && yInRange) {
            this.drawHoveringText(I18n.format("gui.touhou_little_maid.wireless_io.config_slot"), mouseX, mouseY);
        }

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
        if (button.id == 3) {
            if (mc.player.getHeldItemMainhand().getItem() == MaidItems.WIRELESS_IO) {
                mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiConfigSlot(mc.player.getHeldItemMainhand())));
            }
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

    private static class GuiConfigSlot extends GuiScreen {
        private final GuiButtonToggle[] slotButtons;
        private boolean[] configData;

        public GuiConfigSlot(ItemStack wirelessIO) {
            slotButtons = new GuiButtonToggle[SLOT_NUM];
            configData = new boolean[SLOT_NUM];
            byte[] tmp = ItemWirelessIO.getSlotConfig(wirelessIO);
            if (tmp != null) {
                configData = bytes2Booleans(tmp);
            }
        }

        @Override
        public void initGui() {
            int startX = (width - 104) / 2;
            int startY = (height - 205) / 2;

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 5; j++) {
                    int id = i * 5 + j;
                    slotButtons[id] = new GuiButtonToggle(id, startX + 8 + 18 * j, startY + 6 + 18 * i, 16, 16, configData[id]);
                    slotButtons[id].initTextureValues(140, 0, 16, 16, SLOT);
                    addButton(slotButtons[id]);
                }
            }

            for (int k = 0; k < 3; k++) {
                for (int i = 0; i < 2; i++) {
                    for (int j = 0; j < 5; j++) {
                        int id = 15 + k * 10 + i * 5 + j;
                        slotButtons[id] = new GuiButtonToggle(id, startX + 8 + 18 * j, startY + 64 + 40 * k + 18 * i, 16, 16, configData[id]);
                        slotButtons[id].initTextureValues(140, 0, 16, 16, SLOT);
                        addButton(slotButtons[id]);
                    }
                }
            }

            slotButtons[45] = new GuiButtonToggle(45, startX + 113, startY + 8, 16, 16, configData[45]);
            slotButtons[45].initTextureValues(140, 0, 16, 16, SLOT);
            addButton(slotButtons[45]);

            slotButtons[46] = new GuiButtonToggle(46, startX + 113, startY + 26, 16, 16, configData[46]);
            slotButtons[46].initTextureValues(140, 0, 16, 16, SLOT);
            addButton(slotButtons[46]);

            GuiButton returnButton = new GuiButton(-1, (width - 104) / 2, startY + 188, 104, 20,
                    I18n.format("gui.touhou_little_maid.wireless_io.config_slot.return"));
            addButton(returnButton);
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            int startX = (width - 104) / 2;
            int startY = (height - 205) / 2;

            drawDefaultBackground();
            mc.getTextureManager().bindTexture(SLOT);
            this.drawTexturedModalRect(startX, startY, 0, 0, 137, 205);
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(MaidItems.MAID_BACKPACK, 1, 1), startX - 18, startY + 64);
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(MaidItems.MAID_BACKPACK, 1, 2), startX - 18, startY + 104);
            mc.getRenderItem().renderItemAndEffectIntoGUI(new ItemStack(MaidItems.MAID_BACKPACK, 1, 3), startX - 18, startY + 144);

            GlStateManager.enableBlend();
            super.drawScreen(mouseX, mouseY, partialTicks);
            GlStateManager.disableBlend();
        }

        @Override
        protected void actionPerformed(GuiButton button) {
            if (0 <= button.id && button.id < SLOT_NUM) {
                slotButtons[button.id].setStateTriggered(!configData[button.id]);
                configData[button.id] = !configData[button.id];
                return;
            }
            if (button.id == -1) {
                CommonProxy.INSTANCE.sendToServer(new WirelessIOSlotConfigMessage(booleans2Bytes(configData), false));
            }
        }

        @Override
        public void onGuiClosed() {
            CommonProxy.INSTANCE.sendToServer(new WirelessIOSlotConfigMessage(booleans2Bytes(configData), true));
        }
    }
}
