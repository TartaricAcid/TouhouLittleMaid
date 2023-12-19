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
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

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
        super(new StringTextComponent("Maid Beacon GUI"));
        this.beacon = beacon;
        this.overflowDelete = beacon.isOverflowDelete();
        this.potionIndex = beacon.getPotionIndex();
    }

    @Override
    protected void init() {
        this.buttons.clear();
        this.children.clear();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;

        this.addEffectButton(this.leftPos + 146, 26, this.topPos + 19);
        this.addStorageAndTakeButton();
        this.addButton(new Button(leftPos + 118, topPos + 94, 154, 20, getOverflowDeleteButtonText(this.overflowDelete), b -> {
            this.overflowDelete = !this.overflowDelete;
            NetworkHandler.CHANNEL.sendToServer(new SetBeaconOverflowMessage(beacon.getBlockPos(), this.overflowDelete));
            this.init();
        }));
    }

    private void addStorageAndTakeButton() {
        this.addButton(new Button(leftPos + 118, topPos + 72, 76, 20, new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.add_one"), b -> {
            NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), 1, true));
        }));
        this.addButton(new Button(leftPos + 196, topPos + 72, 76, 20, new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.min_one"), b -> {
            NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), 1, false));
        }));
    }

    private void addEffectButton(int start, int spacing, int y) {
        this.addButton(new BeaconEffectButton(BeaconEffect.SPEED, start, y, potionIndex, beacon, (isStateTriggered) -> {
            this.potionIndex = isStateTriggered ? BeaconEffect.SPEED.ordinal() : -1;
            this.init();
        }));
        this.addButton(new BeaconEffectButton(BeaconEffect.FIRE_RESISTANCE, start + spacing, y, potionIndex, beacon, (isStateTriggered) -> {
            this.potionIndex = isStateTriggered ? BeaconEffect.FIRE_RESISTANCE.ordinal() : -1;
            this.init();
        }));
        this.addButton(new BeaconEffectButton(BeaconEffect.STRENGTH, start + spacing * 2, y, potionIndex, beacon, (isStateTriggered) -> {
            this.potionIndex = isStateTriggered ? BeaconEffect.STRENGTH.ordinal() : -1;
            this.init();
        }));
        this.addButton(new BeaconEffectButton(BeaconEffect.RESISTANCE, start + spacing * 3, y, potionIndex, beacon, (isStateTriggered) -> {
            this.potionIndex = isStateTriggered ? BeaconEffect.RESISTANCE.ordinal() : -1;
            this.init();
        }));
        this.addButton(new BeaconEffectButton(BeaconEffect.REGENERATION, start + spacing * 4, y, potionIndex, beacon, (isStateTriggered) -> {
            this.potionIndex = isStateTriggered ? BeaconEffect.REGENERATION.ordinal() : -1;
            this.init();
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        getMinecraft().textureManager.bind(BG);

        blit(matrixStack, leftPos, topPos + 2, 0, 0, 142, 111);
        blit(matrixStack, leftPos + 118, topPos + 1, 44, 111, 154, 15);

        blit(matrixStack, leftPos + 224, topPos + 44, 44, 126, 12, 12);
        blit(matrixStack, leftPos + 224, topPos + 58, 44, 138, 12, 12);

        blit(matrixStack, leftPos + 146, topPos + 46, 58, 128, 74, 9);
        blit(matrixStack, leftPos + 146, topPos + 59, 58, 128, 74, 9);
        float percent = beacon.getStoragePower() / beacon.getMaxStorage();
        blit(matrixStack, leftPos + 146, topPos + 48, 58, 138, (int) (74 * percent), 5);

        this.renderPlayerPower(matrixStack);
        drawString(matrixStack, font, DECIMAL_FORMAT.format(beacon.getStoragePower()), leftPos + 240, topPos + 46, 0xffffff);
        if (potionIndex == -1) {
            this.drawCenteredStringNoShadow(matrixStack, font, I18n.get("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(0)), leftPos + 195, topPos + 5, TextFormatting.DARK_GRAY.getColor());
        } else {
            this.drawCenteredStringNoShadow(matrixStack, font, new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(beacon.getEffectCost() * 900)).withStyle(TextFormatting.RED), leftPos + 195, topPos + 5, 0xffffff);
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.buttons.stream().filter(b -> b instanceof BeaconEffectButton).forEach(b -> ((BeaconEffectButton) b).renderToolTip(matrixStack, this, mouseX, mouseY));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputMappings.Input mouseKey = InputMappings.getKey(keyCode, scanCode);
        if (this.minecraft != null && this.minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void renderPlayerPower(MatrixStack matrixStack) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(power -> {
                float percent = power.get() / PowerCapability.MAX_POWER;
                blit(matrixStack, leftPos + 146, topPos + 61, 58, 143, (int) (74 * percent), 5);
                drawString(matrixStack, font, DECIMAL_FORMAT.format(power.get()), leftPos + 240, topPos + 60, 0xffffff);
            });
        }
    }

    private TranslationTextComponent getOverflowDeleteButtonText(boolean overflowDelete) {
        return overflowDelete ? new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.overflow_delete_true") :
                new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.overflow_delete_false");
    }

    private void drawCenteredStringNoShadow(MatrixStack matrixStack, FontRenderer font, String text, int pX, int pY, int color) {
        font.draw(matrixStack, text, pX - font.width(text) / 2f, pY, color);
    }

    private void drawCenteredStringNoShadow(MatrixStack matrixStack, FontRenderer font, ITextComponent text, int pX, int pY, int color) {
        font.draw(matrixStack, text, pX - font.width(text) / 2f, pY, color);
    }
}
