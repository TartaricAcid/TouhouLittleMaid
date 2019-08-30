package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.function.Consumer;

/**
 * @author TartaricAcid
 * @date 2019/8/30 15:31
 **/
@SideOnly(Side.CLIENT)
public final class RenderHelper {
    private RenderHelper() {
    }

    public static void drawEntityPoint(EntityXPOrb entity, double x, double y, double z, RenderManager renderManager, Consumer<Entity> textureBindFunc) {
        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        textureBindFunc.accept(entity);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 170f, 170f);

        int textureIndex = entity.getTextureByXP();
        double texturePos1 = textureIndex % 4 * 16 / 64.0;
        double texturePos2 = (textureIndex % 4 * 16 + 16) / 64.0;
        double texturePos3 = textureIndex / 4 * 16 / 64.0F;
        double texturePos4 = (textureIndex / 4 * 16 + 16) / 64.0;

        GlStateManager.translate(0.0F, 0.1F, 0.0F);
        GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (renderManager.options.thirdPersonView == 2 ? -1 : 1) * -renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.3F, 0.3F, 0.3F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-1D, -0.25D, 0.0D).tex(texturePos1, texturePos4).endVertex();
        bufferbuilder.pos(1D, -0.25D, 0.0D).tex(texturePos2, texturePos4).endVertex();
        bufferbuilder.pos(1D, 1.75D, 0.0D).tex(texturePos2, texturePos3).endVertex();
        bufferbuilder.pos(-1D, 1.75D, 0.0D).tex(texturePos1, texturePos3).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    public static void drawEntityXP(EntityXPOrb entity, double x, double y, double z, float partialTicks, RenderManager renderManager, Consumer<Entity> textureBindFunc) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y, (float) z);
        textureBindFunc.accept(entity);
        net.minecraft.client.renderer.RenderHelper.enableStandardItemLighting();
        int textureIndex = entity.getTextureByXP();
        double texturePos1 = textureIndex % 4 * 16 / 64.0;
        double texturePos2 = (textureIndex % 4 * 16 + 16) / 64.0;
        double texturePos3 = textureIndex / 4 * 16 / 64.0;
        double texturePos4 = (textureIndex / 4 * 16 + 16) / 64.0;
        int brightness = entity.getBrightnessForRender();
        int lightmapX = brightness % 65536;
        int lightmapY = brightness / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lightmapX, (float) lightmapY);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float tmpValue = ((float) entity.xpColor + partialTicks) / 2.0F;
        lightmapY = (int) ((MathHelper.sin(tmpValue + 0.0F) + 1.0F) * 0.5F * 255.0F);
        int j1 = (int) ((MathHelper.sin(tmpValue + 4.1887903F) + 1.0F) * 0.1F * 255.0F);
        GlStateManager.translate(0.0F, 0.1F, 0.0F);
        GlStateManager.rotate(180.0F - renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (renderManager.options.thirdPersonView == 2 ? -1 : 1) * -renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.3F, 0.3F, 0.3F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        bufferbuilder.pos(-0.5D, -0.25D, 0.0D).tex(texturePos1, texturePos4).color(lightmapY, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(0.5D, -0.25D, 0.0D).tex(texturePos2, texturePos4).color(lightmapY, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(0.5D, 0.75D, 0.0D).tex(texturePos2, texturePos3).color(lightmapY, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        bufferbuilder.pos(-0.5D, 0.75D, 0.0D).tex(texturePos1, texturePos3).color(lightmapY, 255, j1, 128).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();
        GlStateManager.disableBlend();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }
}
