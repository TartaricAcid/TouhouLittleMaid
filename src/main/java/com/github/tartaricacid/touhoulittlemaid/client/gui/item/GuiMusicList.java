package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.audio.music.NetEaseMusicList;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.PortableAudioMessageToServer;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.client.audio.music.MusicManger.MUSIC_LIST_GROUP;

/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public class GuiMusicList extends GuiScrollingList {
    private static final ResourceLocation PIC = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/netease_music.png");
    public static int MUSIC_INDEX;
    private final PortableAudioGui parent;
    private final NetEaseMusicList.PlayList playList;

    public GuiMusicList(PortableAudioGui parent) {
        super(parent.mc, parent.width - 101, parent.height - 40, 0, parent.height - 24,
                101, 12, parent.width, parent.height);
        this.parent = parent;
        this.playList = MUSIC_LIST_GROUP.get(Math.min(PortableAudioGui.LIST_INDEX, MUSIC_LIST_GROUP.size() - 1)).getPlayList();
        MUSIC_INDEX = Math.min(playList.getTracks().size() - 1, MUSIC_INDEX);
        setHeaderInfo(true, 130);
    }

    @Override
    protected int getSize() {
        return playList.getTrackCount();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        if (doubleClick) {
            int id = playList.getTracks().get(index).getId();
            CommonProxy.INSTANCE.sendToServer(new PortableAudioMessageToServer(parent.getAudio().getUniqueID(), id));
        } else {
            MUSIC_INDEX = index;
        }
    }

    @Override
    protected boolean isSelected(int index) {
        return index == MUSIC_INDEX;
    }

    @Override
    protected void drawHeader(int entryRight, int startY, Tessellator tess) {
        int startX = 101;
        Minecraft mc = parent.mc;
        FontRenderer fontRenderer = mc.fontRenderer;

        Gui.drawRect(0, 0, parent.width, parent.height, 0xff16181c);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        mc.renderEngine.bindTexture(PIC);
        Gui.drawModalRectWithCustomSizedTexture(startX + 10, startY + 10, 0, 0, 64, 64, 64, 64);
        String name = I18n.format("gui.touhou_little_maid.portable_audio.play_list.name", playList.getName());
        parent.drawString(fontRenderer, fontRenderer.trimStringToWidth(name, parent.width - startX - 85 - 80 - 3), startX + 85, startY + 12, -1);
        String creator = I18n.format("gui.touhou_little_maid.portable_audio.play_list.creator", playList.getCreator(), playList.getCreateTime());
        parent.drawString(fontRenderer, creator, startX + 85, startY + 26, 0x828385);

        String track = I18n.format("gui.touhou_little_maid.portable_audio.play_list.track_count");
        parent.drawString(fontRenderer, track, parent.width - 54 - fontRenderer.getStringWidth(track), startY + 12, 0x4e4e52);
        String play = I18n.format("gui.touhou_little_maid.portable_audio.play_list.play_count");
        parent.drawString(fontRenderer, play, parent.width - 14 - fontRenderer.getStringWidth(play), startY + 12, 0x4e4e52);
        Gui.drawRect(parent.width - 47, startY + 12, parent.width - 46, startY + 30, 0x994e4e52);
        String trackCount = String.valueOf(playList.getTrackCount());
        parent.drawString(fontRenderer, trackCount, parent.width - 54 - fontRenderer.getStringWidth(trackCount), startY + 22, 0x4e4e52);
        String playCount = String.valueOf(playList.getPlayCount());
        parent.drawString(fontRenderer, playCount, parent.width - 14 - fontRenderer.getStringWidth(playCount), startY + 22, 0x4e4e52);

        String tag = I18n.format("gui.touhou_little_maid.portable_audio.play_list.tags", playList.getTags());
        parent.drawString(fontRenderer, tag, startX + 85, startY + 38, -1);

        String desc = I18n.format("gui.touhou_little_maid.portable_audio.play_list.description", playList.getDescription());
        List<String> test = fontRenderer.listFormattedStringToWidth(trimStringNewline(desc), parent.width - startX - 97);
        int y = startY + 50;
        for (int i = 0; i < Math.min(test.size(), 7); i++) {
            fontRenderer.drawString(test.get(i), startX + 85, y, -1, false);
            y += fontRenderer.FONT_HEIGHT;
        }

        int cell = (parent.width - startX - 58) / 5;
        parent.drawString(fontRenderer, I18n.format("gui.touhou_little_maid.portable_audio.track.name"),
                startX + 25, startY + 115, 0x7e7c7e);
        parent.drawString(fontRenderer, I18n.format("gui.touhou_little_maid.portable_audio.track.artists"),
                startX + 25 + cell * 2, startY + 115, 0x7e7c7e);
        parent.drawString(fontRenderer, I18n.format("gui.touhou_little_maid.portable_audio.track.album"),
                startX + 25 + cell * 3, startY + 115, 0x7e7c7e);
        parent.drawString(fontRenderer, I18n.format("gui.touhou_little_maid.portable_audio.track.duration"),
                parent.width - 33, startY + 115, 0x7e7c7e);
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawScreen(int mouseX, int mouseY) {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        Gui.drawRect(0, parent.height - 52, 100, parent.height - 24, 0xff191b1f);
        if (MUSIC_INDEX < playList.getTrackCount()) {
            FontRenderer fontRenderer = parent.mc.fontRenderer;

            RenderHelper.enableGUIStandardItemLighting();
            parent.mc.getRenderItem().renderItemAndEffectIntoGUI(
                    new ItemStack(Blocks.STAINED_GLASS_PANE, 1, 0),
                    3, parent.height - 46);
            parent.mc.getRenderItem().renderItemAndEffectIntoGUI(
                    new ItemStack(Blocks.DOUBLE_PLANT, 1, 0),
                    3, parent.height - 46);
            RenderHelper.disableStandardItemLighting();

            NetEaseMusicList.Track track = playList.getTracks().get(MUSIC_INDEX);
            parent.drawString(fontRenderer, fontRenderer.trimStringToWidth(track.getName(), 70),
                    23, parent.height - 47, 0xdcdde4);
            parent.drawString(fontRenderer, fontRenderer.trimStringToWidth(track.getArtists(), 70),
                    23, parent.height - 37, 0x525354);
        }
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        if (slotIdx >= playList.getTrackCount()) {
            return;
        }
        Minecraft mc = parent.mc;
        FontRenderer fontRenderer = mc.fontRenderer;
        NetEaseMusicList.Track track = playList.getTracks().get(slotIdx);
        int startX = 101;
        if (track != null) {
            int textColor = 0x82837e;
            if (slotIdx % 2 != 0) {
                Gui.drawRect(0, slotTop - 2, parent.width, slotTop + slotHeight - 2, 0xff1a1c20);
            }
            if (isSelected(slotIdx)) {
                Gui.drawRect(0, slotTop - 2, parent.width, slotTop + slotHeight - 2, 0xff2c2e32);
                textColor = 0xdcdde4;
            }

            String index = slotIdx < 9 ? "0" : "";
            index += String.valueOf(slotIdx + 1);
            parent.drawString(fontRenderer, index, startX + 20 - fontRenderer.getStringWidth(index), slotTop, textColor);
            parent.drawString(fontRenderer, track.getDuration(), parent.width - 33, slotTop, textColor);

            int cell = (parent.width - startX - 58) / 5;
            parent.drawString(fontRenderer, fontRenderer.trimStringToWidth(track.getName(), cell * 2 - 5),
                    startX + 25, slotTop, 0xdcdde4);
            parent.drawString(fontRenderer, fontRenderer.trimStringToWidth(track.getArtists(), cell - 5),
                    startX + 25 + cell * 2, slotTop, textColor);

            parent.drawString(fontRenderer, fontRenderer.trimStringToWidth(track.getAlbum(), cell * 2 - 5),
                    startX + 25 + cell * 3, slotTop, textColor);
        }
    }

    private String trimStringNewline(String text) {
        while (text != null && text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }
        return text;
    }
}
