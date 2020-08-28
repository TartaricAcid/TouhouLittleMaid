package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.BookDeskModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.LivingModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.PianoModel;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidJoy;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class TileEntityMaidJoyRenderer extends TileEntitySpecialRenderer<TileEntityMaidJoy> {
    private static final ModelBase BOOK_DESK = new BookDeskModel();
    private static final ModelBase PIANO = new PianoModel();
    private static final ModelBase LIVING = new LivingModel();

    private static final ResourceLocation BOOK_DESK_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/book_desk.png");
    private static final ResourceLocation PIANO_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/piano.png");
    private static final ResourceLocation LIVING_TEXTURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/living.png");

    @Override
    public void render(TileEntityMaidJoy te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        EnumFacing facing = EnumFacing.byHorizontalIndex(te.getBlockMetadata());
        String type = te.getType();

        this.bindTexture(getTexture(type));
        GlStateManager.pushMatrix();
        GlStateManager.disableCull();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        final boolean lightmapEnabled = GL11.glGetBoolean(GL11.GL_TEXTURE_2D);
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.translate(x + 0.5, y + 1.5, z + 0.5);
        switch (facing) {
            case NORTH:
            default:
                break;
            case SOUTH:
                GlStateManager.rotate(180, 0, 1, 0);
                break;
            case EAST:
                GlStateManager.rotate(270, 0, 1, 0);
                break;
            case WEST:
                GlStateManager.rotate(90, 0, 1, 0);
                break;
        }
        GlStateManager.rotate(180, 0, 0, 1);
        getModel(type).render(null, 0, 0, 0, 0, 0, 0.0625f);
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
    }

    @Override
    public boolean isGlobalRenderer(TileEntityMaidJoy te) {
        return true;
    }

    public ResourceLocation getTexture(String type) {
        switch (type) {
            default:
            case "reading":
                return BOOK_DESK_TEXTURE;
            case "piano":
                return PIANO_TEXTURE;
            case "live":
                return LIVING_TEXTURE;
        }
    }

    public ModelBase getModel(String type) {
        switch (type) {
            default:
            case "reading":
                return BOOK_DESK;
            case "piano":
                return PIANO;
            case "live":
                return LIVING;
        }
    }
}