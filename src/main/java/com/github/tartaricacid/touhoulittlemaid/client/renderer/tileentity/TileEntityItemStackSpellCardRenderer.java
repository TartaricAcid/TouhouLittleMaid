package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.danmaku.CustomSpellCardEntry;
import com.github.tartaricacid.touhoulittlemaid.item.ItemSpellCard;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class TileEntityItemStackSpellCardRenderer extends TileEntityItemStackRenderer {
    public static final TileEntityItemStackSpellCardRenderer INSTANCE = new TileEntityItemStackSpellCardRenderer();

    @Override
    public void renderByItem(@Nullable ItemStack itemStackIn) {
        CustomSpellCardEntry entry = ItemSpellCard.getCustomSpellCardEntry(itemStackIn, ClientProxy.CUSTOM_SPELL_CARD_MAP_CLIENT);
        if (entry != null) {
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.enableColorMaterial();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            final boolean lightmapEnabled = GL11.glGetBoolean(GL11.GL_TEXTURE_2D);
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);

            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufBuilder = tessellator.getBuffer();
            bufBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            Minecraft.getMinecraft().renderEngine.bindTexture(entry.getIcon());
            bufBuilder.pos(0, 1, 0.5).tex(0, 0).endVertex();
            bufBuilder.pos(0, 0, 0.5).tex(0, 1).endVertex();
            bufBuilder.pos(1, 0, 0.5).tex(1, 1).endVertex();
            bufBuilder.pos(1, 1, 0.5).tex(1, 0).endVertex();
            tessellator.draw();

            GlStateManager.popMatrix();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            if (lightmapEnabled) {
                GlStateManager.enableTexture2D();
            } else {
                GlStateManager.disableTexture2D();
            }
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.enableBlend();
        }
    }
}
