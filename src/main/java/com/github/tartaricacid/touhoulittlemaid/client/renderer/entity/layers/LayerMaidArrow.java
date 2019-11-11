package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * @author TartaricAcid
 * @date 2019/11/11 21:20
 **/
@SideOnly(Side.CLIENT)
public class LayerMaidArrow implements LayerRenderer<EntityLivingBase> {
    private final RenderLivingBase<?> renderer;

    public LayerMaidArrow(RenderLivingBase<?> rendererIn) {
        this.renderer = rendererIn;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityLivingBase entityLivingBase, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        int i = entityLivingBase.getArrowCountInEntity();

        if (i > 0) {
            Entity entity = new EntityTippedArrow(entityLivingBase.world, entityLivingBase.posX, entityLivingBase.posY, entityLivingBase.posZ);
            Random random = new Random((long) entityLivingBase.getEntityId());
            RenderHelper.disableStandardItemLighting();

            for (int j = 0; j < i; ++j) {
                GlStateManager.pushMatrix();
                ModelRenderer modelrenderer = this.renderer.getMainModel().getRandomModelBox(random);
                if (modelrenderer.cubeList.size() > 0) {
                    ModelBox modelbox = modelrenderer.cubeList.get(random.nextInt(modelrenderer.cubeList.size()));
                    modelrenderer.postRender(0.0625F);
                    float xRandom = random.nextFloat();
                    float yRandom = random.nextFloat();
                    float zRandom = random.nextFloat();
                    float xPos = (modelbox.posX1 + (modelbox.posX2 - modelbox.posX1) * xRandom) / 16.0F;
                    float yPos = (modelbox.posY1 + (modelbox.posY2 - modelbox.posY1) * yRandom) / 16.0F;
                    float zPos = (modelbox.posZ1 + (modelbox.posZ2 - modelbox.posZ1) * zRandom) / 16.0F;
                    GlStateManager.translate(xPos, yPos, zPos);
                    xRandom = xRandom * 2.0F - 1.0F;
                    yRandom = yRandom * 2.0F - 1.0F;
                    zRandom = zRandom * 2.0F - 1.0F;
                    xRandom = xRandom * -1.0F;
                    yRandom = yRandom * -1.0F;
                    zRandom = zRandom * -1.0F;
                    float xzSqrt = MathHelper.sqrt(xRandom * xRandom + zRandom * zRandom);
                    entity.rotationYaw = (float) (Math.atan2((double) xRandom, (double) zRandom) * (180D / Math.PI));
                    entity.rotationPitch = (float) (Math.atan2((double) yRandom, (double) xzSqrt) * (180D / Math.PI));
                    entity.prevRotationYaw = entity.rotationYaw;
                    entity.prevRotationPitch = entity.rotationPitch;
                    this.renderer.getRenderManager().renderEntity(entity, 0.0D, 0.0D, 0.0D, 0.0F, partialTicks, false);
                }
                GlStateManager.popMatrix();
            }

            RenderHelper.enableStandardItemLighting();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
