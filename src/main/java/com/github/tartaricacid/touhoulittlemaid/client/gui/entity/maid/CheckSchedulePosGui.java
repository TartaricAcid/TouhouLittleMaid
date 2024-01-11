package com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class CheckSchedulePosGui extends Screen {
    private final AbstractMaidContainerGui<?> parent;
    private final ITextComponent tips;
    private int middleX;
    private int middleY;

    public CheckSchedulePosGui(AbstractMaidContainerGui<?> parent, ITextComponent tips) {
        super(new StringTextComponent("Check Schedule Pos Screen"));
        this.parent = parent;
        this.tips = tips;
    }

    @Override
    protected void init() {
        this.middleX = this.width / 2;
        this.middleY = this.height / 2;
        Button returnButton = new Button(middleX - 100, middleY + 10, 200, 20,
                new TranslationTextComponent("button.touhou_little_maid.maid.return"), (b) -> getMinecraft().setScreen(this.parent));
        this.addButton(returnButton);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(matrixStack);
        List<IReorderingProcessor> split = font.split(tips, 300);
        int startY = middleY - 10 - split.size() * font.lineHeight;
        for (IReorderingProcessor text : split) {
            drawCenteredString(matrixStack, font, (ITextComponent) text, middleX, startY, 0xFFFFFF);
            startY += font.lineHeight;
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }
}
