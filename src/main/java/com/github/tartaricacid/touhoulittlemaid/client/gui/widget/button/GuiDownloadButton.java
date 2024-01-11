package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class GuiDownloadButton extends Button {
    private final DownloadInfo info;

    public GuiDownloadButton(int pX, int pY, int pWidth, int pHeight, DownloadInfo info, Button.IPressable pOnPress) {
        super(pX, pY, pWidth, pHeight, StringTextComponent.EMPTY, pOnPress);
        this.info = info;
        if (!DownloadStatus.canDownload(info.getStatus())) {
            this.active = false;
        }
    }

    @Override
    public ITextComponent getMessage() {
        TextComponent text;
        switch (info.getStatus()) {
            default:
                text = new TranslationTextComponent("gui.touhou_little_maid.resources_download.not_download");
                break;
            case DOWNLOADED:
                text = new TranslationTextComponent("gui.touhou_little_maid.resources_download.downloaded");
                break;
            case DOWNLOADING:
                text = new TranslationTextComponent("gui.touhou_little_maid.resources_download.downloading");
                break;
            case NEED_UPDATE:
                text = new TranslationTextComponent("gui.touhou_little_maid.resources_download.need_update");
                break;
        }
        return text;
    }
}
