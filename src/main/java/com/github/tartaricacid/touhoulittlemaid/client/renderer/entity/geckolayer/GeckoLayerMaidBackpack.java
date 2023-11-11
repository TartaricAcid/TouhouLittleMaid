package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackBigModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackMiddleModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.MaidBackpackSmallModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
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
    private static final ResourceLocation SMALL = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_small.png");
    private static final ResourceLocation MIDDLE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_middle.png");
    private static final ResourceLocation BIG = new ResourceLocation(TouhouLittleMaid.MOD_ID, "textures/entity/maid_backpack_big.png");
    private final GeckoEntityMaidRenderer renderer;
    private final EntityModel<EntityMaid> smallModel = new MaidBackpackSmallModel();
    private final EntityModel<EntityMaid> middleModel = new MaidBackpackMiddleModel();
    private final EntityModel<EntityMaid> bigModel = new MaidBackpackBigModel();

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
                switch (maid.getBackpackLevel()) {
                    case BackpackLevel.SMALL:
                        renderColoredCutoutModel(smallModel, SMALL, matrixStackIn, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f);
                        return;
                    case BackpackLevel.MIDDLE:
                        renderColoredCutoutModel(middleModel, MIDDLE, matrixStackIn, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f);
                        return;
                    case BackpackLevel.BIG:
                        renderColoredCutoutModel(bigModel, BIG, matrixStackIn, bufferIn, packedLightIn, maid, 1.0f, 1.0f, 1.0f);
                    case BackpackLevel.EMPTY:
                    default:
                }
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
