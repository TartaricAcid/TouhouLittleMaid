package com.github.tartaricacid.touhoulittlemaid.client.renderer.tileentity;

import com.github.tartaricacid.touhoulittlemaid.block.BlockGrid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGrid.Direction;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGrid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TileEntityGridRenderer extends TileEntitySpecialRenderer<TileEntityGrid> {
    @Override
    public void render(TileEntityGrid te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        super.render(te, x, y, z, partialTicks, destroyStage, alpha);

        if (x * x + y * y + z * z > 256) {
            return;
        }

        IBlockState state = te.getBlockType().getStateFromMeta(te.getBlockMetadata());
        Direction direction = state.getValue(BlockGrid.DIRECTION);
        Vec3i vec = direction.face.getDirectionVec();
        double d = x * vec.getX() + y * vec.getY() + z * vec.getZ();
        if (d > 2) {
            return;
        }

        int itemCount = 0;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = te.handler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                ++itemCount;
            }
        }
        if (itemCount == 0) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        //Matrix4f matrix = new Matrix4f(direction.matrix());
        //try {
        //    GlStateManager.disableTexture2D();
        //    matrix.invert();
        //    GlStateManager.color(0, 0, 0, 1);
        //    Point3f point0 = new Point3f(0, 0, 0);
        //    Point3f point1 = new Point3f(1, 0, 0);
        //    Point3f point2 = new Point3f(1, 0, 1);
        //    Point3f point3 = new Point3f(0, 0, 1);
        //
        //    BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        //    buffer.begin(3, DefaultVertexFormats.POSITION);
        //    buffer.pos(point0.x, point0.y, point0.z).endVertex();
        //    buffer.pos(point1.x, point1.y, point1.z).endVertex();
        //    buffer.pos(point2.x, point2.y, point2.z).endVertex();
        //    buffer.pos(point3.x, point3.y, point3.z).endVertex();
        //    buffer.pos(point0.x, point0.y, point0.z).endVertex();
        //    Tessellator.getInstance().draw();
        //
        //    buffer = Tessellator.getInstance().getBuffer();
        //    buffer.begin(3, DefaultVertexFormats.POSITION);
        //    matrix.transform(point0);
        //    buffer.pos(point0.x, point0.y, point0.z).endVertex();
        //    matrix.transform(point1);
        //    buffer.pos(point1.x, point1.y, point1.z).endVertex();
        //    matrix.transform(point2);
        //    buffer.pos(point2.x, point2.y, point2.z).endVertex();
        //    matrix.transform(point3);
        //    buffer.pos(point3.x, point3.y, point3.z).endVertex();
        //    buffer.pos(point0.x, point0.y, point0.z).endVertex();
        //    Tessellator.getInstance().draw();
        //    GlStateManager.enableTexture2D();
        //}
        //catch (SingularMatrixException e) {
        //}

        Minecraft mc = Minecraft.getMinecraft();
        RenderItem itemRenderer = mc.getRenderItem();

        GlStateManager.translate(.5f, .5f, .5f);
        int rotY = (int) direction.rotY;
        if (rotY == 90 || rotY == 270) {
            rotY += 180;
        }

        GlStateManager.rotate(180 - rotY, 0, 1, 0);
        GlStateManager.rotate(90 - direction.rotX, 1, 0, 0);
        GlStateManager.scale(.21f, .21f, .21f);
        GlStateManager.translate(0f, 0f, 2.05f);
        GlStateManager.scale(-1, 1, -0.001);

        for (int i = 0, j = -1; j < 2; j++) {
            for (int k = -1; k < 2; k++) {

                ItemStack stack = te.handler.getStackInSlot(i);
                ++i;
                if (stack.isEmpty()) {
                    continue;
                }

                GlStateManager.pushMatrix();

                if (j == 0 && k == 0 && itemCount == 1) {
                    GlStateManager.scale(2.5f, 2.5f, 1);
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(4f, 4f, 1);
                } else {
                    GlStateManager.pushMatrix();
                    GlStateManager.scale(1.92f, 1.92f, 1);
                }
                GlStateManager.rotate(17.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.rotate(105.0F, -1.0F, 0.0F, 0.0F);
                RenderHelper.enableStandardItemLighting();
                GlStateManager.popMatrix();

                int ambLight = getWorld().getCombinedLight(te.getPos(), 0);
                int lu = ambLight % 65536;
                int lv = ambLight / 65536;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) lu / 1.0F, (float) lv / 1.0F);

                // TileEntitySkullRenderer alters both of these options on, but does not restore them.
                GlStateManager.enableCull();
                GlStateManager.disableRescaleNormal();

                // GL_POLYGON_OFFSET is used to offset flat icons toward the viewer (-Z) in screen space,
                // so they always appear on top of the drawer's front space.
                GlStateManager.enablePolygonOffset();
                GlStateManager.doPolygonOffset(-1, -1);

                // DIRTY HACK: Fool GlStateManager into thinking GL_RESCALE_NORMAL is enabled, but disable
                // it using popAttrib This prevents RenderItem from enabling it again.
                //
                // Normals are transformed by the inverse of the modelview and projection matrices that
                // excludes the translate terms. When put through the extreme Z scale used to flatten the
                // block, this makes them point away from the drawer face at a very sharp angle. These
                // normals are no longer unit scale (normalized), and normalizing them via
                // GL_RESCALE_NORMAL causes a loss of precision that results in the normals pointing
                // directly away from the face, which is visible as the block faces having identical
                // (dark) shading.

                GlStateManager.enableRescaleNormal();
                GlStateManager.disableRescaleNormal();
                GlStateManager.pushAttrib();
                GlStateManager.enableRescaleNormal();
                GlStateManager.popAttrib();

                IBakedModel bakedmodel = itemRenderer.getItemModelWithOverrides(stack, getWorld(), null);
                if (!bakedmodel.isGui3d() || stack.getItem() == Item.getItemFromBlock(MaidBlocks.GRID)) {
                    GlStateManager.disableLighting();
                } else {
                    GlStateManager.enableLighting();
                }
                GlStateManager.pushMatrix();
                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
                GlStateManager.enableRescaleNormal();
                GlStateManager.enableAlpha();
                GlStateManager.alphaFunc(516, 0.1F);
                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.translate(k, -j, 0);
                bakedmodel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(bakedmodel, ItemCameraTransforms.TransformType.GUI, false);
                itemRenderer.renderItem(stack, bakedmodel);
                GlStateManager.disableAlpha();
                GlStateManager.disableRescaleNormal();
                GlStateManager.disableLighting();
                GlStateManager.popMatrix();
                mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
                mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();

                GlStateManager.disableBlend(); // Clean up after RenderItem
                GlStateManager.enableAlpha(); // Restore world render state after RenderItem

                GlStateManager.disablePolygonOffset();
                GlStateManager.popMatrix();
            }
        }

        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableNormalize();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
        GlStateManager.color(1, 1, 1, 1);
    }
}
