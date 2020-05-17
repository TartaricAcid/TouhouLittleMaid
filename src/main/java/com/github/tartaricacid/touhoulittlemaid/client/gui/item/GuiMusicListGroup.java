package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.client.audio.music.MusicManger;
import com.github.tartaricacid.touhoulittlemaid.client.audio.music.NetEaseMusicList;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.github.tartaricacid.touhoulittlemaid.client.audio.music.MusicManger.MUSIC_LIST_GROUP;

/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public class GuiMusicListGroup extends GuiScrollingList {
    private final PortableAudioGui parent;

    public GuiMusicListGroup(PortableAudioGui parent) {
        super(parent.mc, 100, parent.height - 53, 0, parent.height - 53,
                0, 16, parent.width, parent.height);
        this.parent = parent;
        setHeaderInfo(true, 15);
        PortableAudioGui.LIST_INDEX = Math.min(MUSIC_LIST_GROUP.size() - 1, PortableAudioGui.LIST_INDEX);
    }

    @Override
    protected int getSize() {
        return MUSIC_LIST_GROUP.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        if (!doubleClick) {
            PortableAudioGui.LIST_INDEX = index;
            parent.guiMusicList = new GuiMusicList(parent);
        }
    }

    @Override
    protected boolean isSelected(int index) {
        return index == PortableAudioGui.LIST_INDEX;
    }

    @Override
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
        Gui.drawRect(0, 0, 100, parent.height - 24, 0xff191b1f);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        String loadInfo = I18n.format("gui.touhou_little_maid.portable_audio.play_list.loading_situation",
                MusicManger.getCompletedTaskCount(), MusicManger.getTaskCount());
        parent.drawString(parent.mc.fontRenderer, loadInfo, 6, relativeY, 0x7e7c7e);
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        NetEaseMusicList neteaseMusicList = MUSIC_LIST_GROUP.get(slotIdx);
        if (isSelected(slotIdx)) {
            Gui.drawRect(0, slotTop - 2, parent.width, slotTop + slotHeight - 2, 0xff26282c);
            Gui.drawRect(0, slotTop - 2, 2, slotTop + slotHeight - 2, 0xffb82525);
        }
        parent.drawString(parent.mc.fontRenderer,
                parent.mc.fontRenderer.trimStringToWidth(neteaseMusicList.getPlayList().getName(), 86),
                6, slotTop + 2, 0xdcdde3);
    }
}
