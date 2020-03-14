package com.github.tartaricacid.touhoulittlemaid.client.gui.sound;

import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.SoundPackInfo;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.client.GuiScrollingList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiInfoList extends GuiScrollingList {
    private GuiMaidSound parent;

    public GuiInfoList(GuiMaidSound parent) {
        super(parent.mc, parent.width - 210, parent.height, 20, parent.height - 8,
                8, 90, parent.width, parent.height);
        this.parent = parent;
        setHeaderInfo(true, 16);
    }

    @Override
    protected int getSize() {
        return CustomResourcesLoader.SOUND_INFO.getInfoList().size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        this.parent.setInfoIndex(index);
        if (doubleClick) {
            SoundPackInfo info = CustomResourcesLoader.SOUND_INFO.getInfoList().get(index);
            // 绘制 URL
            if (StringUtils.isNotBlank(info.getUrl())) {
                ITextComponent urlText = ForgeHooks.newChatWithLinks(info.getUrl());
                parent.handleComponentClick(urlText);
            }
        }
    }

    @Override
    protected boolean isSelected(int index) {
        return parent.getInfoIndex() == index;
    }

    @Override
    protected void drawBackground() {
    }

    @Override
    protected void drawHeader(int entryRight, int relativeY, Tessellator tess) {
        parent.drawCenteredString(parent.mc.fontRenderer, I18n.format("gui.touhou_little_maid.sound_info.url.desc"),
                8 + (parent.width - 210) / 2, relativeY + 2, 0xff888888);
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        SoundPackInfo info = CustomResourcesLoader.SOUND_INFO.getInfoList().get(slotIdx);
        int middleX = 14;
        int middleY = slotTop + 4;
        if (info != null) {
            int offSet = 0;
            FontRenderer fontRenderer = parent.mc.fontRenderer;

            // 绘制包名
            parent.drawString(fontRenderer, ParseI18n.parse(info.getPackName()), middleX, middleY + offSet, 0xffffff);


            // 绘制作者列表
            if (!info.getAuthor().isEmpty()) {
                for (List<String> textList : Lists.partition(info.getAuthor(), 2)) {
                    offSet += 10;
                    parent.drawString(fontRenderer, TextFormatting.GOLD + textList.toString(),
                            middleX, middleY + offSet, 0xffffff);
                }
            }

            // 绘制版本信息
            if (StringUtils.isNotBlank(info.getVersion())) {
                offSet += 10;
                parent.drawString(fontRenderer, TextFormatting.DARK_AQUA + I18n.format("gui.touhou_little_maid.skin.text.version", info.getVersion()),
                        middleX, middleY + offSet, 0xffffff);
            }

            // 绘制日期信息
            if (StringUtils.isNotBlank(info.getDate())) {
                offSet += 10;
                parent.drawString(fontRenderer, TextFormatting.GREEN + I18n.format("gui.touhou_little_maid.skin.text.date", info.getDate()),
                        middleX, middleY + offSet, 0xffffff);
            }

            offSet += 15;
            // 如果描述不为空，逐行绘制描述
            fontRenderer.drawSplitString(TextFormatting.GRAY + ParseI18n.parse(info.getDescription()),
                    middleX, middleY + offSet, parent.width - 228, 0xffffff);
        }
    }
}
