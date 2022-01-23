package com.github.tartaricacid.touhoulittlemaid.client.gui.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.BeaconEffectButton;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetBeaconOverflowMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.StorageAndTakePowerMessage;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon.BeaconEffect;
import com.github.tartaricacid.touhoulittlemaid.util.ShapeDraw;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;

public class MaidBeaconGui extends Screen {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_beacon.png");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private final TileEntityMaidBeacon beacon;

    protected int imageWidth = 256;
    protected int imageHeight = 105;
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
        int start = this.leftPos + 16;
        int spacing = 26;
        int y = this.topPos + 15;

        this.addEffectButton(start, spacing, y);
        this.addStorageAndTakeButton();
        this.addButton(new Button(leftPos + 7, topPos - 22, 242, 20, getOverflowDeleteButtonText(this.overflowDelete), b -> {
            this.overflowDelete = !this.overflowDelete;
            NetworkHandler.CHANNEL.sendToServer(new SetBeaconOverflowMessage(beacon.getBlockPos(), this.overflowDelete));
            this.init();
        }));
    }

    private void addStorageAndTakeButton() {
        this.addButton(new Button(leftPos + 16, topPos + 48, 62, 20, new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.add_one"), b -> {
            NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), 1, true));
        }));
        this.addButton(new Button(leftPos + 80, topPos + 48, 62, 20, new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.add_all"), b -> {
            ClientPlayerEntity player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(power -> NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), power.get(), true)));
            }
        }));
        this.addButton(new Button(leftPos + 16, topPos + 70, 62, 20, new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.min_one"), b -> {
            NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), 1, false));
        }));
        this.addButton(new Button(leftPos + 80, topPos + 70, 62, 20, new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.min_all"), b -> {
            NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), beacon.getStoragePower(), false));
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
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        this.renderPlayerPower(matrixStack);
        drawCenteredString(matrixStack, font, I18n.get("gui.touhou_little_maid.maid_beacon.storage_power", DECIMAL_FORMAT.format(beacon.getStoragePower())), leftPos + 198, topPos + 22, 0xffffff);
        if (potionIndex == -1) {
            drawCenteredString(matrixStack, font, I18n.get("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(0)), leftPos + 198, topPos + 32, 0xffffff);
        } else {
            drawCenteredString(matrixStack, font, new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(beacon.getEffectCost() * 900)).withStyle(TextFormatting.RED), leftPos + 198, topPos + 32, 0xffffff);
        }

        ShapeDraw.drawCircle(leftPos + 180, topPos + 70, 20, 64, 0x22066b6d);
        ShapeDraw.drawSector(leftPos + 180, topPos + 70, 22, 0, beacon.getStoragePower() / beacon.getMaxStorage() * 2 * Math.PI, 64, 0xff40e4e5);

        drawCenteredString(matrixStack, font, String.format("%.2f%%", beacon.getStoragePower() / beacon.getMaxStorage() * 100), leftPos + 223, topPos + 66, 0xffffff);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
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
                drawCenteredString(matrixStack, font, I18n.get("gui.touhou_little_maid.maid_beacon.player_power",
                        DECIMAL_FORMAT.format(power.get())), leftPos + 198, topPos + 10 + 2, 0xffffff);
            });
        }
    }

    private TranslationTextComponent getOverflowDeleteButtonText(boolean overflowDelete) {
        return overflowDelete ? new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.overflow_delete_true") :
                new TranslationTextComponent("gui.touhou_little_maid.maid_beacon.overflow_delete_false");
    }
}
