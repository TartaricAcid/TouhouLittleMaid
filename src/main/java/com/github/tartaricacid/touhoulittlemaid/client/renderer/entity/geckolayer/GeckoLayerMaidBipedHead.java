package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.GeckoEntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoBone;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.RenderUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.SkullTileEntity;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public class GeckoLayerMaidBipedHead<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private static final String SKULL_OWNER_TAG = "SkullOwner";
    private final GeckoEntityMaidRenderer maidRenderer;

    public GeckoLayerMaidBipedHead(GeckoEntityMaidRenderer entityRendererIn) {
        super(entityRendererIn);
        this.maidRenderer = entityRendererIn;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, LivingEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityLivingBaseIn instanceof EntityMaid && this.maidRenderer.getGeoModel() != null) {
            EntityMaid maid = (EntityMaid) entityLivingBaseIn;
            ItemStack head = maid.getItemBySlot(EquipmentSlotType.HEAD);
            GeoModel geoModel = this.maidRenderer.getGeoModel();
            if (!head.isEmpty() && maidRenderer.getMainInfo().isShowCustomHead() && !geoModel.headBones.isEmpty()) {
                Item item = head.getItem();
                matrixStackIn.pushPose();
                this.translateToHead(matrixStackIn, geoModel);
                if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof AbstractSkullBlock) {
                    AbstractSkullBlock skullBlock = (AbstractSkullBlock) ((BlockItem) item).getBlock();
                    matrixStackIn.scale(-1.1875F, 1.1875F, -1.1875F);
                    GameProfile gameprofile = getSkullGameProfile(head);
                    matrixStackIn.translate(-0.5D, 0.0D, -0.5D);
                    SkullTileEntityRenderer.renderSkull(null, 180.0F, skullBlock.getType(), gameprofile, limbSwing, matrixStackIn, bufferIn, packedLightIn);
                }
                matrixStackIn.popPose();
            }
            ItemStack stack = maid.getMaidInv().getStackInSlot(5);
            if (stack.getItem() instanceof BlockItem && maidRenderer.getMainInfo().isShowCustomHead() && !geoModel.headBones.isEmpty()) {
                Block block = ((BlockItem) stack.getItem()).getBlock();
                if (block instanceof IPlantable && !(block instanceof DoublePlantBlock)) {
                    BlockState plant = ((IPlantable) block).getPlant(maid.level, maid.blockPosition());
                    matrixStackIn.pushPose();
                    this.translateToHead(matrixStackIn, geoModel);
                    matrixStackIn.scale(-0.8F, 0.8F, -0.8F);
                    matrixStackIn.translate(-0.5, 0.625, -0.5);
                    Minecraft.getInstance().getBlockRenderer().renderSingleBlock(plant, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
                    matrixStackIn.popPose();
                }
            }
        }
    }

    @Nullable
    private GameProfile getSkullGameProfile(ItemStack head) {
        GameProfile gameProfile = null;
        if (head.hasTag()) {
            CompoundNBT nbt = head.getTag();
            if (nbt != null) {
                if (nbt.contains(SKULL_OWNER_TAG, Constants.NBT.TAG_COMPOUND)) {
                    gameProfile = NBTUtil.readGameProfile(nbt.getCompound(SKULL_OWNER_TAG));
                } else if (nbt.contains(SKULL_OWNER_TAG, Constants.NBT.TAG_STRING)) {
                    String skullOwner = nbt.getString(SKULL_OWNER_TAG);
                    if (!StringUtils.isBlank(skullOwner)) {
                        gameProfile = SkullTileEntity.updateGameprofile(new GameProfile(null, skullOwner));
                        if (gameProfile != null) {
                            nbt.put(SKULL_OWNER_TAG, NBTUtil.writeGameProfile(new CompoundNBT(), gameProfile));
                        }
                    }
                }
            }
        }
        return gameProfile;
    }

    protected void translateToHead(MatrixStack matrixStack, GeoModel geoModel) {
        int size = geoModel.headBones.size();
        for (int i = 0; i < size - 1; i++) {
            RenderUtils.prepMatrixForBone(matrixStack, geoModel.headBones.get(i));
        }
        GeoBone lastBone = geoModel.headBones.get(size - 1);
        RenderUtils.translateMatrixToBone(matrixStack, lastBone);
        RenderUtils.translateToPivotPoint(matrixStack, lastBone);
        RenderUtils.rotateMatrixAroundBone(matrixStack, lastBone);
        RenderUtils.scaleMatrixForBone(matrixStack, lastBone);
    }
}
