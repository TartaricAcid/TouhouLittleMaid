package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Map;

public class GeckoLayerMaidBipedHead<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private static final String SKULL_OWNER_TAG = "SkullOwner";
    private final GeckoEntityMaidRenderer maidRenderer;
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;

    public GeckoLayerMaidBipedHead(GeckoEntityMaidRenderer entityRendererIn, EntityModelSet modelSet) {
        super(entityRendererIn);
        this.maidRenderer = entityRendererIn;
        this.skullModels = SkullBlockRenderer.createSkullRenderers(modelSet);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityLivingBaseIn instanceof EntityMaid maid && this.maidRenderer.getGeoModel() != null) {
            ItemStack head = maid.getItemBySlot(EquipmentSlot.HEAD);
            GeoModel geoModel = this.maidRenderer.getGeoModel();
            if (!head.isEmpty() && maidRenderer.getMainInfo().isShowCustomHead() && !geoModel.headBones.isEmpty()) {
                Item item = head.getItem();
                poseStack.pushPose();
                this.translateToHead(poseStack, geoModel);
                if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof AbstractSkullBlock) {
                    AbstractSkullBlock skullBlock = (AbstractSkullBlock) ((BlockItem) item).getBlock();
                    poseStack.scale(-1.1875F, 1.1875F, -1.1875F);
                    GameProfile gameprofile = getSkullGameProfile(head);
                    poseStack.translate(-0.5D, 0.0D, -0.5D);
                    SkullBlock.Type type = skullBlock.getType();
                    SkullModelBase modelBase = this.skullModels.get(type);
                    RenderType rendertype = SkullBlockRenderer.getRenderType(type, gameprofile);
                    SkullBlockRenderer.renderSkull(null, 180.0F, 0.0F, poseStack, bufferIn, packedLightIn, modelBase, rendertype);
                }
                poseStack.popPose();
            }
            ItemStack stack = maid.getMaidInv().getStackInSlot(5);
            if (stack.getItem() instanceof BlockItem && maidRenderer.getMainInfo().isShowCustomHead() && !geoModel.headBones.isEmpty()) {
                Block block = ((BlockItem) stack.getItem()).getBlock();
                if (block instanceof IPlantable && !(block instanceof DoublePlantBlock)) {
                    BlockState plant = ((IPlantable) block).getPlant(maid.level, maid.blockPosition());
                    poseStack.pushPose();
                    this.translateToHead(poseStack, geoModel);
                    poseStack.scale(-0.8F, 0.8F, -0.8F);
                    poseStack.translate(-0.5, 0.625, -0.5);
                    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(plant, poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
                    poseStack.popPose();
                }
            }
        }
    }

    @Nullable
    private GameProfile getSkullGameProfile(ItemStack head) {
        if (head.hasTag()) {
            CompoundTag nbt = head.getTag();
            if (nbt != null && nbt.contains(SKULL_OWNER_TAG, Tag.TAG_COMPOUND)) {
                return NbtUtils.readGameProfile(nbt.getCompound(SKULL_OWNER_TAG));
            }
        }
        return null;
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
