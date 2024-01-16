package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class CheckSchedulePosGui extends Screen {
    private final AbstractMaidContainerGui<?> parent;
    private final Component tips;
    private int middleX;
    private int middleY;

    public CheckSchedulePosGui(AbstractMaidContainerGui<?> parent, Component tips) {
        super(new TextComponent("Check Schedule Pos Screen"));
        this.parent = parent;
        this.tips = tips;
    }

    @Override
    protected void init() {
        this.middleX = this.width / 2;
        this.middleY = this.height / 2;
        Button returnButton = new Button(middleX - 100, middleY + 10, 200, 20,
                new TranslatableComponent(("button.touhou_little_maid.maid.return")), (b) -> getMinecraft().setScreen(this.parent));
        this.addRenderableWidget(returnButton);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        List<FormattedCharSequence> split = font.split(tips, 300);
        int startY = middleY - 10 - split.size() * font.lineHeight;
        for (FormattedCharSequence text : split) {
            drawCenteredString(poseStack, font, text, middleX, startY, 0xFFFFFF);
            startY += font.lineHeight;
        }
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }
}
