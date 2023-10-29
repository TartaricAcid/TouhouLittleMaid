package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.Locale;

public class GuiDownloadButton extends Button {
    private final DownloadInfo info;

    public GuiDownloadButton(int x, int y, int width, int height, DownloadInfo info) {
        super(Button.builder(Component.empty(), (b) -> {
        }).pos(x, y).size(width, height));
        this.info = info;
    }

    @Override
    public void onPress() {
        if (DownloadStatus.canDownload(info.getStatus())) {
            InfoGetManager.downloadResourcesPack(info);
        }
    }

    @Override
    public int getTextureY() {
        int i = 1;
        if (!this.active || !DownloadStatus.canDownload(info.getStatus())) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }
        return 46 + i * 20;
    }

    @Override
    public Component getMessage() {
        return Component.translatable(String.format("gui.touhou_little_maid.resources_download.%s", info.getStatus().name().toLowerCase(Locale.US)));
    }
}
