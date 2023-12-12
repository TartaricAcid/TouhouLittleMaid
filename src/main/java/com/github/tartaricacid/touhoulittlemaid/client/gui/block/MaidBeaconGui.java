package com.github.tartaricacid.touhoulittlemaid.client.gui.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.BeaconEffectButton;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetBeaconOverflowMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.StorageAndTakePowerMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon.BeaconEffect;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.text.DecimalFormat;

public class MaidBeaconGui extends Screen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_beacon.png");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private final TileEntityMaidBeacon beacon;

    protected int imageWidth = 300;
    protected int imageHeight = 113;
    protected int leftPos;
    protected int topPos;
    private boolean overflowDelete;
    private int potionIndex;

    public MaidBeaconGui(TileEntityMaidBeacon beacon) {
        super(Component.literal("Maid Beacon GUI"));
        this.beacon = beacon;
        this.overflowDelete = beacon.isOverflowDelete();
        this.potionIndex = beacon.getPotionIndex();
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.addEffectButton(this.leftPos + 146, 26, this.topPos + 19);
        this.addStorageAndTakeButton();
        this.addRenderableWidget(Button.builder(getOverflowDeleteButtonText(this.overflowDelete), b -> {
            this.overflowDelete = !this.overflowDelete;
            NetworkHandler.CHANNEL.sendToServer(new SetBeaconOverflowMessage(beacon.getBlockPos(), this.overflowDelete));
            this.init();
        }).pos(leftPos + 118, topPos + 94).size(154, 20).build());
    }

    private void addStorageAndTakeButton() {
        this.addRenderableWidget(Button.builder(Component.translatable("gui.touhou_little_maid.maid_beacon.add_one"), b -> {
            NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), 1, true));
        }).pos(leftPos + 118, topPos + 72).size(76, 20).build());

        this.addRenderableWidget(Button.builder(Component.translatable("gui.touhou_little_maid.maid_beacon.min_one"), b -> {
            NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), 1, false));
        }).pos(leftPos + 196, topPos + 72).size(76, 20).build());
    }

    private void addEffectButton(int start, int spacing, int y) {
        this.addRenderableWidget(new BeaconEffectButton(BeaconEffect.SPEED, start, y, potionIndex, beacon, (isStateTriggered) -> {
            this.potionIndex = isStateTriggered ? BeaconEffect.SPEED.ordinal() : -1;
            this.init();
        }));
        this.addRenderableWidget(new BeaconEffectButton(BeaconEffect.FIRE_RESISTANCE, start + spacing, y, potionIndex, beacon, (isStateTriggered) -> {
            this.potionIndex = isStateTriggered ? BeaconEffect.FIRE_RESISTANCE.ordinal() : -1;
            this.init();
        }));
        this.addRenderableWidget(new BeaconEffectButton(BeaconEffect.STRENGTH, start + spacing * 2, y, potionIndex, beacon, (isStateTriggered) -> {
            this.potionIndex = isStateTriggered ? BeaconEffect.STRENGTH.ordinal() : -1;
            this.init();
        }));
        this.addRenderableWidget(new BeaconEffectButton(BeaconEffect.RESISTANCE, start + spacing * 3, y, potionIndex, beacon, (isStateTriggered) -> {
            this.potionIndex = isStateTriggered ? BeaconEffect.RESISTANCE.ordinal() : -1;
            this.init();
        }));
        this.addRenderableWidget(new BeaconEffectButton(BeaconEffect.REGENERATION, start + spacing * 4, y, potionIndex, beacon, (isStateTriggered) -> {
            this.potionIndex = isStateTriggered ? BeaconEffect.REGENERATION.ordinal() : -1;
            this.init();
        }));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        graphics.blit(BG, leftPos, topPos + 2, 0, 0, 142, 111);
        graphics.blit(BG, leftPos + 118, topPos + 1, 44, 111, 154, 15);

        graphics.blit(BG, leftPos + 224, topPos + 44, 44, 126, 12, 12);
        graphics.blit(BG, leftPos + 224, topPos + 58, 44, 138, 12, 12);

        graphics.blit(BG, leftPos + 146, topPos + 46, 58, 128, 74, 9);
        graphics.blit(BG, leftPos + 146, topPos + 59, 58, 128, 74, 9);
        float percent = beacon.getStoragePower() / beacon.getMaxStorage();
        graphics.blit(BG, leftPos + 146, topPos + 48, 58, 138, (int) (74 * percent), 5);

        this.renderPlayerPower(graphics);
        graphics.drawString(font, DECIMAL_FORMAT.format(beacon.getStoragePower()), leftPos + 240, topPos + 46, 0xffffff);
        if (potionIndex == -1) {
            this.drawCenteredString(graphics, font, I18n.get("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(0)), leftPos + 195, topPos + 5, ChatFormatting.DARK_GRAY.getColor());
        } else {
            this.drawCenteredString(graphics, font, Component.translatable("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(beacon.getEffectCost() * 900)).withStyle(ChatFormatting.RED), leftPos + 195, topPos + 5, 0xffffff);
        }

        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
        if (this.minecraft != null && this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void renderPlayerPower(GuiGraphics graphics) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(power -> {
                float percent = power.get() / PowerCapability.MAX_POWER;
                graphics.blit(BG, leftPos + 146, topPos + 61, 58, 143, (int) (74 * percent), 5);
                graphics.drawString(font, DECIMAL_FORMAT.format(power.get()), leftPos + 240, topPos + 60, 0xffffff);
            });
        }
    }

    private MutableComponent getOverflowDeleteButtonText(boolean overflowDelete) {
        return overflowDelete ? Component.translatable("gui.touhou_little_maid.maid_beacon.overflow_delete_true") :
                Component.translatable("gui.touhou_little_maid.maid_beacon.overflow_delete_false");
    }

    private void drawCenteredString(GuiGraphics graphics, Font font, String text, int pX, int pY, int color) {
        graphics.drawString(font, text, pX - font.width(text) / 2, pY, color, false);
    }

    private void drawCenteredString(GuiGraphics graphics, Font font, Component text, int pX, int pY, int color) {
        graphics.drawString(font, text, pX - font.width(text) / 2, pY, color, false);
    }
}
