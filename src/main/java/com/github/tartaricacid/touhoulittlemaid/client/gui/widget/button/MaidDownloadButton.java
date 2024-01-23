package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.ModelDownloadGui;
import com.github.tartaricacid.touhoulittlemaid.util.TipsHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class MaidDownloadButton extends ImageButton {
    public MaidDownloadButton(int pX, int pY, ResourceLocation texture) {
        super(pX, pY, 41, 20, 0, 86, 20, texture, (b) -> {
            InfoGetManager.STATUE = InfoGetManager.Statue.NOT_UPDATE;
            Minecraft.getInstance().setScreen(new ModelDownloadGui());
        });
    }

    public void renderExtraTips(PoseStack poseStack) {
        TipsHelper.renderTips(poseStack, this, getText());
    }

    private Component getText() {
        if (InfoGetManager.STATUE == InfoGetManager.Statue.FIRST) {
            return new TranslatableComponent("gui.touhou_little_maid.button.model_download.statue.first");
        }
        if (InfoGetManager.STATUE == InfoGetManager.Statue.UPDATE) {
            return new TranslatableComponent("gui.touhou_little_maid.button.model_download.statue.update");
        }
        return TextComponent.EMPTY;
    }
}
