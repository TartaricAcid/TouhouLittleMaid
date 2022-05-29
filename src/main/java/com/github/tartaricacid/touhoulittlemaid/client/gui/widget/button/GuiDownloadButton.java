package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadInfo;
import com.github.tartaricacid.touhoulittlemaid.client.download.pojo.DownloadStatus;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.Locale;

public class GuiDownloadButton extends Button {
    private final DownloadInfo info;

    public GuiDownloadButton(int x, int y, int width, int height, DownloadInfo info) {
        super(x, y, width, height, TextComponent.EMPTY, (b) -> {
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
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(poseStack, mouseX, mouseY, partialTicks);
        if (info.getStatus() == DownloadStatus.DOWNLOADED && info.hasType(DownloadInfo.TypeEnum.SOUND)) {
            Font font = Minecraft.getInstance().font;
            font.drawWordWrap(new TranslatableComponent("gui.touhou_little_maid.resources_download.sound.downloaded.tip"),
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
    public Component getMessage() {
        return new TranslatableComponent(String.format("gui.touhou_little_maid.resources_download.%s", info.getStatus().name().toLowerCase(Locale.US)));
    }
}
