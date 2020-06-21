package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.StatueBaseModel;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityStatue;
import com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.ExecutionException;

import static com.github.tartaricacid.touhoulittlemaid.util.EntityCacheUtil.clearMaidDataResidue;

public class TileEntityStatueRenderer extends TileEntitySpecialRenderer<TileEntityStatue> {
    private static final StatueBaseModel BASE = new StatueBaseModel();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/statue_base.png");

    @Override
    @ParametersAreNonnullByDefault
    public void render(TileEntityStatue te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        if (!te.isCoreBlock()) {
            return;
        }

        float size = te.getSize().getScale();
        final EnumFacing facing = te.getFacing();

        float offset = 0;
        if (te.getSize() == TileEntityStatue.Size.MIDDLE) {
            offset = 1.0f / 4.0f;
        } else if (te.getSize() == TileEntityStatue.Size.BIG) {
            offset = 1.0f / 3.0f;
        }

        this.bindTexture(TEXTURE);
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        final boolean lightmapEnabled = GL11.glGetBoolean(GL11.GL_TEXTURE_2D);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        switch (te.getFacing()) {
            case EAST:
                GlStateManager.translate(x - offset * size, y, z - offset * size);
                break;
            case NORTH:
                GlStateManager.translate(x - offset * size, y, z + offset * size);
                break;
            case WEST:
                GlStateManager.translate(x + offset * size, y, z + offset * size);
                break;
            case SOUTH:
                GlStateManager.translate(x + offset * size, y, z - offset * size);
                break;
            default:
                GlStateManager.translate(x, y, z);
        }
        GlStateManager.scale(size, size, size);
        GlStateManager.translate(0.5 / size, 0.5, 0.5 / size);
        GlStateManager.rotate(180, 0, 0, 1);
        BASE.render(null, 0, 0, 0, 0, 0, 0.0625f);
        GlStateManager.disableBlend();
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        if (lightmapEnabled) {
            GlStateManager.enableTexture2D();
        } else {
            GlStateManager.disableTexture2D();
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        Entity entity;
        String name = te.getModelId();

        try {
            entity = EntityCacheUtil.ENTITY_CACHE.get(name, () -> {
                Entity e = EntityList.createEntityByIDFromName(new ResourceLocation(name), getWorld());
                if (e == null) {
                    return new EntityMaid(getWorld());
                } else {
                    return e;
                }
            });
            if (entity instanceof EntityMaid) {
                EntityMaid maid = (EntityMaid) entity;
                clearMaidDataResidue(maid, true);
                if (te.getModelId() != null) {
                    maid.setModelId(te.getModelId());
                }
            }
        } catch (ExecutionException e) {
            return;
        }

        size = te.getSize().getScale();

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.scale(size, size, size);
        GlStateManager.translate(0.5 / size, 0.21328125, 0.5 / size);

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        switch (facing) {
            case NORTH:
            default:
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            case SOUTH:
                break;
            case EAST:
                GlStateManager.rotate(90, 0, 1, 0);
                break;
            case WEST:
                GlStateManager.rotate(270, 0, 1, 0);
                break;
        }

        Minecraft.getMinecraft().getRenderManager().setRenderShadow(false);
        Minecraft.getMinecraft().getRenderManager().renderEntity(entity, offset, 0, -offset, 0, 0, true);
        Minecraft.getMinecraft().getRenderManager().setRenderShadow(true);
        GlStateManager.popMatrix();
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isGlobalRenderer(TileEntityStatue te) {
        return true;
    }
}
