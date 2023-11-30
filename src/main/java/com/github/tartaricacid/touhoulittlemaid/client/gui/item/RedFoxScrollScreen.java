package com.github.tartaricacid.touhoulittlemaid.client.gui.item;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Map;

public class RedFoxScrollScreen extends Screen {
    private final Map<String, List<BlockPos>> data;
    private int leftPos;
    private int topPos;

    public RedFoxScrollScreen(Map<String, List<BlockPos>> data) {
        super(Component.literal("Red Fox Scroll"));
        this.data = data;
    }

    @Override
    protected void init() {
        super.init();
        this.leftPos = (this.width - 256) / 2;
        this.topPos = (this.height - 200) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(graphics);
        BlockPos playerPos = this.getPlayerPos();
        int offset = this.topPos;
        for (String dimension : data.keySet()) {
            graphics.drawCenteredString(font, dimension, this.leftPos + 128, offset, 0xFFFFFF);
            offset = offset + 15;
            List<BlockPos> posList = data.get(dimension);
            for (BlockPos pos : posList) {
                int distance = (int) Math.sqrt(playerPos.distSqr(pos));
                graphics.fill(this.leftPos, offset - 3, this.leftPos + 256, offset + 10, 0xFF2A2A2A);
                graphics.drawString(font, String.format("%dm, [%s]", distance, pos.toShortString()), this.leftPos + 5, offset, 0xffff55);
                offset = offset + 15;
            }
            offset = offset + 5;
        }
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
    }

    private BlockPos getPlayerPos() {
        if (this.getMinecraft().player != null) {
            return this.getMinecraft().player.blockPosition();
        }
        return BlockPos.ZERO;
    }
}
