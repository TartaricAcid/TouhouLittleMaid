package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.AltarModel;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityAltar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.ExecutionException;

/**
 * @author TartaricAcid
 * @date 2019/8/31 22:02
 **/
public class TileEntityAltarRenderer extends TileEntitySpecialRenderer<TileEntityAltar> {
    private static final ModelBase MODEL = new AltarModel();
    private static final ResourceLocation TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/altar.png");
    private static final String ENTITY_ITEM_ID = "minecraft:item";

    @Override
    public void render(TileEntityAltar te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (te.isRender()) {
            this.bindTexture(TEXTURE);
            GlStateManager.pushMatrix();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            final boolean lightmapEnabled = GL11.glGetBoolean(GL11.GL_TEXTURE_2D);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            switch (te.getFacing()) {
                case NORTH:
                    GlStateManager.translate(x, y - 1.5, z + 4);
                    break;
                case SOUTH:
                    GlStateManager.translate(x + 1, y - 1.5, z - 3);
                    GlStateManager.rotate(180, 0, 1, 0);
                    break;
                case EAST:
                    GlStateManager.translate(x - 3, y - 1.5, z);
                    GlStateManager.rotate(270, 0, 1, 0);
                    break;
                case WEST:
                    GlStateManager.translate(x + 4, y - 1.5, z + 1);
                    GlStateManager.rotate(90, 0, 1, 0);
                    break;
                default:
                    GlStateManager.translate(x, y - 1.5, z + 4);
            }
            GlStateManager.rotate(180, 0, 0, 1);
            MODEL.render(null, 0, 0, 0, 0, 0, 0.0625f);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            if (lightmapEnabled) {
                GlStateManager.enableTexture2D();
            } else {
                GlStateManager.disableTexture2D();
            }
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
        if (te.isCanPlaceItem() && te.handler.getStackInSlot(0) != ItemStack.EMPTY) {
            Entity entity;
            try {
                entity = ClientProxy.ENTITY_CACHE.get(ENTITY_ITEM_ID, () -> {
                    Entity e = EntityList.createEntityByIDFromName(new ResourceLocation(ENTITY_ITEM_ID), getWorld());
                    if (e == null) {
                        return new EntityItem(getWorld());
                    } else {
                        return e;
                    }
                });
                if (entity instanceof EntityItem) {
                    ((EntityItem) entity).setItem(te.handler.getStackInSlot(0));
                    ((EntityItem) entity).age = (int) Minecraft.getSystemTime() / 50;
                }
            } catch (ExecutionException e) {
                return;
            }

            GlStateManager.pushMatrix();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            final boolean lightmapEnabled = GL11.glGetBoolean(GL11.GL_TEXTURE_2D);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.translate(x, y + 1.0625, z);
            GlStateManager.translate(0.5, 0, 0.5);
            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            } else {
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }
            Minecraft.getMinecraft().getRenderManager().renderEntity(entity, 0, 0, 0, 0, partialTicks, true);
            GlStateManager.popMatrix();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            if (lightmapEnabled) {
                GlStateManager.enableTexture2D();
            } else {
                GlStateManager.disableTexture2D();
            }
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }

    @Override
    public boolean isGlobalRenderer(TileEntityAltar te) {
        return true;
    }
}
