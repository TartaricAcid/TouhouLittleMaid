package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.backpack.IMaidBackpack;
import com.github.tartaricacid.touhoulittlemaid.client.model.backpack.BigBackpackBModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.backpack.MaidBackpackMiddleModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.backpack.SmallBackpackModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.BackpackManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class GeckoLayerMaidBackpack<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private final GeckoEntityMaidRenderer renderer;

    public GeckoLayerMaidBackpack(GeckoEntityMaidRenderer entityRendererIn) {
        super(entityRendererIn);
        this.renderer = entityRendererIn;
    }

    protected static <T extends LivingEntity> void renderColoredCutoutModel(EntityModel<T> pModel, ResourceLocation pTextureLocation, MatrixStack pMatrixStack, IRenderTypeBuffer pBuffer, int pPackedLight, T pEntity, float pRed, float pGreen, float pBlue) {
        IVertexBuilder ivertexbuilder = pBuffer.getBuffer(RenderType.entityCutoutNoCull(pTextureLocation));
        pModel.renderToBuffer(pMatrixStack, ivertexbuilder, pPackedLight, LivingRenderer.getOverlayCoords(pEntity, 0.0F), pRed, pGreen, pBlue, 1.0F);
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity livingEntity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (livingEntity instanceof EntityMaid && this.entityRenderer.getGeoModel() != null) {
            EntityMaid maid = (EntityMaid) livingEntity;
            if (!renderer.getMainInfo().isShowBackpack() || !InGameMaidConfig.INSTANCE.isShowBackpack() || maid.isSleeping() || maid.isInvisible()) {
                return;
            }
            GeoModel geoModel = this.entityRenderer.getGeoModel();
            if (!geoModel.backpackBones.isEmpty()) {
                translateToBackpack(matrixStackIn, geoModel);
                matrixStackIn.translate(0, 1, 0.25);
                matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(180));
                IMaidBackpack type = maid.getMaidBackpackType();
                BackpackManager.findBackpackModel(type.getId()).ifPresent(pair -> renderColoredCutoutModel(pair.getLeft(), pair.getRight(), matrixStackIn, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f));
            }
        }
    }

    protected void translateToBackpack(MatrixStack matrixStackIn, GeoModel geoModel) {
        int size = geoModel.backpackBones.size();
        for (int i = 0; i < size - 1; i++) {
            RenderUtils.prepMatrixForBone(matrixStackIn, geoModel.backpackBones.get(i));
        }
        GeoBone lastBone = geoModel.backpackBones.get(size - 1);
        RenderUtils.translateMatrixToBone(matrixStackIn, lastBone);
        RenderUtils.translateToPivotPoint(matrixStackIn, lastBone);
        RenderUtils.rotateMatrixAroundBone(matrixStackIn, lastBone);
        RenderUtils.scaleMatrixForBone(matrixStackIn, lastBone);
    }
}
