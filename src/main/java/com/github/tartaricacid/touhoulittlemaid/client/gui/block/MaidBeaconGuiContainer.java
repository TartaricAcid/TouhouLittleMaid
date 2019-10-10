package com.github.tartaricacid.touhoulittlemaid.client.gui.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.CapabilityPowerHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidBeaconContainer;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SetBeaconPotionMessage;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.StorageAndTakePowerMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import com.github.tartaricacid.touhoulittlemaid.util.RenderHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonToggle;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.MobEffects;
import net.minecraft.util.ResourceLocation;

import java.text.DecimalFormat;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.block.MaidBeaconGuiContainer.Button.*;
import static com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon.COST;
import static com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon.Effect.*;
import static com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon.MAX_STORAGE;

/**
 * @author TartaricAcid
 * @date 2019/10/9 20:00
 **/
public class MaidBeaconGuiContainer extends GuiContainer {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/maid_beacon.png");
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("0.00");
    private TileEntityMaidBeacon beacon;
    private GuiButtonToggle speed;
    private GuiButtonToggle fireResistance;
    private GuiButtonToggle strength;
    private GuiButtonToggle resistance;
    private GuiButtonToggle regeneration;

    public MaidBeaconGuiContainer(MaidBeaconContainer maidBeaconContainer) {
        super(maidBeaconContainer);
        this.xSize = 256;
        this.ySize = 105;
        this.beacon = maidBeaconContainer.getTileEntityMaidBeacon();
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = this.guiLeft;
        int j = this.guiTop;
        int start = i + 16;
        int spacing = 26;
        int y = j + 15;

        speed = new GuiButtonToggle(SPEED.ordinal(), start, y, 22, 22, beacon.getPotionIndex() == SPEED.ordinal());
        speed.initTextureValues(0, 111, 22, 22, BG);
        this.buttonList.add(speed);

        fireResistance = new GuiButtonToggle(FIRE_RESISTANCE.ordinal(), start + spacing, y, 22, 22, beacon.getPotionIndex() == FIRE_RESISTANCE.ordinal());
        fireResistance.initTextureValues(0, 111, 22, 22, BG);
        this.buttonList.add(fireResistance);

        strength = new GuiButtonToggle(STRENGTH.ordinal(), start + spacing * 2, y, 22, 22, beacon.getPotionIndex() == STRENGTH.ordinal());
        strength.initTextureValues(0, 111, 22, 22, BG);
        this.buttonList.add(strength);

        resistance = new GuiButtonToggle(RESISTANCE.ordinal(), start + spacing * 3, y, 22, 22, beacon.getPotionIndex() == RESISTANCE.ordinal());
        resistance.initTextureValues(0, 111, 22, 22, BG);
        this.buttonList.add(resistance);

        regeneration = new GuiButtonToggle(REGENERATION.ordinal(), start + spacing * 4, y, 22, 22, beacon.getPotionIndex() == REGENERATION.ordinal());
        regeneration.initTextureValues(0, 111, 22, 22, BG);
        this.buttonList.add(regeneration);

        this.buttonList.add(new GuiButton(ADD_ONE.getIndex(), i + 16, j + 48, 62, 20, I18n.format("gui.touhou_little_maid.maid_beacon.add_one")));
        this.buttonList.add(new GuiButton(ADD_ALL.getIndex(), i + 80, j + 48, 62, 20, I18n.format("gui.touhou_little_maid.maid_beacon.add_all")));
        this.buttonList.add(new GuiButton(MIN_ONE.getIndex(), i + 16, j + 70, 62, 20, I18n.format("gui.touhou_little_maid.maid_beacon.min_one")));
        this.buttonList.add(new GuiButton(MIN_ALL.getIndex(), i + 80, j + 70, 62, 20, I18n.format("gui.touhou_little_maid.maid_beacon.min_all")));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        mc.getTextureManager().bindTexture(BG);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 105);

        PowerHandler powerHandler = Minecraft.getMinecraft().player.getCapability(CapabilityPowerHandler.POWER_CAP, null);
        if (powerHandler != null) {
            drawCenteredString(fontRenderer, I18n.format("gui.touhou_little_maid.maid_beacon.player_power",
                    DECIMAL_FORMAT.format(powerHandler.get())), guiLeft + 198, guiTop + 10 + 2, 0xffffff);
        }

