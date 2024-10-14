package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.other;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class CheckSchedulePosGui extends Screen {
    private final AbstractMaidContainerGui<?> parent;
    private final Component tips;
    private int middleX;
    private int middleY;

    public CheckSchedulePosGui(AbstractMaidContainerGui<?> parent, Component tips) {
        super(Component.literal("Check Schedule Pos Screen"));
        this.parent = parent;
        this.tips = tips;
    }

    @Override
    protected void init() {
        this.middleX = this.width / 2;
        this.middleY = this.height / 2;
        Button returnButton = Button.builder(Component.translatable("button.touhou_little_maid.maid.return"), (b) -> getMinecraft().setScreen(this.parent))
                .pos(middleX - 100, middleY + 10).size(200, 20).build();
        this.addRenderableWidget(returnButton);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(graphics);
        List<FormattedCharSequence> split = font.split(tips, 300);
        int startY = middleY - 10 - split.size() * (font.lineHeight + 3);
        for (FormattedCharSequence text : split) {
            graphics.drawCenteredString(font, text, middleX, startY, 0xFFFFFF);
            startY += font.lineHeight + 3;
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
}
