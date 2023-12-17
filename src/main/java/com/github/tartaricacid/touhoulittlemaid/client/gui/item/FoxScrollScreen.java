package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.FoxScrollMessage;
import com.github.tartaricacid.touhoulittlemaid.network.message.SetScrollData;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class FoxScrollScreen extends Screen {
    private static final int PER_PAGE_COUNT = 5;
    private final Map<String, List<FoxScrollMessage.FoxScrollData>> data;
    private int leftPos;
    private int topPos;
    private String selectDim;
    private int page = 0;

    public FoxScrollScreen(Map<String, List<FoxScrollMessage.FoxScrollData>> data) {
        super(new StringTextComponent("Red Fox Scroll"));
        this.data = data;
        if (!this.data.isEmpty()) {
            this.selectDim = this.data.keySet().stream().findFirst().get();
        }
    }

    @Override
    protected void init() {
        this.buttons.clear();
        this.children.clear();
        this.leftPos = (this.width - 400) / 2;
        this.topPos = (this.height - 208) / 2;
        this.addDimensionButtons();
        this.addPointButtons();
    }

    private void addPointButtons() {
        if (StringUtils.isNotBlank(this.selectDim) && this.data.containsKey(this.selectDim)) {
            List<FoxScrollMessage.FoxScrollData> scrollData = this.data.get(this.selectDim);
            if (scrollData.size() > PER_PAGE_COUNT) {
                addButton(new FlatColorButton(leftPos + 400 - 20, topPos, 20, 20, new StringTextComponent("↑"), b -> {
                    if (this.page > 0) {
                        this.page--;
                        this.init();
                    }
                }));
                addButton(new FlatColorButton(leftPos + 400 - 20, topPos + 208 - 20, 20, 20, new StringTextComponent("↓"), b -> {
                    if (this.page < (scrollData.size() - 1) / PER_PAGE_COUNT) {
                        this.page++;
                        this.init();
                    }
                }));
            }
            int offsetIn = this.topPos;
            for (int i = this.page * PER_PAGE_COUNT; i < this.page * PER_PAGE_COUNT + PER_PAGE_COUNT; i++) {
                if (i < scrollData.size()) {
                    FoxScrollMessage.FoxScrollData info = scrollData.get(i);
                    this.addButton(new FlatColorButton(leftPos + 400 - 90, offsetIn + 11, 60, 20, new TranslationTextComponent("gui.touhou_little_maid.fox_scroll.track"), b -> {
                        NetworkHandler.CHANNEL.sendToServer(new SetScrollData(this.selectDim, info.getPos()));
                    }));
                    offsetIn = offsetIn + 42;
                }
            }
        }
    }

    private void addDimensionButtons() {
        int offset = this.topPos;
        for (String dim : this.data.keySet()) {
            ITextComponent name = new StringTextComponent(dim);
            FlatColorButton dimButton = new FlatColorButton(leftPos, offset, 150, 19, name, b -> {
                this.selectDim = dim;
                this.page = 0;
                this.init();
            });
            if (dim.equals(this.selectDim)) {
                dimButton.setSelect(true);
            }
            this.addButton(dimButton);
            offset = offset + 21;
        }
    }


    @Override
    public void render(MatrixStack matrixStack, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(matrixStack);
        if (this.data.isEmpty()) {
            int x = this.width / 2;
            int y = this.height / 2 - 5;
            drawCenteredString(matrixStack, font, new TranslationTextComponent("gui.touhou_little_maid.fox_scroll.empty"), x, y, 0xFF0000);
            return;
        }
        this.renderMain(matrixStack);
        super.render(matrixStack, pMouseX, pMouseY, pPartialTick);
    }

    private void renderMain(MatrixStack matrixStack) {
        if (StringUtils.isNotBlank(this.selectDim) && this.data.containsKey(this.selectDim)) {
            List<FoxScrollMessage.FoxScrollData> scrollData = this.data.get(this.selectDim);
            boolean inSameDim = this.selectDim.equals(this.getPlayerDimension());
            BlockPos playerPos = this.getPlayerPos();
            int offsetIn = this.topPos;
            for (int i = this.page * PER_PAGE_COUNT; i < this.page * PER_PAGE_COUNT + PER_PAGE_COUNT; i++) {
                if (i < scrollData.size()) {
                    FoxScrollMessage.FoxScrollData info = scrollData.get(i);
                    BlockPos pos = info.getPos();
                    ITextComponent distanceText;
                    if (inSameDim) {
                        int distance = (int) Math.sqrt(playerPos.distSqr(pos));
                        distanceText = new TranslationTextComponent("gui.touhou_little_maid.fox_scroll.distance.same_dimension", distance);
                    } else {
                        distanceText = new TranslationTextComponent("gui.touhou_little_maid.fox_scroll.distance.different_dimension");
                    }
                    ITextComponent posText = new TranslationTextComponent("gui.touhou_little_maid.fox_scroll.position", pos.toShortString());
                    fill(matrixStack, leftPos + 152, offsetIn, leftPos + 400 - 22, offsetIn + 40, 0xef58626b);
                    drawString(matrixStack, font, info.getName(), leftPos + 160, offsetIn + 4, TextFormatting.GOLD.getColor());
                    font.draw(matrixStack, posText, leftPos + 160, offsetIn + 16, TextFormatting.GRAY.getColor());
                    font.draw(matrixStack, distanceText, leftPos + 160, offsetIn + 28, TextFormatting.GRAY.getColor());
                    offsetIn = offsetIn + 42;
                }
            }
            if (scrollData.size() > PER_PAGE_COUNT) {
                String pageText = String.format("%d/%d", this.page + 1, (scrollData.size() - 1) / PER_PAGE_COUNT + 1);
                drawCenteredString(matrixStack, font, pageText, leftPos + 400 - 8, topPos + 104 - 5, TextFormatting.GRAY.getColor());
            }
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private BlockPos getPlayerPos() {
        if (this.getMinecraft().player != null) {
            return this.getMinecraft().player.blockPosition();
        }
        return BlockPos.ZERO;
    }

    @Nullable
    private String getPlayerDimension() {
        if (this.getMinecraft().player != null) {
            return this.getMinecraft().player.level.dimension().location().toString();
        }
        return null;
    }
}
