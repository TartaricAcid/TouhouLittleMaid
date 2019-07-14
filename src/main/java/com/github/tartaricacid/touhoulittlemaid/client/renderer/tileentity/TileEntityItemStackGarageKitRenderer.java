package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

/**
 * @author TartaricAcid
 * @date 2019/7/7 15:36
 **/
public class TileEntityItemStackGarageKitRenderer extends TileEntityItemStackRenderer {
    public static TileEntityItemStackGarageKitRenderer instance = new TileEntityItemStackGarageKitRenderer();

    @Override
    public void renderByItem(ItemStack itemStackIn) {
        if (itemStackIn.getItem() == Item.getItemFromBlock(MaidBlocks.GARAGE_KIT)) {
            World world = Minecraft.getMinecraft().world;
            final Entity entity = EntityList.createEntityByIDFromName(new ResourceLocation(MaidBlocks.GARAGE_KIT.getCharacter(itemStackIn)), world);

            GlStateManager.enableColorMaterial();
            GlStateManager.pushMatrix();

            if (Minecraft.isAmbientOcclusionEnabled()) {
                GlStateManager.shadeModel(GL11.GL_SMOOTH);
            } else {
                GlStateManager.shadeModel(GL11.GL_FLAT);
            }

            RenderHelper.enableStandardItemLighting();
            GlStateManager.scale(0.5, 0.5, 0.5);
            GlStateManager.rotate(22.5f, 1, 0, 0);
            GlStateManager.rotate(22.5f, 0, 1, 0);
            Minecraft.getMinecraft().getRenderManager().setRenderShadow(false);
            Minecraft.getMinecraft().getRenderManager().renderEntity(entity == null ? new EntityMaid(world) : entity,
                    0.875, 0.25, 0.75, 0, 0, true);
            Minecraft.getMinecraft().getRenderManager().setRenderShadow(true);

            GlStateManager.popMatrix();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableRescaleNormal();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
    }
}
