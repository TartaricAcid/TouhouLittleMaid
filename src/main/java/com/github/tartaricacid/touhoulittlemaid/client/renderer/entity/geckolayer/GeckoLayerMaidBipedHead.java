package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ResolvableProfile;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Map;

public class GeckoLayerMaidBipedHead<T extends Mob & IAnimatable> extends GeoLayerRenderer<T> {
    private final GeckoEntityMaidRenderer maidRenderer;
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;

    public GeckoLayerMaidBipedHead(GeckoEntityMaidRenderer entityRendererIn, EntityModelSet modelSet) {
        super(entityRendererIn);
        this.maidRenderer = entityRendererIn;
        this.skullModels = SkullBlockRenderer.createSkullRenderers(modelSet);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (this.maidRenderer.getGeoModel() != null) {
            ItemStack head = entity.getItemBySlot(EquipmentSlot.HEAD);
            GeoModel geoModel = this.maidRenderer.getGeoModel();
            if (!head.isEmpty() && maidRenderer.getMainInfo().isShowCustomHead() && !geoModel.headBones.isEmpty()) {
                Item item = head.getItem();
                poseStack.pushPose();
                this.translateToHead(poseStack, geoModel);
                if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof AbstractSkullBlock skullBlock) {
                    poseStack.scale(-1.1875F, 1.1875F, -1.1875F);
                    ResolvableProfile resolvableProfile = head.get(DataComponents.PROFILE);
                    poseStack.translate(-0.5D, 0.0D, -0.5D);
                    SkullBlock.Type type = skullBlock.getType();
                    SkullModelBase modelBase = this.skullModels.get(type);
                    RenderType rendertype = SkullBlockRenderer.getRenderType(type, resolvableProfile);
                    SkullBlockRenderer.renderSkull(null, 180.0F, 0.0F, poseStack, bufferIn, packedLightIn, modelBase, rendertype);
                }
                poseStack.popPose();
            }
            IMaid maid = IMaid.convert(entity);
            if (maid == null) {
                return;
            }
            ItemStack stack = maid.getBackpackShowItem();
            if (stack.getItem() instanceof BlockItem && maidRenderer.getMainInfo().isShowCustomHead() && !geoModel.headBones.isEmpty()) {
                Block block = ((BlockItem) stack.getItem()).getBlock();
                BlockState blockState = block.defaultBlockState();
                //TODO 由于麻将删除了IPlantable，先借用Tags
                if (blockState.is(BlockTags.SMALL_FLOWERS) && blockState.is(Blocks.SHORT_GRASS)) {
                    poseStack.pushPose();
                    this.translateToHead(poseStack, geoModel);
                    poseStack.scale(-0.8F, 0.8F, -0.8F);
                    poseStack.translate(-0.5, 0.625, -0.5);
                    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockState, poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
                    poseStack.popPose();
                }
            }
        }
    }

    protected void translateToHead(PoseStack poseStack, GeoModel geoModel) {
        int size = geoModel.headBones.size();
        for (int i = 0; i < size - 1; i++) {
            RenderUtils.prepMatrixForBone(poseStack, geoModel.headBones.get(i));
        }
        GeoBone lastBone = geoModel.headBones.get(size - 1);
        RenderUtils.translateMatrixToBone(poseStack, lastBone);
        RenderUtils.translateToPivotPoint(poseStack, lastBone);
        RenderUtils.rotateMatrixAroundBone(poseStack, lastBone);
        RenderUtils.scaleMatrixForBone(poseStack, lastBone);
    }
}
