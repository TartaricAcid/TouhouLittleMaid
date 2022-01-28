package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Locale;

public class GuiDownloadButton extends Button {
    private final DownloadInfo info;

    public GuiDownloadButton(int x, int y, int width, int height, DownloadInfo info) {
        super(x, y, width, height, StringTextComponent.EMPTY, (b) -> {
        });
        this.info = info;
    }

    @Override
    public void onPress() {
        if (DownloadStatus.canDownload(info.getStatus())) {
            InfoGetManager.downloadResourcesPack(info);
        }
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(matrixStack, mouseX, mouseY, partialTicks);
        if (info.getStatus() == DownloadStatus.DOWNLOADED && info.hasType(DownloadInfo.TypeEnum.SOUND)) {
            FontRenderer font = Minecraft.getInstance().font;
            font.drawWordWrap(new TranslationTextComponent("gui.touhou_little_maid.resources_download.sound.downloaded.tip"),
                    this.x + 10, this.y + (this.height - 8) / 2 - this.height * 2, 200, 0xffbb1010);
        }
    }

    @Override
    protected int getYImage(boolean isHovered) {
        int i = 1;
        if (!this.active || !DownloadStatus.canDownload(info.getStatus())) {
            i = 0;
        } else if (isHovered) {
            i = 2;
        }
        return i;
    }

    @Override
    public ITextComponent getMessage() {
        return new TranslationTextComponent(String.format("gui.touhou_little_maid.resources_download.%s", info.getStatus().name().toLowerCase(Locale.US)));
    }
}
