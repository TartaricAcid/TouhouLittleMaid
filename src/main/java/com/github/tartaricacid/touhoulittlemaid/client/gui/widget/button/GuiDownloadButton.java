package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class GuiDownloadButton extends Button {
    private final DownloadInfo info;

    public GuiDownloadButton(int pX, int pY, int pWidth, int pHeight, DownloadInfo info, OnPress pOnPress) {
        super(pX, pY, pWidth, pHeight, TextComponent.EMPTY, pOnPress);
        this.info = info;
        if (!DownloadStatus.canDownload(info.getStatus())) {
            this.active = false;
        }
    }

    @Override
    public Component getMessage() {
        MutableComponent text;
        if (info == null) {
            return new TranslatableComponent("selectWorld.futureworld.error.title");
        }
        switch (info.getStatus()) {
            default -> text = new TranslatableComponent("gui.touhou_little_maid.resources_download.not_download");
            case DOWNLOADED -> text = new TranslatableComponent("gui.touhou_little_maid.resources_download.downloaded");
            case DOWNLOADING -> text = new TranslatableComponent("gui.touhou_little_maid.resources_download.downloading");
            case NEED_UPDATE -> text = new TranslatableComponent("gui.touhou_little_maid.resources_download.need_update");
        }
        return text;
    }
}
