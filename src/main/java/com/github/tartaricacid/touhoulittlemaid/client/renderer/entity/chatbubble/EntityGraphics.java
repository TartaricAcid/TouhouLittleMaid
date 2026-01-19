package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Divisor;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;

public class EntityGraphics {
    private final MultiBufferSource bufferSource;
    private final PoseStack pose;
    private final EntityMaid maid;
    private final int packedLight;
    private final float partialTicks;

    public EntityGraphics(MultiBufferSource bufferSource, PoseStack pose, EntityMaid maid, int packedLight, float partialTicks) {
        this.bufferSource = bufferSource;
        this.pose = pose;
        this.maid = maid;
        this.packedLight = packedLight;
        this.partialTicks = partialTicks;
    }

    public void fill(int minX, int minY, int maxX, int maxY, int color) {
        this.fill(minX, minY, maxX, maxY, 0, color);
    }

    public void fill(int minX, int minY, int maxX, int maxY, int z, int color) {
        this.fill(RenderType.textBackground(), minX, minY, maxX, maxY, z, color);
    }

    public void fill(RenderType renderType, int minX, int minY, int maxX, int maxY, int color) {
        this.fill(renderType, minX, minY, maxX, maxY, 0, color);
    }

    public void fill(RenderType renderType, int minX, int minY, int maxX, int maxY, int z, int color) {
        Matrix4f matrix4f = this.pose.last().pose();
        if (minX < maxX) {
            int i = minX;
            minX = maxX;
            maxX = i;
        }
        if (minY < maxY) {
            int j = minY;
            minY = maxY;
            maxY = j;
        }
        VertexConsumer vertexconsumer = this.bufferSource.getBuffer(renderType);
        vertexconsumer.vertex(matrix4f, minX, minY, z).color(color).uv2(this.packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, minX, maxY, z).color(color).uv2(this.packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, maxX, maxY, z).color(color).uv2(this.packedLight).endVertex();
        vertexconsumer.vertex(matrix4f, maxX, minY, z).color(color).uv2(this.packedLight).endVertex();
    }

    public int drawString(Font font, FormattedCharSequence text, int x, int y, int color) {
        return this.drawString(font, text, x, y, color, true);
    }

