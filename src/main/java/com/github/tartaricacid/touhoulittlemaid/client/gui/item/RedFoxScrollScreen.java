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
    private final Map<String, List<RedFoxScrollMessage.RedFoxScrollData>> data;
    private int leftPos;
    private int topPos;
    private String selectDim;

    public RedFoxScrollScreen(Map<String, List<RedFoxScrollMessage.RedFoxScrollData>> data) {
        super(Component.literal("Red Fox Scroll"));
        this.data = data;
    }

    @Override
    protected void init() {
        this.clearWidgets();
        this.leftPos = (this.width - 400) / 2;
        this.topPos = (this.height - 200) / 2;

        int offset = this.topPos;
        for (String dim : this.data.keySet()) {
            Component name = Component.literal(dim);
            FlatColorButton dimButton = new FlatColorButton(leftPos, offset, 150, 20, name, b -> {
                this.selectDim = dim;
                this.init();
            });
            if (dim.equals(this.selectDim)) {
                dimButton.setSelect(true);
            }
            this.addRenderableWidget(dimButton);
            offset = offset + 22;
        }

        if (StringUtils.isNotBlank(this.selectDim) && this.data.containsKey(this.selectDim)) {
            List<RedFoxScrollMessage.RedFoxScrollData> scrollData = this.data.get(this.selectDim);
            boolean inSameDim = this.selectDim.equals(this.getPlayerDimension());
            int offsetIn = this.topPos;
            for (RedFoxScrollMessage.RedFoxScrollData info : scrollData) {
                this.addRenderableWidget(new FlatColorButton(leftPos + 400 - 45, offsetIn + 4, 40, 20, Component.translatable("gui.touhou_little_maid.red_fox_scroll.track"), b -> {
                }));
                offsetIn = offsetIn + 30;
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
            for (RedFoxScrollMessage.RedFoxScrollData info : scrollData) {
                BlockPos pos = info.getPos();
                Component component;
                if (inSameDim) {
                    int distance = (int) Math.sqrt(playerPos.distSqr(pos));
                    component = Component.translatable("gui.touhou_little_maid.red_fox_scroll.same_dimension", distance, pos.toShortString());
                } else {
                    component = Component.translatable("gui.touhou_little_maid.red_fox_scroll.different_dimension", pos.toShortString());
                }
                graphics.fill(leftPos + 152, offsetIn, leftPos + 400, offsetIn + 28, 0x2f55FF55);
                graphics.drawString(font, info.getName(), leftPos + 160, offsetIn + 4, ChatFormatting.GOLD.getColor());
                graphics.drawString(font, component, leftPos + 160, offsetIn + 16, ChatFormatting.GRAY.getColor());
                offsetIn = offsetIn + 30;
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
