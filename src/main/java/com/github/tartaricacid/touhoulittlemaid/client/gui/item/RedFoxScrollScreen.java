package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.FlatColorButton;
import com.github.tartaricacid.touhoulittlemaid.network.message.RedFoxScrollMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class RedFoxScrollScreen extends Screen {
    private static final int PER_PAGE_COUNT = 5;
    private final Map<String, List<RedFoxScrollMessage.RedFoxScrollData>> data;
    private int leftPos;
    private int topPos;
    private String selectDim;
    private int page = 0;

    public RedFoxScrollScreen(Map<String, List<RedFoxScrollMessage.RedFoxScrollData>> data) {
        super(Component.literal("Red Fox Scroll"));
        this.data = data;
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.leftPos = (this.width - 400) / 2;
        this.topPos = (this.height - 208) / 2;

        int offset = this.topPos;
        for (String dim : this.data.keySet()) {
            Component name = Component.literal(dim);
            FlatColorButton dimButton = new FlatColorButton(leftPos, offset, 150, 19, name, b -> {
                this.selectDim = dim;
                this.page = 0;
                this.init();
            });
            if (dim.equals(this.selectDim)) {
                dimButton.setSelect(true);
            }
            this.addRenderableWidget(dimButton);
            offset = offset + 21;
        }

        if (StringUtils.isNotBlank(this.selectDim) && this.data.containsKey(this.selectDim)) {
            List<RedFoxScrollMessage.RedFoxScrollData> scrollData = this.data.get(this.selectDim);
            boolean inSameDim = this.selectDim.equals(this.getPlayerDimension());
            if (scrollData.size() > PER_PAGE_COUNT) {
                addRenderableWidget(new FlatColorButton(leftPos + 400 - 20, topPos, 20, 20, Component.literal("↑"), b -> {
                    if (this.page > 0) {
                        this.page--;
                        this.init();
                    }
                }));
                addRenderableWidget(new FlatColorButton(leftPos + 400 - 20, topPos + 208 - 20, 20, 20, Component.literal("↓"), b -> {
                    if (this.page < (scrollData.size() - 1) / PER_PAGE_COUNT) {
                        this.page++;
                        this.init();
                    }
                }));
            }
            int offsetIn = this.topPos;
            for (int i = this.page * PER_PAGE_COUNT; i < this.page * PER_PAGE_COUNT + PER_PAGE_COUNT; i++) {
                if (i < scrollData.size()) {
                    RedFoxScrollMessage.RedFoxScrollData info = scrollData.get(i);
                    this.addRenderableWidget(new FlatColorButton(leftPos + 400 - 90, offsetIn + 11, 60, 20, Component.translatable("gui.touhou_little_maid.red_fox_scroll.track"), b -> {
                    }));
                    offsetIn = offsetIn + 42;
                }
            }
        }
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics);
        if (StringUtils.isNotBlank(this.selectDim) && this.data.containsKey(this.selectDim)) {
            List<RedFoxScrollMessage.RedFoxScrollData> scrollData = this.data.get(this.selectDim);
            boolean inSameDim = this.selectDim.equals(this.getPlayerDimension());
            BlockPos playerPos = this.getPlayerPos();
            int offsetIn = this.topPos;
            for (int i = this.page * PER_PAGE_COUNT; i < this.page * PER_PAGE_COUNT + PER_PAGE_COUNT; i++) {
                if (i < scrollData.size()) {
                    RedFoxScrollMessage.RedFoxScrollData info = scrollData.get(i);
                    BlockPos pos = info.getPos();
                    Component distanceText;
                    if (inSameDim) {
                        int distance = (int) Math.sqrt(playerPos.distSqr(pos));
                        distanceText = Component.translatable("gui.touhou_little_maid.red_fox_scroll.distance.same_dimension", distance);
                    } else {
                        distanceText = Component.translatable("gui.touhou_little_maid.red_fox_scroll.distance.different_dimension");
                    }
                    Component posText = Component.translatable("gui.touhou_little_maid.red_fox_scroll.position", pos.toShortString());
                    graphics.fill(leftPos + 152, offsetIn, leftPos + 400 - 22, offsetIn + 40, 0xef58626b);
                    graphics.drawString(font, info.getName(), leftPos + 160, offsetIn + 4, ChatFormatting.GOLD.getColor());
                    graphics.drawString(font, posText, leftPos + 160, offsetIn + 16, ChatFormatting.GRAY.getColor(), false);
                    graphics.drawString(font, distanceText, leftPos + 160, offsetIn + 28, ChatFormatting.GRAY.getColor(), false);
                    offsetIn = offsetIn + 42;
                }
            }
            if (scrollData.size() > PER_PAGE_COUNT) {
                String pageText = String.format("%d/%d", this.page + 1, (scrollData.size() - 1) / PER_PAGE_COUNT + 1);
                graphics.drawCenteredString(font, pageText, leftPos + 400 - 8, topPos + 104 - 5, ChatFormatting.GRAY.getColor());
            }
        }
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
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
