package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/8/9 0:36
 **/
public class TileEntityItemStackChairRenderer extends TileEntityItemStackRenderer {
    public static final TileEntityItemStackChairRenderer INSTANCE = new TileEntityItemStackChairRenderer();

    @Override
    public void renderByItem(@Nonnull ItemStack itemStackIn) {
        World world = Minecraft.getMinecraft().world;
        String entityId = TouhouLittleMaid.MOD_ID + ":entity.item.chair";
        EntityChair entityChair;
        try {
            entityChair = (EntityChair) ClientProxy.ENTITY_CACHE.get(entityId, () -> {
                Entity e = EntityList.createEntityByIDFromName(new ResourceLocation(entityId), world);
                if (e == null) {
                    return new EntityChair(world);
                } else {
                    return e;
                }
            });
        }
        catch (ExecutionException | ClassCastException e) {
            e.printStackTrace();
            return;
        }
        entityChair.setModelId(ItemChair.getChairModelId(itemStackIn));

        GlStateManager.pushMatrix();

        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        GlStateManager.enableColorMaterial();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        final boolean lightmapEnabled = GL11.glGetBoolean(GL11.GL_TEXTURE_2D);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

        Minecraft.getMinecraft().getRenderManager().setRenderShadow(false);
        Minecraft.getMinecraft().getRenderManager().renderEntity(entityChair,
                0.875, 0.25, 0.75, 0, 0, true);
        Minecraft.getMinecraft().getRenderManager().setRenderShadow(true);

        GlStateManager.popMatrix();

        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        if (lightmapEnabled) {
            GlStateManager.enableTexture2D();
        } else {
            GlStateManager.disableTexture2D();
        }
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.enableBlend();
    }
}
