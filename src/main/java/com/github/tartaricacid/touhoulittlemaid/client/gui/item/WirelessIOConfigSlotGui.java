package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.WirelessIOSlotButton;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import net.neoforged.neoforge.network.PacketDistributor;
import com.github.tartaricacid.touhoulittlemaid.network.message.WirelessIOSlotConfigMessage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
        super(Component.literal("Wireless IO Config Slot GUI"));
        configData = bytes2Booleans(ItemWirelessIO.getSlotConfig(wirelessIO), SLOT_NUM);
    }

    @Override
    protected void init() {
        this.clearWidgets();

        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        int index = 0;
        for (int col = 0; col < 6; col++) {
            WirelessIOSlotButton button = new WirelessIOSlotButton(index, leftPos + 8 + 18 * col, topPos + 8, 16, 16, configData);
            button.initTextureValues(158, 0, 16, 16, SLOT);
            addRenderableWidget(button);
            index++;
        }

        for (int col = 0; col < 6; col++) {
            WirelessIOSlotButton button = new WirelessIOSlotButton(index, leftPos + 8 + 18 * col, topPos + 30, 16, 16, configData);
            button.initTextureValues(158, 0, 16, 16, SLOT);
            addRenderableWidget(button);
            index++;
        }

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                WirelessIOSlotButton button = new WirelessIOSlotButton(index, leftPos + 8 + 18 * col, topPos + 53 + 18 * row, 16, 16, configData);
                button.initTextureValues(158, 0, 16, 16, SLOT);
                addRenderableWidget(button);
                index++;
            }
        }

        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 6; col++) {
                WirelessIOSlotButton button = new WirelessIOSlotButton(index, leftPos + 8 + 18 * col, topPos + 94 + 18 * row, 16, 16, configData);
                button.initTextureValues(158, 0, 16, 16, SLOT);
                addRenderableWidget(button);
                index++;
            }
        }

        for (int row = 0; row < 2; row++) {
            WirelessIOSlotButton button = new WirelessIOSlotButton(index, leftPos + 131, topPos + 8 + 18 * row, 16, 16, configData);
            button.initTextureValues(158, 0, 16, 16, SLOT);
            addRenderableWidget(button);
            index++;
        }

        Button confirm = Button.builder(Component.translatable("gui.done"), b -> PacketDistributor.sendToServer(new WirelessIOSlotConfigMessage(booleans2Bytes(this.configData))))
                .pos(leftPos, topPos + 140).size(60, 20).build();
        Button cancel = Button.builder(Component.translatable("gui.cancel"), b -> PacketDistributor.sendToServer(new WirelessIOSlotConfigMessage()))
                .pos(leftPos + 62, topPos + 140).size(60, 20).build();
        addRenderableWidget(confirm);
        addRenderableWidget(cancel);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBg(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    private void renderBg(GuiGraphics guiGraphics) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(SLOT, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }
}
