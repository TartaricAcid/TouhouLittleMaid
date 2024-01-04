package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class PackInfoButton extends FlatColorButton {
    private final DownloadInfo info;

    public PackInfoButton(int pX, int pY, DownloadInfo info, OnPress onPress) {
        super(pX, pY, 268, 46, Component.empty(), onPress);
        this.info = info;
        this.setTooltips(Lists.newArrayList(getI18nFormatDesc(info.getDesc()), getI18nFormatLicense(info.getLicense())));
    }

    @Override
    public void renderString(GuiGraphics graphics, Font font, int pColor) {
        graphics.fillGradient(this.getX() + 4, this.getY() + 4, this.getX() + 42, this.getY() + 42, 0xFFFFFFFF, 0xFFFFFFFF);

        int startX = this.getX() + 50;
        int startY = this.getY() + 4;

        MutableComponent packName = Component.translatable(info.getName());
        graphics.drawString(font, packName, startX, startY, ChatFormatting.WHITE.getColor());
        graphics.drawString(font, getI18nFormatFileVersion(info.getVersion()), startX + 5 + font.width(packName), startY, ChatFormatting.GREEN.getColor());
        graphics.drawString(font, getI18nFormatFileSize(info.getFormatFileSize()), startX, startY + 10, ChatFormatting.GOLD.getColor());
        graphics.drawString(font, getI18nFormatAuthor(info.getAuthor()), startX, startY + 20, ChatFormatting.AQUA.getColor());
        graphics.drawString(font, getI18nFormatFileTime(info.getFormatData()), startX, startY + 30, ChatFormatting.GRAY.getColor());
    }

    private String getI18nFormatFileVersion(String version) {
        return "§nv" + version;
    }

    private MutableComponent getI18nFormatAuthor(List<String> authors) {
        String str = String.join(I18n.get("gui.touhou_little_maid.resources_download.author.delimiter"), authors);
        return Component.translatable("gui.touhou_little_maid.resources_download.author", str);
    }

    private MutableComponent getI18nFormatFileSize(String size) {
        return Component.translatable("gui.touhou_little_maid.resources_download.file_size", size);
    }

    private MutableComponent getI18nFormatFileTime(String time) {
        return Component.translatable("gui.touhou_little_maid.resources_download.upload_time", time);
    }

    private MutableComponent getI18nFormatLicense(String license) {
        return Component.translatable("gui.touhou_little_maid.resources_download.license", license)
                .withStyle(ChatFormatting.DARK_PURPLE).withStyle(ChatFormatting.ITALIC);
    }

    private MutableComponent getI18nFormatDesc(String desc) {
        return Component.translatable(desc).withStyle(ChatFormatting.GRAY);
    }
}
