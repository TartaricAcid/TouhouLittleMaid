package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.ModelDownloadGui;
import com.github.tartaricacid.touhoulittlemaid.util.TipsHelper;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class MaidDownloadButton extends ImageButton {
    public MaidDownloadButton(int pX, int pY, ResourceLocation texture) {
        super(pX, pY, 41, 20, 0, 86, 20, texture, (b) -> {
            InfoGetManager.STATUE = InfoGetManager.Statue.NOT_UPDATE;
            Minecraft.getInstance().setScreen(new ModelDownloadGui());
        });
    }

    public void renderExtraTips(MatrixStack matrixStack) {
        TipsHelper.renderTips(matrixStack, this, getText());
    }

    private ITextComponent getText() {
        if (InfoGetManager.STATUE == InfoGetManager.Statue.FIRST) {
            return new TranslationTextComponent("gui.touhou_little_maid.button.model_download.statue.first");
        }
        if (InfoGetManager.STATUE == InfoGetManager.Statue.UPDATE) {
            return new TranslationTextComponent("gui.touhou_little_maid.button.model_download.statue.update");
        }
        return StringTextComponent.EMPTY;
    }
}
