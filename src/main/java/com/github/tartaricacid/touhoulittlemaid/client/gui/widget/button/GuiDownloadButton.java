package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class GuiDownloadButton extends Button {
    private final DownloadInfo info;

    public GuiDownloadButton(int pX, int pY, int pWidth, int pHeight, DownloadInfo info, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, Component.empty(), pOnPress, Button.DEFAULT_NARRATION);
        this.info = info;
        if (!DownloadStatus.canDownload(info.getStatus())) {
            this.active = false;
        }
    }

    @Override
    public Component getMessage() {
        MutableComponent text;
        if (info == null) {
            return Component.translatable("selectWorld.futureworld.error.title");
        }
        switch (info.getStatus()) {
            case DOWNLOADED -> text = Component.translatable("gui.touhou_little_maid.resources_download.downloaded");
            case DOWNLOADING -> text = Component.translatable("gui.touhou_little_maid.resources_download.downloading");
            case NEED_UPDATE -> text = Component.translatable("gui.touhou_little_maid.resources_download.need_update");
            default -> text = Component.translatable("gui.touhou_little_maid.resources_download.not_download");
        }
        return text;
    }
}
