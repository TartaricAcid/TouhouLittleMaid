package com.github.tartaricacid.touhoulittlemaid.client.gui.sound;

import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiMaidSound extends GuiScreen {
    private GuiSoundList soundList;
    private GuiInfoList infoList;
    private int soundIndex;
    private int infoIndex;

    @Override
    public void initGui() {
        soundList = new GuiSoundList(this);
        infoList = new GuiInfoList(this);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        this.soundList.drawScreen(mouseX, mouseY, partialTicks);
        this.infoList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(mc.fontRenderer, I18n.format("gui.touhou_little_maid.sound_info.title"), width / 2, 5, 0xffffffff);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void handleMouseInput() throws IOException {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        soundList.handleMouseInput(mouseX, mouseY);
        infoList.handleMouseInput(mouseX, mouseY);
        super.handleMouseInput();
    }

    public void setSoundIndex(int index) {
        this.soundIndex = index;
    }

    public int getSoundIndex() {
        return soundIndex;
    }

    public int getInfoIndex() {
        return infoIndex;
    }

    public void setInfoIndex(int infoIndex) {
        this.infoIndex = infoIndex;
    }

    public void playSound(int index) {
        mc.getSoundHandler().stopSounds();
        mc.player.playSound(MaidSoundEvent.getSoundList().get(index), 1.0f, 1.0f);
    }
}
