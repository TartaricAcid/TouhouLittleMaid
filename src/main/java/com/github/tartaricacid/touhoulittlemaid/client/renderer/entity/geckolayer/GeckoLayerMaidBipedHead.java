package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.compat.simplehats.SimpleHatsCompat;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.common.IPlantable;

import javax.annotation.Nullable;
import java.util.Map;

public class GeckoLayerMaidBipedHead<T extends Mob> extends GeoLayerRenderer<T, GeckoEntityMaidRenderer<T>> {
    private static final String SKULL_OWNER_TAG = "SkullOwner";
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;

    public GeckoLayerMaidBipedHead(GeckoEntityMaidRenderer<T> entityRendererIn, EntityModelSet modelSet) {
        super(entityRendererIn);
        this.skullModels = SkullBlockRenderer.createSkullRenderers(modelSet);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        var animatableEntity = this.entityRenderer.getAnimatableEntity(entity);
        if (animatableEntity.getCurrentModel() != null) {
            ItemStack head = entity.getItemBySlot(EquipmentSlot.HEAD);
            AnimatedGeoModel geoModel = animatableEntity.getCurrentModel();
            boolean allowRenderHead = animatableEntity.getMaidInfo().isShowCustomHead() && !geoModel.headBones().isEmpty();
            if (!allowRenderHead) {
                return;
            }

            // 渲染头盔栏的
            if (!head.isEmpty()) {
                Item item = head.getItem();
                poseStack.pushPose();
                RenderUtils.prepMatrixForLocator(poseStack, geoModel.headBones());
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

            IMaid maid = IMaid.convert(entity);
            if (maid == null) {
                return;
            }

            // 渲染女仆背部的
            ItemStack stack = maid.getBackpackShowItem();
            // 不做限制，任意方块都可以显示
            if (stack.getItem() instanceof BlockItem blockItem) {
                Block block = blockItem.getBlock();
                BlockState blockState;
                if (block instanceof IPlantable iPlantable && !(block instanceof DoublePlantBlock)) {
                    blockState = iPlantable.getPlant(entity.level, entity.blockPosition());
                } else {
                    blockState = block.defaultBlockState();
                }
                poseStack.pushPose();
                RenderUtils.prepMatrixForLocator(poseStack, geoModel.headBones());
                poseStack.scale(-0.8F, 0.8F, -0.8F);
                poseStack.translate(-0.5, 0.625, -0.5);
                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockState, poseStack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, null);
                poseStack.popPose();
            } else {
                SimpleHatsCompat.renderGeckoHat(poseStack, bufferIn, packedLightIn, entity, stack, geoModel.headBones());
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
}
