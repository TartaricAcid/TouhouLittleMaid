package com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class ModelDetailsButton extends StateSwitchingButton {
    private static final ResourceLocation BUTTON_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/gui/skin_detail.png");
    private final Consumer<Boolean> onClick;
    private final TranslatableComponent name;

    public ModelDetailsButton(int xIn, int yIn, String langKey, Consumer<Boolean> onClick) {
        super(xIn, yIn, 128, 12, false);
        this.name = new TranslatableComponent(langKey);
        this.onClick = onClick;
        this.initTextureValues(0, 0, 128, 12, BUTTON_TEXTURE);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        super.renderButton(poseStack, mouseX, mouseY, partialTicks);
        drawString(poseStack, Minecraft.getInstance().font, name, this.x + 14, this.y + 2, 0xffffffff);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.isStateTriggered = !this.isStateTriggered;
        onClick.accept(this.isStateTriggered);
    }
}
