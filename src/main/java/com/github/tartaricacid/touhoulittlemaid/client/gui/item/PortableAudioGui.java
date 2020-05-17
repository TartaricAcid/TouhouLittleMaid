package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.audio.music.MusicJsonInfo;
import com.github.tartaricacid.touhoulittlemaid.client.audio.music.NetEaseMusicList;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPortableAudio;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.PortableAudioMessageToServer;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiButtonImage;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import static com.github.tartaricacid.touhoulittlemaid.client.audio.music.MusicManger.MUSIC_LIST_GROUP;
import static com.github.tartaricacid.touhoulittlemaid.client.gui.item.GuiMusicList.MUSIC_INDEX;

/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public class PortableAudioGui extends GuiScreen {
    private static final ResourceLocation ICON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/netease_music_icon.png");
    protected static int LIST_INDEX;
    private final EntityPortableAudio audio;
    private final boolean isMusicListEmpty;
    protected GuiMusicList guiMusicList;
    private GuiMusicListGroup guiMusicListGroup;
    private GuiTextField songField;
    private String promptMsg = "";

    public PortableAudioGui(EntityPortableAudio audio) {
        this.audio = audio;
        this.isMusicListEmpty = MUSIC_LIST_GROUP.isEmpty();
    }

    @Override
    public void initGui() {
        if (isMusicListEmpty) {
            return;
        }
        Keyboard.enableRepeatEvents(true);
        buttonList.clear();
        songField = new GuiTextField(0, mc.fontRenderer, width - 86, height - 19, 80, 14) {
            @Override
            public boolean getEnableBackgroundDrawing() {
                return false;
            }
        };
        songField.setText(String.valueOf(audio.getSongId()));
        songField.setMaxStringLength(10);
        this.guiMusicList = new GuiMusicList(this);
        this.guiMusicListGroup = new GuiMusicListGroup(this);
        this.buttonList.add(new GuiButtonImage(1, 16, height - 19, 16, 16,
                32, 0, 16, ICON));
        this.buttonList.add(new GuiButtonImage(2, 41, height - 19, 16, 16,
                0, 0, 16, ICON));
        this.buttonList.add(new GuiButtonImage(3, 66, height - 19, 16, 16,
                16, 0, 16, ICON));
        this.buttonList.add(new GuiButtonImage(4, width - 105, height - 20, 16, 16,
                48, 0, 16, ICON));
    }

    @Override
    public void updateScreen() {
        if (isMusicListEmpty) {
            return;
        }
        songField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (isMusicListEmpty) {
            this.drawGradientRect(0, 0, this.width, this.height, 0xef101010, 0xef101010);
            mc.fontRenderer.drawSplitString(I18n.format("gui.touhou_little_maid.portable_audio.play_list.empty")
                            .replace("\\n", "\n"),
                    (width - 300) / 2, (height - 170) / 2, 300, 0xff1111);
            super.drawScreen(mouseX, mouseY, partialTicks);
            return;
        }
        this.drawGradientRect(0, 0, this.width, this.height, 0xff23262c, 0xff23262c);
        guiMusicList.drawScreen(mouseX, mouseY, partialTicks);
        guiMusicListGroup.drawScreen(mouseX, mouseY, partialTicks);
        this.drawGradientRect(0, this.height - 23, this.width, this.height, 0xff212124, 0xff212124);
        this.drawGradientRect(width - 86, height - 19, width - 6, height - 5, 0xff161618, 0xff161618);
        songField.drawTextBox();
        drawString(mc.fontRenderer, promptMsg,
                width - 110 - mc.fontRenderer.getStringWidth(promptMsg), height - 17, 0xbe3a3a);
        MusicJsonInfo info = MUSIC_LIST_GROUP.get(LIST_INDEX).getMusicJsonInfo();
        drawString(mc.fontRenderer, I18n.format("gui.touhou_little_maid.portable_audio.play_list.importer", info.getCreator()),
                105, height - 16, 0x7e7c7e);
        GlStateManager.color(1, 1, 1);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (isMusicListEmpty) {
            return;
        }
        if (button.id == 1) {
            if (LIST_INDEX < MUSIC_LIST_GROUP.size()) {
                NetEaseMusicList.PlayList playList = MUSIC_LIST_GROUP.get(LIST_INDEX).getPlayList();
                if ((MUSIC_INDEX - 1) < 0) {
                    MUSIC_INDEX = playList.getTrackCount() - 1;
                } else {
                    MUSIC_INDEX -= 1;
                }
                CommonProxy.INSTANCE.sendToServer(new PortableAudioMessageToServer(audio.getUniqueID(),
                        playList.getTracks().get(MUSIC_INDEX).getId()));
            }
            return;
        }

        if (button.id == 2) {
            CommonProxy.INSTANCE.sendToServer(new PortableAudioMessageToServer(audio.getUniqueID(), -1));
            return;
        }

        if (button.id == 3) {
            if (LIST_INDEX < MUSIC_LIST_GROUP.size()) {
                NetEaseMusicList.PlayList playList = MUSIC_LIST_GROUP.get(LIST_INDEX).getPlayList();
                if ((MUSIC_INDEX + 1) < playList.getTrackCount()) {
                    MUSIC_INDEX = MUSIC_INDEX + 1;
                } else {
                    MUSIC_INDEX = 0;
                }
                CommonProxy.INSTANCE.sendToServer(new PortableAudioMessageToServer(audio.getUniqueID(),
                        playList.getTracks().get(MUSIC_INDEX).getId()));
            }
            return;
        }

        if (button.id == 4) {
            if (StringUtils.isNotBlank(songField.getText())) {
                try {
                    int id = Integer.parseUnsignedInt(songField.getText());
                    CommonProxy.INSTANCE.sendToServer(new PortableAudioMessageToServer(audio.getUniqueID(), id));
                } catch (NumberFormatException ignore) {
                    promptMsg = I18n.format("gui.touhou_little_maid.portable_audio.song_id.illegal");
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (isMusicListEmpty) {
            return;
        }
        songField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (isMusicListEmpty) {
            return;
        }
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        guiMusicList.handleMouseInput(mouseX, mouseY);
        guiMusicListGroup.handleMouseInput(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char c, int keyCode) throws IOException {
        super.keyTyped(c, keyCode);
        if (isMusicListEmpty) {
            return;
        }
        if (Character.isDigit(c) || keyCode == Keyboard.KEY_BACK || keyCode == Keyboard.KEY_DELETE
                || keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT
                || GuiScreen.isKeyComboCtrlA(keyCode) || GuiScreen.isKeyComboCtrlC(keyCode)
                || GuiScreen.isKeyComboCtrlV(keyCode) || GuiScreen.isKeyComboCtrlX(keyCode)) {
            songField.textboxKeyTyped(c, keyCode);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public EntityPortableAudio getAudio() {
        return audio;
    }
}
