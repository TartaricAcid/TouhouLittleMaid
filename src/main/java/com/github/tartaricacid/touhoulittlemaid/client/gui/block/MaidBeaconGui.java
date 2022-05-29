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
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

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
        super(new TextComponent("Maid Beacon GUI"));
        this.beacon = beacon;
        this.overflowDelete = beacon.isOverflowDelete();
        this.potionIndex = beacon.getPotionIndex();
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        int start = this.leftPos + 16;
        int spacing = 26;
        int y = this.topPos + 15;

        this.addEffectButton(start, spacing, y);
        this.addStorageAndTakeButton();
        this.addRenderableWidget(new Button(leftPos + 7, topPos - 22, 242, 20, getOverflowDeleteButtonText(this.overflowDelete), b -> {
            this.overflowDelete = !this.overflowDelete;
            NetworkHandler.CHANNEL.sendToServer(new SetBeaconOverflowMessage(beacon.getBlockPos(), this.overflowDelete));
            this.init();
        }));
    }

    private void addStorageAndTakeButton() {
        this.addRenderableWidget(new Button(leftPos + 16, topPos + 48, 62, 20, new TranslatableComponent("gui.touhou_little_maid.maid_beacon.add_one"), b -> {
            NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), 1, true));
        }));
        this.addRenderableWidget(new Button(leftPos + 80, topPos + 48, 62, 20, new TranslatableComponent("gui.touhou_little_maid.maid_beacon.add_all"), b -> {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(power -> NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), power.get(), true)));
            }
        }));
        this.addRenderableWidget(new Button(leftPos + 16, topPos + 70, 62, 20, new TranslatableComponent("gui.touhou_little_maid.maid_beacon.min_one"), b -> {
            NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), 1, false));
        }));
        this.addRenderableWidget(new Button(leftPos + 80, topPos + 70, 62, 20, new TranslatableComponent("gui.touhou_little_maid.maid_beacon.min_all"), b -> {
            NetworkHandler.CHANNEL.sendToServer(new StorageAndTakePowerMessage(beacon.getBlockPos(), beacon.getStoragePower(), false));
        }));
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
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(poseStack);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BG);
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        this.renderPlayerPower(poseStack);
        drawCenteredString(poseStack, font, I18n.get("gui.touhou_little_maid.maid_beacon.storage_power", DECIMAL_FORMAT.format(beacon.getStoragePower())), leftPos + 198, topPos + 22, 0xffffff);
        if (potionIndex == -1) {
            drawCenteredString(poseStack, font, I18n.get("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(0)), leftPos + 198, topPos + 32, 0xffffff);
        } else {
            drawCenteredString(poseStack, font, new TranslatableComponent("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(beacon.getEffectCost() * 900)).withStyle(ChatFormatting.RED), leftPos + 198, topPos + 32, 0xffffff);
        }

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        ShapeDraw.drawCircle(leftPos + 180, topPos + 70, 20, 64, 0x22066b6d);
        ShapeDraw.drawSector(leftPos + 180, topPos + 70, 22, 0, beacon.getStoragePower() / beacon.getMaxStorage() * 2 * Math.PI, 64, 0xff40e4e5);

        drawCenteredString(poseStack, font, String.format("%.2f%%", beacon.getStoragePower() / beacon.getMaxStorage() * 100), leftPos + 223, topPos + 66, 0xffffff);
        super.render(poseStack, mouseX, mouseY, partialTicks);
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

    private void renderPlayerPower(PoseStack poseStack) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(power -> {
                drawCenteredString(poseStack, font, I18n.get("gui.touhou_little_maid.maid_beacon.player_power",
                        DECIMAL_FORMAT.format(power.get())), leftPos + 198, topPos + 10 + 2, 0xffffff);
            });
        }
    }

    private TranslatableComponent getOverflowDeleteButtonText(boolean overflowDelete) {
        return overflowDelete ? new TranslatableComponent("gui.touhou_little_maid.maid_beacon.overflow_delete_true") :
                new TranslatableComponent("gui.touhou_little_maid.maid_beacon.overflow_delete_false");
    }
}