        drawCenteredString(fontRenderer, I18n.format("gui.touhou_little_maid.maid_beacon.storage_power", DECIMAL_FORMAT.format(beacon.getStoragePower())), guiLeft + 198, guiTop + 22, 0xffffff);
        drawCenteredString(fontRenderer, I18n.format("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(beacon.getPotionIndex() == -1 ? 0 : COST * 900)), guiLeft + 198, guiTop + 32, 0xffffff);

        RenderHelper.drawCircle(guiLeft + 180, guiTop + 70, 20, 64, 0x22066b6d);
        RenderHelper.drawSector(guiLeft + 180, guiTop + 70, 22, 0, beacon.getStoragePower() / MAX_STORAGE * 2 * Math.PI, 64, 0xff40e4e5);
        drawCenteredString(fontRenderer, String.format("%.2f%%", beacon.getStoragePower() / TileEntityMaidBeacon.MAX_STORAGE * 100), guiLeft + 223, guiTop + 66, 0xffffff);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        PowerHandler powerHandler = Minecraft.getMinecraft().player.getCapability(CapabilityPowerHandler.POWER_CAP, null);
        if (powerHandler == null) {
            return;
        }
        if (button.id == speed.id) {
            CommonProxy.INSTANCE.sendToServer(new SetBeaconPotionMessage(mc.player.getUniqueID(), beacon.getPos(), speed.isStateTriggered() ? -1 : button.id));
            speed.setStateTriggered(!speed.isStateTriggered());
            if (speed.isStateTriggered()) {
                fireResistance.setStateTriggered(false);
                strength.setStateTriggered(false);
                resistance.setStateTriggered(false);
                regeneration.setStateTriggered(false);
            }
            return;
        }
        if (button.id == fireResistance.id) {
            CommonProxy.INSTANCE.sendToServer(new SetBeaconPotionMessage(mc.player.getUniqueID(), beacon.getPos(), fireResistance.isStateTriggered() ? -1 : button.id));
            fireResistance.setStateTriggered(!fireResistance.isStateTriggered());
            if (fireResistance.isStateTriggered()) {
                speed.setStateTriggered(false);
                strength.setStateTriggered(false);
                resistance.setStateTriggered(false);
                regeneration.setStateTriggered(false);
            }
            return;
        }
        if (button.id == strength.id) {
            CommonProxy.INSTANCE.sendToServer(new SetBeaconPotionMessage(mc.player.getUniqueID(), beacon.getPos(), strength.isStateTriggered() ? -1 : button.id));
            strength.setStateTriggered(!strength.isStateTriggered());
            if (strength.isStateTriggered()) {
                speed.setStateTriggered(false);
                fireResistance.setStateTriggered(false);
                resistance.setStateTriggered(false);
                regeneration.setStateTriggered(false);
            }
            return;
        }
        if (button.id == resistance.id) {
            CommonProxy.INSTANCE.sendToServer(new SetBeaconPotionMessage(mc.player.getUniqueID(), beacon.getPos(), resistance.isStateTriggered() ? -1 : button.id));
            resistance.setStateTriggered(!resistance.isStateTriggered());
            if (resistance.isStateTriggered()) {
                speed.setStateTriggered(false);
                fireResistance.setStateTriggered(false);
                strength.setStateTriggered(false);
                regeneration.setStateTriggered(false);
            }
            return;
        }
        if (button.id == regeneration.id) {
            CommonProxy.INSTANCE.sendToServer(new SetBeaconPotionMessage(mc.player.getUniqueID(), beacon.getPos(), regeneration.isStateTriggered() ? -1 : button.id));
            regeneration.setStateTriggered(!regeneration.isStateTriggered());
            if (regeneration.isStateTriggered()) {
                speed.setStateTriggered(false);
                fireResistance.setStateTriggered(false);
                strength.setStateTriggered(false);
                resistance.setStateTriggered(false);
            }
            return;
        }
        if (button.id == Button.ADD_ONE.getIndex()) {
            CommonProxy.INSTANCE.sendToServer(new StorageAndTakePowerMessage(mc.player.getUniqueID(), beacon.getPos(), 1, true));
            return;
        }
        if (button.id == Button.ADD_ALL.getIndex()) {
            CommonProxy.INSTANCE.sendToServer(new StorageAndTakePowerMessage(mc.player.getUniqueID(), beacon.getPos(), powerHandler.get(), true));
            return;
        }
        if (button.id == Button.MIN_ONE.getIndex()) {
            CommonProxy.INSTANCE.sendToServer(new StorageAndTakePowerMessage(mc.player.getUniqueID(), beacon.getPos(), 1, false));
            return;
        }
        if (button.id == Button.MIN_ALL.getIndex()) {
            CommonProxy.INSTANCE.sendToServer(new StorageAndTakePowerMessage(mc.player.getUniqueID(), beacon.getPos(), beacon.getStoragePower(), false));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int i = this.guiLeft;
        int j = this.guiTop;
        int start = i + 18;
        int spacing = 26;
        int y = j + 17;
        mc.renderEngine.bindTexture(GuiContainer.INVENTORY_BACKGROUND);
        drawTexturedModalRect(start, y, MobEffects.SPEED.getStatusIconIndex() % 8 * 18, 198 + MobEffects.SPEED.getStatusIconIndex() / 8 * 18, 18, 18);
        drawTexturedModalRect(start + spacing, y, MobEffects.FIRE_RESISTANCE.getStatusIconIndex() % 8 * 18, 198 + MobEffects.FIRE_RESISTANCE.getStatusIconIndex() / 8 * 18, 18, 18);
        drawTexturedModalRect(start + spacing * 2, y, MobEffects.STRENGTH.getStatusIconIndex() % 8 * 18, 198 + MobEffects.STRENGTH.getStatusIconIndex() / 8 * 18, 18, 18);
        drawTexturedModalRect(start + spacing * 3, y, MobEffects.RESISTANCE.getStatusIconIndex() % 8 * 18, 198 + MobEffects.RESISTANCE.getStatusIconIndex() / 8 * 18, 18, 18);
        drawTexturedModalRect(start + spacing * 4, y, MobEffects.REGENERATION.getStatusIconIndex() % 8 * 18, 198 + MobEffects.REGENERATION.getStatusIconIndex() / 8 * 18, 18, 18);
    }

    enum Button {
        // 存入取出经验按钮
        ADD_ONE,
        ADD_ALL,
        MIN_ONE,
        MIN_ALL;

        int getIndex() {
            return ordinal() + TileEntityMaidBeacon.Effect.values().length;
        }
    }
}
