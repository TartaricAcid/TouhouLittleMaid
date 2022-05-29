package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;

import javax.annotation.Nullable;
import java.util.Map;

public class LayerMaidBipedHead extends RenderLayer<EntityMaid, BedrockModel<EntityMaid>> {
    private static final String SKULL_OWNER_TAG = "SkullOwner";
    private final EntityMaidRenderer maidRenderer;
    private final Map<SkullBlock.Type, SkullModelBase> skullModels;

    public LayerMaidBipedHead(EntityMaidRenderer maidRenderer, EntityModelSet modelSet) {
        super(maidRenderer);
        this.maidRenderer = maidRenderer;
        this.skullModels = SkullBlockRenderer.createSkullRenderers(modelSet);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack head = maid.getItemBySlot(EquipmentSlot.HEAD);
        if (!head.isEmpty() && maidRenderer.getMainInfo().isShowCustomHead() && getParentModel().hasHead()) {
            Item item = head.getItem();
            poseStack.pushPose();
            this.getParentModel().getHead().translateAndRotate(poseStack);
            if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof AbstractSkullBlock) {
                AbstractSkullBlock skullBlock = (AbstractSkullBlock) ((BlockItem) item).getBlock();
                poseStack.scale(1.1875F, -1.1875F, -1.1875F);
                GameProfile gameprofile = getSkullGameProfile(head);
                poseStack.translate(-0.5D, 0.0D, -0.5D);
                SkullBlock.Type type = skullBlock.getType();
                SkullModelBase modelBase = this.skullModels.get(type);
                RenderType rendertype = SkullBlockRenderer.getRenderType(type, gameprofile);
                SkullBlockRenderer.renderSkull(null, 180.0F, 0.0F, poseStack, bufferIn, packedLightIn, modelBase, rendertype);
            } else if (!(item instanceof ArmorItem) || ((ArmorItem) item).getSlot() != EquipmentSlot.HEAD) {
                poseStack.translate(0.0D, -0.25D, 0.0D);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
                poseStack.scale(0.625F, -0.625F, -0.625F);
                Minecraft.getInstance().getItemInHandRenderer().renderItem(maid, head, ItemTransforms.TransformType.HEAD, false, poseStack, bufferIn, packedLightIn);
            }
            poseStack.popPose();
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