    public int drawString(Font font, FormattedCharSequence text, float x, float y, int color, boolean dropShadow) {
        return font.drawInBatch(text, x, y, color, dropShadow, this.pose.last().pose(), this.bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
    }

    public int drawString(Font font, Component text, int x, int y, int color, boolean dropShadow) {
        return this.drawString(font, text.getVisualOrderText(), x, y, color, dropShadow);
    }

    public void drawWordWrap(Font font, FormattedText text, int startX, int startY, int lineWidth, int color) {
        int currentY = startY;
        for (FormattedCharSequence lineSequence : font.split(text, lineWidth)) {
            this.drawString(font, lineSequence, startX, currentY, color, false);
            currentY += 9;
        }
    }

    public void blitNineSliced(ResourceLocation atlasLocation, int x, int y, int width, int height, int sliceSize, int uOffset, int vOffset, int textureWidth, int textureHeight) {
        this.blitNineSliced(atlasLocation, x, y, width, height, sliceSize, sliceSize, sliceSize, sliceSize, uOffset, vOffset, textureWidth, textureHeight);
    }

    public void blitNineSliced(ResourceLocation atlasLocation, int x, int y, int width, int height, int sliceWidth, int sliceHeight, int uWidth, int vHeight, int textureX, int textureY) {
        this.blitNineSliced(atlasLocation, x, y, width, height, sliceWidth, sliceHeight, sliceWidth, sliceHeight, uWidth, vHeight, textureX, textureY);
    }

    public void blitNineSliced(ResourceLocation atlasLocation, int x, int y, int width, int height, int leftSliceWidth, int topSliceHeight, int rightSliceWidth, int bottomSliceHeight, int uWidth, int vHeight, int textureX, int textureY) {
        leftSliceWidth = Math.min(leftSliceWidth, width / 2);
        rightSliceWidth = Math.min(rightSliceWidth, width / 2);
        topSliceHeight = Math.min(topSliceHeight, height / 2);
        bottomSliceHeight = Math.min(bottomSliceHeight, height / 2);
        if (width == uWidth && height == vHeight) {
            this.blit(atlasLocation, x, y, textureX, textureY, width, height);
        } else if (height == vHeight) {
            this.blit(atlasLocation, x, y, textureX, textureY, leftSliceWidth, height);
            this.blitRepeating(atlasLocation, x + leftSliceWidth, y, width - rightSliceWidth - leftSliceWidth, height, textureX + leftSliceWidth, textureY, uWidth - rightSliceWidth - leftSliceWidth, vHeight);
            this.blit(atlasLocation, x + width - rightSliceWidth, y, textureX + uWidth - rightSliceWidth, textureY, rightSliceWidth, height);
        } else if (width == uWidth) {
            this.blit(atlasLocation, x, y, textureX, textureY, width, topSliceHeight);
            this.blitRepeating(atlasLocation, x, y + topSliceHeight, width, height - bottomSliceHeight - topSliceHeight, textureX, textureY + topSliceHeight, uWidth, vHeight - bottomSliceHeight - topSliceHeight);
            this.blit(atlasLocation, x, y + height - bottomSliceHeight, textureX, textureY + vHeight - bottomSliceHeight, width, bottomSliceHeight);
        } else {
            this.blit(atlasLocation, x, y, textureX, textureY, leftSliceWidth, topSliceHeight);
            this.blitRepeating(atlasLocation, x + leftSliceWidth, y, width - rightSliceWidth - leftSliceWidth, topSliceHeight, textureX + leftSliceWidth, textureY, uWidth - rightSliceWidth - leftSliceWidth, topSliceHeight);
            this.blit(atlasLocation, x + width - rightSliceWidth, y, textureX + uWidth - rightSliceWidth, textureY, rightSliceWidth, topSliceHeight);
            this.blit(atlasLocation, x, y + height - bottomSliceHeight, textureX, textureY + vHeight - bottomSliceHeight, leftSliceWidth, bottomSliceHeight);
            this.blitRepeating(atlasLocation, x + leftSliceWidth, y + height - bottomSliceHeight, width - rightSliceWidth - leftSliceWidth, bottomSliceHeight, textureX + leftSliceWidth, textureY + vHeight - bottomSliceHeight, uWidth - rightSliceWidth - leftSliceWidth, bottomSliceHeight);
            this.blit(atlasLocation, x + width - rightSliceWidth, y + height - bottomSliceHeight, textureX + uWidth - rightSliceWidth, textureY + vHeight - bottomSliceHeight, rightSliceWidth, bottomSliceHeight);
            this.blitRepeating(atlasLocation, x, y + topSliceHeight, leftSliceWidth, height - bottomSliceHeight - topSliceHeight, textureX, textureY + topSliceHeight, leftSliceWidth, vHeight - bottomSliceHeight - topSliceHeight);
            this.blitRepeating(atlasLocation, x + leftSliceWidth, y + topSliceHeight, width - rightSliceWidth - leftSliceWidth, height - bottomSliceHeight - topSliceHeight, textureX + leftSliceWidth, textureY + topSliceHeight, uWidth - rightSliceWidth - leftSliceWidth, vHeight - bottomSliceHeight - topSliceHeight);
            this.blitRepeating(atlasLocation, x + width - rightSliceWidth, y + topSliceHeight, leftSliceWidth, height - bottomSliceHeight - topSliceHeight, textureX + uWidth - rightSliceWidth, textureY + topSliceHeight, rightSliceWidth, vHeight - bottomSliceHeight - topSliceHeight);
        }
    }

    public void blitRepeating(ResourceLocation atlas, int startX, int startY, int areaWidth, int areaHeight, int uOffset, int vOffset, int sourceWidth, int sourceHeight) {
        blitRepeating(atlas, startX, startY, areaWidth, areaHeight, uOffset, vOffset, sourceWidth, sourceHeight, 256, 256);
    }

    public void blitRepeating(ResourceLocation atlas, int startX, int startY, int areaWidth, int areaHeight, int uOffset, int vOffset, int sourceWidth, int sourceHeight, int textureWidth, int textureHeight) {
        int currentX = startX;
        int sliceWidth;
        for (IntIterator widthIterator = slices(areaWidth, sourceWidth); widthIterator.hasNext(); currentX += sliceWidth) {
            sliceWidth = widthIterator.nextInt();
            int uPadding = (sourceWidth - sliceWidth) / 2;
            int currentY = startY;
            int sliceHeight;
            for (IntIterator heightIterator = slices(areaHeight, sourceHeight); heightIterator.hasNext(); currentY += sliceHeight) {
                sliceHeight = heightIterator.nextInt();
                int vPadding = (sourceHeight - sliceHeight) / 2;
                this.blit(atlas, currentX, currentY, uOffset + uPadding, vOffset + vPadding, sliceWidth, sliceHeight, textureWidth, textureHeight);
            }
        }
    }

    private static IntIterator slices(int totalLength, int sliceLength) {
        int count = Mth.positiveCeilDiv(totalLength, sliceLength);
        return new Divisor(totalLength, count);
    }

    public void blit(ResourceLocation atlasLocation, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        this.blit(atlasLocation, x, y, 0, uOffset, vOffset, uWidth, vHeight, 256, 256);
    }

    public void blit(ResourceLocation atlasLocation, int x, int y, int blitOffset, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        this.blit(atlasLocation, x, x + uWidth, y, y + vHeight, blitOffset, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    public void blit(ResourceLocation atlasLocation, int x, int y, float uOffset, float vOffset, int width, int height, int textureWidth, int textureHeight) {
        this.blit(atlasLocation, x, y, width, height, uOffset, vOffset, width, height, textureWidth, textureHeight);
    }

    public void blit(ResourceLocation atlasLocation, int x, int y, int width, int height, float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        this.blit(atlasLocation, x, x + width, y, y + height, 0, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    void blit(ResourceLocation atlasLocation, int x1, int x2, int y1, int y2, int blitOffset, int uWidth, int vHeight, float uOffset, float vOffset, int textureWidth, int textureHeight) {
        this.innerBlit(atlasLocation, x1, x2, y1, y2, blitOffset, (uOffset + 0.0F) / textureWidth, (uOffset + uWidth) / textureWidth, (vOffset + 0.0F) / textureHeight, (vOffset + vHeight) / textureHeight);
    }

    public void innerBlit(ResourceLocation atlas, int x1, int x2, int y1, int y2, int z, float minU, float maxU, float minV, float maxV) {
        RenderSystem.setShaderTexture(0, atlas);
        RenderSystem.setShader(GameRenderer::getPositionColorTexLightmapShader);
        Matrix4f matrix4f = this.pose.last().pose();
        BufferBuilder bufferBuilder = Tesselator.getInstance().getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP);
        bufferBuilder.vertex(matrix4f, x1, y1, z).color(0xFFFFFFFF).uv(minU, minV).uv2(this.packedLight).endVertex();
        bufferBuilder.vertex(matrix4f, x1, y2, z).color(0xFFFFFFFF).uv(minU, maxV).uv2(this.packedLight).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y2, z).color(0xFFFFFFFF).uv(maxU, maxV).uv2(this.packedLight).endVertex();
        bufferBuilder.vertex(matrix4f, x2, y1, z).color(0xFFFFFFFF).uv(maxU, minV).uv2(this.packedLight).endVertex();
        BufferUploader.drawWithShader(bufferBuilder.end());
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public int getPackedLight() {
        return packedLight;
    }

    public float getPartialTicks() {
        return partialTicks;
    }

    public PoseStack getPoseStack() {
        return pose;
    }

    @ApiStatus.AvailableSince("1.4.7")
    public MultiBufferSource getBufferSource() {
        return bufferSource;
    }

    @ApiStatus.AvailableSince("1.4.7")
    public PoseStack getPose() {
        return pose;
    }
}
