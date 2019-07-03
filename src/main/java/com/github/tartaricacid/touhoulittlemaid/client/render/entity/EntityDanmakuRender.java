package com.github.tartaricacid.touhoulittlemaid.client.render.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
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

import java.util.Objects;

@SideOnly(Side.CLIENT)
public class EntityDanmakuRender extends Render<EntityDanmaku> {
    public static final Factory FACTORY = new EntityDanmakuRender.Factory();

    public EntityDanmakuRender(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void doRender(EntityDanmaku entity, double x, double y, double z, float entityYaw, float partialTicks) {
        // 获取相关数据
        DanmakuType type = entity.getType();
        DanmakuColor color = entity.getColor();
        int w = 224; // 材质宽度
        int l = 320; // 材质长度

        // 依据类型颜色开始定位材质位置（材质块都是 32 * 32 大小）
        double pStartU = 32 * color.getIndex();
        double pStartV = 32 * type.getIndex();

        GlStateManager.disableLighting();
        GlStateManager.pushMatrix();
        GlStateManager.enableRescaleNormal();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.alphaFunc(GL11.GL_GREATER, 0f);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        GlStateManager.translate(x, y, z);
        GlStateManager.rotate(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * this.renderManager.playerViewX,
                1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(180F, 0.0F, 0.0F, 1.0F);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufbuilder = tessellator.getBuffer();

        bufbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        this.renderManager.renderEngine.bindTexture(Objects.requireNonNull(getEntityTexture(entity)));

        bufbuilder.pos(-type.getSize(), type.getSize(), 0).tex((pStartU + 0) / w, (pStartV + 32) / l).endVertex();
        bufbuilder.pos(-type.getSize(), -type.getSize(), 0).tex((pStartU + 0) / w, (pStartV + 0) / l).endVertex();
        bufbuilder.pos(type.getSize(), -type.getSize(), 0).tex((pStartU + 32) / w, (pStartV + 0) / l).endVertex();
        bufbuilder.pos(type.getSize(), type.getSize(), 0).tex((pStartU + 32) / w, (pStartV + 32) / l).endVertex();
        tessellator.draw();

        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityDanmaku entity) {
        return new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/danmaku.png");
    }

    public static class Factory implements IRenderFactory<EntityDanmaku> {
        @Override
        public Render<? super EntityDanmaku> createRenderFor(RenderManager manager) {
            return new EntityDanmakuRender(manager);
        }
    }
}
