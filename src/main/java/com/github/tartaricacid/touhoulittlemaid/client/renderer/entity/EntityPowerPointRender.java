package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

/**
 * 来自于原版经验球的渲染代码
 *
 * @author TartaricAcid
 * @date 2019/8/29 17:05
 **/
@SideOnly(Side.CLIENT)
public class EntityPowerPointRender extends Render<EntityPowerPoint> {
    public static final EntityPowerPointRender.Factory FACTORY = new EntityPowerPointRender.Factory();
    private static final ResourceLocation POWER_POINT_TEXTURES = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/power_point.png");

    private EntityPowerPointRender(RenderManager renderManagerIn) {
        super(renderManagerIn);
        this.shadowSize = 0.15F;
        this.shadowOpaque = 0.75F;
    }

    @Override
    public void doRender(@Nonnull EntityPowerPoint entity, double x, double y, double z, float entityYaw, float partialTicks) {
        if (!this.renderOutlines) {
            GlStateManager.disableLighting();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x, (float) y, (float) z);
            bindEntityTexture(entity);
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
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityPowerPoint entity) {
        return POWER_POINT_TEXTURES;
    }

    public static class Factory implements IRenderFactory<EntityPowerPoint> {
        @Override
        public Render<? super EntityPowerPoint> createRenderFor(RenderManager manager) {
            return new EntityPowerPointRender(manager);
        }
    }
}
