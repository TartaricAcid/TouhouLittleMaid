package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        if (info.getStatus() == DownloadStatus.DOWNLOADED && info.hasType(DownloadInfo.TypeEnum.SOUND)) {
            Font font = Minecraft.getInstance().font;
            graphics.drawWordWrap(font, Component.translatable("gui.touhou_little_maid.resources_download.sound.downloaded.tip"),
                    this.getX() + 10, this.getY() + (this.height - 8) / 2 - this.height * 2, 200, 0xffbb1010);
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
