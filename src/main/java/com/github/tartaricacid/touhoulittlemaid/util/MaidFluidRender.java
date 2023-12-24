package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Matrix4f;

import java.util.Optional;


/**
 * From JEI: https://github.com/mezz/JustEnoughItems/blob/1.20/Forge/src/main/java/mezz/jei/forge/platform/FluidHelper.java
 */
@OnlyIn(Dist.CLIENT)
public final class MaidFluidRender {
    private static final int TEXTURE_SIZE = 16;

    public static Component getFluidName(String fluidId, int amount) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidId));
        if (amount <= 0 || fluid == null || fluid.isSame(Fluids.EMPTY)) {
            return Component.empty();
        }
        return fluid.getFluidType().getDescription();
    }

    public static void drawFluid(GuiGraphics graphics, int x, int y, int width, int height, String fluidId, int amount, int capacity) {
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidId));
        if (amount <= 0 || fluid == null || fluid.isSame(Fluids.EMPTY)) {
            return;
        }
        FluidStack fluidStack = new FluidStack(fluid, amount);
        getStillFluidSprite(fluidStack).ifPresent(fluidStillSprite -> {
            int fluidColor = getColorTint(fluidStack);
            int scaledAmount = (amount * height) / capacity;
            if (scaledAmount < 1) {
                // 至少渲染一行像素，让人知道里面有东西
                scaledAmount = 1;
            }
            if (scaledAmount > height) {
                scaledAmount = height;
            }
            graphics.pose().pushPose();
            graphics.pose().translate(x, y, 0);
            drawTiledSprite(graphics, width, height, fluidColor, scaledAmount, fluidStillSprite);
            graphics.pose().popPose();
        });
    }

    private static Optional<TextureAtlasSprite> getStillFluidSprite(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
        ResourceLocation fluidStill = renderProperties.getStillTexture(fluidStack);
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
        return Optional.of(sprite).filter(s -> s.atlasLocation() != MissingTextureAtlasSprite.getLocation());
    }

    private static int getColorTint(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid);
        return renderProperties.getTintColor(fluidStack);
    }

    private static void drawTiledSprite(GuiGraphics guiGraphics, final int tiledWidth, final int tiledHeight, int color, long scaledAmount, TextureAtlasSprite sprite) {
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Matrix4f matrix = guiGraphics.pose().last().pose();
        setGLColorFromInt(color);

        final int xTileCount = tiledWidth / TEXTURE_SIZE;
        final int xRemainder = tiledWidth - (xTileCount * TEXTURE_SIZE);
        final long yTileCount = scaledAmount / TEXTURE_SIZE;
        final long yRemainder = scaledAmount - (yTileCount * TEXTURE_SIZE);

        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = (xTile == xTileCount) ? xRemainder : TEXTURE_SIZE;
                long height = (yTile == yTileCount) ? yRemainder : TEXTURE_SIZE;
                int x = (xTile * TEXTURE_SIZE);
                int y = tiledHeight - ((yTile + 1) * TEXTURE_SIZE);
                if (width > 0 && height > 0) {
                    long maskTop = TEXTURE_SIZE - height;
                    int maskRight = TEXTURE_SIZE - width;
                    drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight, 100);
                }
            }
        }
    }

    private static void setGLColorFromInt(int color) {
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        float alpha = ((color >> 24) & 0xFF) / 255F;

        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, long maskTop, long maskRight, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax = uMax - (maskRight / 16F * (uMax - uMin));
        vMax = vMax - (maskTop / 16F * (vMax - vMin));

        RenderSystem.setShader(GameRenderer::getPositionTexShader);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.vertex(matrix, xCoord, yCoord + 16, zLevel).uv(uMin, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + 16, zLevel).uv(uMax, vMax).endVertex();
        bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, zLevel).uv(uMax, vMin).endVertex();
        bufferBuilder.vertex(matrix, xCoord, yCoord + maskTop, zLevel).uv(uMin, vMin).endVertex();
        tesselator.end();
    }
}
