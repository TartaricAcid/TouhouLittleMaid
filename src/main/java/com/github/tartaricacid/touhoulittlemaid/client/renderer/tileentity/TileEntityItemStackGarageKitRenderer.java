package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.ExecutionException;

/**
 * @author TartaricAcid
 * @date 2019/7/7 15:36
 **/
@SideOnly(Side.CLIENT)
public class TileEntityItemStackGarageKitRenderer extends TileEntityItemStackRenderer {
    public static final TileEntityItemStackGarageKitRenderer instance = new TileEntityItemStackGarageKitRenderer();

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        if (itemStackIn.getItem() == Item.getItemFromBlock(MaidBlocks.GARAGE_KIT)) {
            World world = Minecraft.getMinecraft().world;
            GlStateManager.pushMatrix();

            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            } else {
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }

            String name = MaidBlocks.GARAGE_KIT.getEntityId(itemStackIn);
            Entity entity;
            try {
                entity = ClientProxy.ENTITY_CACHE.get(name, () -> {
                    Entity e = EntityList.createEntityByIDFromName(new ResourceLocation(name), world);
                    if (e == null) {
                        return new EntityMaid(world);
                    } else {
                        return e;
                    }
                });
                entity.setWorld(world);
            } catch (ExecutionException e) {
                return;
            }
            if (entity instanceof EntityMaid) {
                ((EntityMaid) entity).setModelLocation(MaidBlocks.GARAGE_KIT.getModel(itemStackIn));
                ((EntityMaid) entity).setTextureLocation(MaidBlocks.GARAGE_KIT.getTexture(itemStackIn));
                ((EntityMaid) entity).setModelName(MaidBlocks.GARAGE_KIT.getName(itemStackIn));
            }

            GlStateManager.enableColorMaterial();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            final boolean lightmapEnabled = GL11.glGetBoolean(GL11.GL_TEXTURE_2D);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

            Minecraft.getMinecraft().getRenderManager().setRenderShadow(false);
            Minecraft.getMinecraft().getRenderManager().renderEntity(entity,
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
}
