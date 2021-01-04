package com.github.tartaricacid.touhoulittlemaid.client.gui.block;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerSerializer;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidBeaconContainer;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SetBeaconOverflowMessage;
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
import net.minecraft.util.text.TextFormatting;

import java.text.DecimalFormat;

import static com.github.tartaricacid.touhoulittlemaid.client.gui.block.MaidBeaconGuiContainer.Button.*;
import static com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon.Effect.*;

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
    private GuiButton overflowDeleteButton;
    private boolean overflowDelete;

    public MaidBeaconGuiContainer(MaidBeaconContainer maidBeaconContainer) {
        super(maidBeaconContainer);
        this.xSize = 256;
        this.ySize = 105;
        this.beacon = maidBeaconContainer.getTileEntityMaidBeacon();
        this.overflowDelete = beacon.isOverflowDelete();
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
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

        overflowDeleteButton = new GuiButton(-1, i + 6, j - 22, 145, 20, getOverflowDeleteButtonText());
        addButton(overflowDeleteButton);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        mc.getTextureManager().bindTexture(BG);
        this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, 105);

        PowerHandler powerHandler = Minecraft.getMinecraft().player.getCapability(PowerSerializer.POWER_CAP, null);
        if (powerHandler != null) {
            drawCenteredString(fontRenderer, I18n.format("gui.touhou_little_maid.maid_beacon.player_power",
                    DECIMAL_FORMAT.format(powerHandler.get())), guiLeft + 198, guiTop + 10 + 2, 0xffffff);
        }

        drawCenteredString(fontRenderer, I18n.format("gui.touhou_little_maid.maid_beacon.storage_power", DECIMAL_FORMAT.format(beacon.getStoragePower())), guiLeft + 198, guiTop + 22, 0xffffff);
        if (beacon.getPotionIndex() == -1) {
            drawCenteredString(fontRenderer, I18n.format("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(0)), guiLeft + 198, guiTop + 32, 0xffffff);
        } else {
            drawCenteredString(fontRenderer, TextFormatting.RED + I18n.format("gui.touhou_little_maid.maid_beacon.cost_power", DECIMAL_FORMAT.format(beacon.getEffectCost() * 900)), guiLeft + 198, guiTop + 32, 0xffffff);
        }
        RenderHelper.drawCircle(guiLeft + 180, guiTop + 70, 20, 64, 0x22066b6d);
        RenderHelper.drawSector(guiLeft + 180, guiTop + 70, 22, 0, beacon.getStoragePower() / beacon.getMaxStorage() * 2 * Math.PI, 64, 0xff40e4e5);
        drawCenteredString(fontRenderer, String.format("%.2f%%", beacon.getStoragePower() / beacon.getMaxStorage() * 100), guiLeft + 223, guiTop + 66, 0xffffff);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        PowerHandler powerHandler = Minecraft.getMinecraft().player.getCapability(PowerSerializer.POWER_CAP, null);
        if (powerHandler == null) {
            return;
        }
        if (button.id == speed.id) {
            CommonProxy.INSTANCE.sendToServer(new SetBeaconPotionMessage(beacon.getPos(), speed.isStateTriggered() ? -1 : button.id));
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
            CommonProxy.INSTANCE.sendToServer(new SetBeaconPotionMessage(beacon.getPos(), fireResistance.isStateTriggered() ? -1 : button.id));
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
            CommonProxy.INSTANCE.sendToServer(new SetBeaconPotionMessage(beacon.getPos(), strength.isStateTriggered() ? -1 : button.id));
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
            CommonProxy.INSTANCE.sendToServer(new SetBeaconPotionMessage(beacon.getPos(), resistance.isStateTriggered() ? -1 : button.id));
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
            CommonProxy.INSTANCE.sendToServer(new SetBeaconPotionMessage(beacon.getPos(), regeneration.isStateTriggered() ? -1 : button.id));
            regeneration.setStateTriggered(!regeneration.isStateTriggered());
            if (regeneration.isStateTriggered()) {
                speed.setStateTriggered(false);
                fireResistance.setStateTriggered(false);
                strength.setStateTriggered(false);
                resistance.setStateTriggered(false);
            }
            return;
        }
        if (button.id == -1) {
            overflowDelete = !overflowDelete;
            CommonProxy.INSTANCE.sendToServer(new SetBeaconOverflowMessage(beacon.getPos(), overflowDelete));
            overflowDeleteButton.displayString = getOverflowDeleteButtonText();
            return;
        }
        if (button.id == Button.ADD_ONE.getIndex()) {
            CommonProxy.INSTANCE.sendToServer(new StorageAndTakePowerMessage(beacon.getPos(), 1, true));
            return;
        }
        if (button.id == Button.ADD_ALL.getIndex()) {
            CommonProxy.INSTANCE.sendToServer(new StorageAndTakePowerMessage(beacon.getPos(), powerHandler.get(), true));
            return;
        }
        if (button.id == Button.MIN_ONE.getIndex()) {
            CommonProxy.INSTANCE.sendToServer(new StorageAndTakePowerMessage(beacon.getPos(), 1, false));
            return;
        }
        if (button.id == Button.MIN_ALL.getIndex()) {
            CommonProxy.INSTANCE.sendToServer(new StorageAndTakePowerMessage(beacon.getPos(), beacon.getStoragePower(), false));
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

    private String getOverflowDeleteButtonText() {
        String delete = I18n.format("gui.touhou_little_maid.maid_beacon.overflow_delete_true");
        String keep = I18n.format("gui.touhou_little_maid.maid_beacon.overflow_delete_false");
        return overflowDelete ? delete : keep;
    }

    enum Button {
        // 存入取出经验按钮
        ADD_ONE,
        ADD_ALL,
        MIN_ONE,
        MIN_ALL;

        int getIndex() {
            return ordinal() + TileEntityMaidBeacon.Effect.VALUES.length;
        }
    }
}
