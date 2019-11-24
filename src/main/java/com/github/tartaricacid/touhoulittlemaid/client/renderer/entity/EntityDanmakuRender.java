package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
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

@SideOnly(Side.CLIENT)
public class EntityDanmakuRender extends Render<EntityDanmaku> {
    public static final Factory FACTORY = new EntityDanmakuRender.Factory();
    private static final ResourceLocation SINGLE_PLANE_DANMAKU = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/singe_plane_danmaku.png");

    private EntityDanmakuRender(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(@Nonnull EntityDanmaku entity, double x, double y, double z, float entityYaw, float partialTicks) {
        DanmakuType type = entity.getType();
        switch (type) {
            case PELLET:
            case BALL:
            case ORBS:
            case BIG_BALL:
            case BUBBLE:
            case HEART:
            default:
                renderSinglePlaneDanmaku(entity, x, y, z, type, SINGLE_PLANE_DANMAKU);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(@Nonnull EntityDanmaku entity) {
        return SINGLE_PLANE_DANMAKU;
    }

    private void renderSinglePlaneDanmaku(EntityDanmaku entity, double x, double y, double z, DanmakuType type, ResourceLocation textures) {
        // 获取相关数据
        DanmakuColor color = entity.getColor();
        // 材质宽度
        int w = 416;
        // 材质长度
        int l = 192;

        // 依据类型颜色开始定位材质位置（材质块都是 32 * 32 大小）
        double pStartU = 32 * color.ordinal();
        double pStartV = 32 * type.ordinal();

        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX,
                1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufBuilder = tessellator.getBuffer();

        bufBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        this.renderManager.renderEngine.bindTexture(textures);

        bufBuilder.pos(-type.getSize(), type.getSize(), 0).tex((pStartU + 0) / w, (pStartV + 0) / l).endVertex();
        bufBuilder.pos(-type.getSize(), -type.getSize(), 0).tex((pStartU + 0) / w, (pStartV + 32) / l).endVertex();
        bufBuilder.pos(type.getSize(), -type.getSize(), 0).tex((pStartU + 32) / w, (pStartV + 32) / l).endVertex();
        bufBuilder.pos(type.getSize(), type.getSize(), 0).tex((pStartU + 32) / w, (pStartV + 0) / l).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    public static class Factory implements IRenderFactory<EntityDanmaku> {
        @Override
        public Render<? super EntityDanmaku> createRenderFor(RenderManager manager) {
            return new EntityDanmakuRender(manager);
        }
    }
}
