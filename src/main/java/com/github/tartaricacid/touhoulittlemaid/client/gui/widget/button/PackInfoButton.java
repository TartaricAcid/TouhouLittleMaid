package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class PackInfoButton extends FlatColorButton {
    private static final ResourceLocation BG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/download_background.png");
    private final DownloadInfo info;

    public PackInfoButton(int pX, int pY, DownloadInfo info, Button.IPressable onPress) {
        super(pX, pY, 268, 46, StringTextComponent.EMPTY, onPress);
        this.info = info;
        this.setTooltips(Lists.newArrayList(getI18nFormatDesc(info.getDesc()), getI18nFormatLicense(info.getLicense())));
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float pPartialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bind(BG);
        if (isSelect) {
            fillGradient(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, 0xff_1E90FF, 0xff_1E90FF);
        } else if (info.getStatus() != DownloadStatus.DOWNLOADED) {
            fillGradient(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, 0xff_434242, 0xff_434242);
        }
        if (info.getStatus() == DownloadStatus.NEED_UPDATE) {
            minecraft.getTextureManager().bind(BG);
            blit(matrixStack, this.x + 240, this.y + 15, 48, 16, 16, 16);
        }
        if (this.isHovered()) {
            fillGradient(matrixStack, this.x, this.y + 1, this.x + 1, this.y + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            fillGradient(matrixStack, this.x, this.y, this.x + this.width, this.y + 1, 0xff_F3EFE0, 0xff_F3EFE0);
            fillGradient(matrixStack, this.x + this.width - 1, this.y + 1, this.x + this.width, this.y + this.height - 1, 0xff_F3EFE0, 0xff_F3EFE0);
            fillGradient(matrixStack, this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, 0xff_F3EFE0, 0xff_F3EFE0);
        }
        int count = info.getTypeCount();
        if (count == 3) {
            blit(matrixStack, this.x + 7, this.y + 7, 0, 96, 32, 32);
        } else if (count == 2) {
            if (!info.hasType(DownloadInfo.TypeEnum.MAID)) {
                blit(matrixStack, this.x + 7, this.y + 7, 64, 64, 32, 32);
            } else if (!info.hasType(DownloadInfo.TypeEnum.CHAIR)) {
                blit(matrixStack, this.x + 7, this.y + 7, 32, 64, 32, 32);
            } else {
                blit(matrixStack, this.x + 7, this.y + 7, 0, 64, 32, 32);
            }
        } else {
            if (info.hasType(DownloadInfo.TypeEnum.MAID)) {
                blit(matrixStack, this.x + 7, this.y + 7, 0, 32, 32, 32);
            } else if (info.hasType(DownloadInfo.TypeEnum.CHAIR)) {
                blit(matrixStack, this.x + 7, this.y + 7, 32, 32, 32, 32);
            } else {
                blit(matrixStack, this.x + 7, this.y + 7, 64, 32, 32, 32);
            }
        }
        int i = getFGColor();
        this.renderString(matrixStack, minecraft.font, i | MathHelper.ceil(this.alpha * 255.0F) << 24);
        if (info.getStatus() == DownloadStatus.DOWNLOADED) {
            fillGradient(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, 0x7f_232222, 0x7f_232222);
        }
    }

    @Override
    public void renderString(MatrixStack matrixStack, FontRenderer font, int pColor) {
        int startX = this.x + 50;
        int startY = this.y + 4;

        TranslationTextComponent packName = new TranslationTextComponent(info.getName());
        drawString(matrixStack, font, packName, startX, startY, TextFormatting.WHITE.getColor());
        drawString(matrixStack, font, getI18nFormatFileVersion(info.getVersion()), startX + 5 + font.width(packName), startY, TextFormatting.GREEN.getColor());
        drawString(matrixStack, font, getI18nFormatFileSize(info.getFormatFileSize()), startX, startY + 10, TextFormatting.GOLD.getColor());
        drawString(matrixStack, font, getI18nFormatAuthor(info.getAuthor()), startX, startY + 20, TextFormatting.AQUA.getColor());
        drawString(matrixStack, font, getI18nFormatFileTime(info.getFormatData()), startX, startY + 30, TextFormatting.GRAY.getColor());
    }

    private String getI18nFormatFileVersion(String version) {
        return "§nv" + version;
    }

    private TranslationTextComponent getI18nFormatAuthor(List<String> authors) {
        String str = String.join(I18n.get("gui.touhou_little_maid.resources_download.author.delimiter"), authors);
        return new TranslationTextComponent("gui.touhou_little_maid.resources_download.author", str);
    }

    private TranslationTextComponent getI18nFormatFileSize(String size) {
        return new TranslationTextComponent("gui.touhou_little_maid.resources_download.file_size", size);
    }

    private TranslationTextComponent getI18nFormatFileTime(String time) {
        return new TranslationTextComponent("gui.touhou_little_maid.resources_download.upload_time", time);
    }

    private IFormattableTextComponent getI18nFormatLicense(String license) {
        return new TranslationTextComponent("gui.touhou_little_maid.resources_download.license", license).withStyle(TextFormatting.DARK_PURPLE).withStyle(TextFormatting.ITALIC);
    }

    private IFormattableTextComponent getI18nFormatDesc(String desc) {
        return new TranslationTextComponent(desc).withStyle(TextFormatting.GRAY);
    }
}
