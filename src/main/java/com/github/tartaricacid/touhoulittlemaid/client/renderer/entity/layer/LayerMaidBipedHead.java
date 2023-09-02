package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layer;

import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRenderer;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.SkullTileEntityRenderer;
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

public class LayerMaidBipedHead extends LayerRenderer<EntityMaid, BedrockModel<EntityMaid>> {
    private static final String SKULL_OWNER_TAG = "SkullOwner";
    private final EntityMaidRenderer maidRenderer;

    public LayerMaidBipedHead(EntityMaidRenderer maidRenderer) {
        super(maidRenderer);
        this.maidRenderer = maidRenderer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ItemStack head = maid.getItemBySlot(EquipmentSlotType.HEAD);
        if (!head.isEmpty() && maidRenderer.getMainInfo().isShowCustomHead() && getParentModel().hasHead()) {
            Item item = head.getItem();
            matrixStackIn.pushPose();
            this.getParentModel().getHead().translateAndRotate(matrixStackIn);
            if (item instanceof BlockItem && ((BlockItem) item).getBlock() instanceof AbstractSkullBlock) {
                AbstractSkullBlock skullBlock = (AbstractSkullBlock) ((BlockItem) item).getBlock();
                matrixStackIn.scale(1.1875F, -1.1875F, -1.1875F);
                GameProfile gameprofile = getSkullGameProfile(head);
                matrixStackIn.translate(-0.5D, 0.0D, -0.5D);
                SkullTileEntityRenderer.renderSkull(null, 180.0F, skullBlock.getType(), gameprofile, limbSwing, matrixStackIn, bufferIn, packedLightIn);
            }
            matrixStackIn.popPose();
        }

        ItemStack stack = maid.getMaidInv().getStackInSlot(5);
        if (stack.getItem() instanceof BlockItem && maidRenderer.getMainInfo().isShowCustomHead() && getParentModel().hasHead()) {
            Block block = ((BlockItem) stack.getItem()).getBlock();
            if (block instanceof IPlantable && !(block instanceof DoublePlantBlock)) {
                BlockState plant = ((IPlantable) block).getPlant(maid.level, maid.blockPosition());
                matrixStackIn.pushPose();
                this.getParentModel().getHead().translateAndRotate(matrixStackIn);
                matrixStackIn.scale(0.8F, -0.8F, -0.8F);
                matrixStackIn.translate(-0.5, 0.625, -0.5);
                Minecraft.getInstance().getBlockRenderer().renderSingleBlock(plant, matrixStackIn, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY);
                matrixStackIn.popPose();
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
}
