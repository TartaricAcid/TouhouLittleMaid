package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.WirelessIOSlotButton;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.WirelessIOSlotConfigMessage;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import static com.github.tartaricacid.touhoulittlemaid.util.BytesBooleansConvert.booleans2Bytes;
import static com.github.tartaricacid.touhoulittlemaid.util.BytesBooleansConvert.bytes2Booleans;

public class WirelessIOConfigSlotGui extends Screen {
    private static final ResourceLocation SLOT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/wireless_io_slot_config.png");
    private static final int SLOT_NUM = 38;
    private final boolean[] configData;
    protected int imageWidth = 155;
    protected int imageHeight = 160;
    protected int leftPos;
    protected int topPos;

    protected WirelessIOConfigSlotGui(ItemStack wirelessIO) {
        super(new StringTextComponent("Wireless IO Config Slot GUI"));
        configData = bytes2Booleans(ItemWirelessIO.getSlotConfig(wirelessIO), SLOT_NUM);
    }

    @Override
    protected void init() {
        this.buttons.clear();
        this.children.clear();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        int index = 0;
        for (int col = 0; col < 6; col++) {
            WirelessIOSlotButton button = new WirelessIOSlotButton(index, leftPos + 8 + 18 * col, topPos + 8, 16, 16, configData);
            button.initTextureValues(158, 0, 16, 16, SLOT);
            addButton(button);
            index++;
        }

        for (int col = 0; col < 6; col++) {
            WirelessIOSlotButton button = new WirelessIOSlotButton(index, leftPos + 8 + 18 * col, topPos + 30, 16, 16, configData);
            button.initTextureValues(158, 0, 16, 16, SLOT);
            addButton(button);
            index++;
        }

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                WirelessIOSlotButton button = new WirelessIOSlotButton(index, leftPos + 8 + 18 * col, topPos + 53 + 18 * row, 16, 16, configData);
                button.initTextureValues(158, 0, 16, 16, SLOT);
                addButton(button);
                index++;
            }
        }

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                WirelessIOSlotButton button = new WirelessIOSlotButton(index, leftPos + 8 + 18 * col, topPos + 94 + 18 * row, 16, 16, configData);
                button.initTextureValues(158, 0, 16, 16, SLOT);
                addButton(button);
                index++;
            }
        }

        for (int row = 0; row < 2; row++) {
            WirelessIOSlotButton button = new WirelessIOSlotButton(index, leftPos + 131, topPos + 8 + 18 * row, 16, 16, configData);
            button.initTextureValues(158, 0, 16, 16, SLOT);
            addButton(button);
            index++;
        }

        Button confirm = new Button(leftPos, topPos + 140, 60, 20, new TranslationTextComponent("gui.done"),
                b -> NetworkHandler.CHANNEL.sendToServer(new WirelessIOSlotConfigMessage(booleans2Bytes(this.configData))));
        Button cancel = new Button(leftPos + 62, topPos + 140, 60, 20, new TranslationTextComponent("gui.cancel"),
                b -> NetworkHandler.CHANNEL.sendToServer(new WirelessIOSlotConfigMessage()));
        addButton(confirm);
        addButton(cancel);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBg(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    private void renderBg(MatrixStack matrixStack) {
        this.renderBackground(matrixStack);
        getMinecraft().textureManager.bind(SLOT);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
