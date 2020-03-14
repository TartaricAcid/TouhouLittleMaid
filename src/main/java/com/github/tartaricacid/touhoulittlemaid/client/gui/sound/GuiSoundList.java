package com.github.tartaricacid.touhoulittlemaid.client.gui.sound;

import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiSoundList extends GuiScrollingList {
    private GuiMaidSound parent;

    public GuiSoundList(GuiMaidSound parent) {
        super(parent.mc, 185, parent.height, 20, parent.height - 8,
                parent.width - 192, 12, parent.width, parent.height);
        this.parent = parent;
        setHeaderInfo(true, 28);
    }

    @Override
    protected int getSize() {
        return MaidSoundEvent.getSoundList().size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        this.parent.setSoundIndex(index);
        if (doubleClick) {
            this.parent.playSound(index);
        }
    }

    @Override
    protected boolean isSelected(int index) {
        return parent.getSoundIndex() == index;
    }

    @Override
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
        parent.drawCenteredString(parent.mc.fontRenderer, I18n.format("gui.touhou_little_maid.sound_info.play.desc"),
                parent.width - 192 + 185 / 2, relativeY + 2, 0xff888888);
        parent.drawString(parent.mc.fontRenderer, I18n.format("gui.touhou_little_maid.sound_info.sound.name"),
                parent.width - 201 + 15, relativeY + 14,
                0xffffffff);
        String str = I18n.format("gui.touhou_little_maid.sound_info.sound.count");
        parent.drawString(parent.mc.fontRenderer, str,
                parent.width - 198 + 160 - parent.mc.fontRenderer.getStringWidth(str) / 2, relativeY + 14,
                0xffffffff);
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        SoundEventAccessor accessor = parent.mc.getSoundHandler().getAccessor(MaidSoundEvent.getSoundList().get(slotIdx).getSoundName());
        if (accessor == null) {
            return;
        }
        ITextComponent iTextComponent = accessor.getSubtitle();
        if (iTextComponent != null) {
            parent.drawString(parent.mc.fontRenderer, iTextComponent.getFormattedText(), parent.width - 201 + 15, slotTop,
                    0xfff00fff);
            parent.drawString(parent.mc.fontRenderer, accessor.accessorList.size() + "", parent.width - 198 + 160, slotTop,
                    0xff00ffff);
        }
    }
}
