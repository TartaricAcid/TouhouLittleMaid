package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.layers;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRender;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySkullRenderer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/12/31 18:03
 **/
public class LayerMaidCustomHead implements LayerRenderer<EntityMaid> {
    private static final String SKULL_OWNER_TAG = "SkullOwner";
    private final EntityMaidRender maidRender;

    public LayerMaidCustomHead(EntityMaidRender maidRender) {
        this.maidRender = maidRender;
    }

    @Override
    public void doRenderLayer(@Nonnull EntityMaid maid, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        if (!maid.isShowHelmet()) {
            return;
        }
        ItemStack head = maid.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        EntityModelJson modelJson = (EntityModelJson) this.maidRender.getMainModel();
        if (!head.isEmpty() && maidRender.getMainInfo().isShowCustomHead() && modelJson.hasHead()) {
            Item item = head.getItem();
            Minecraft minecraft = Minecraft.getMinecraft();
            GlStateManager.pushMatrix();

            modelJson.postRenderCustomHead(0.0625F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (item == Items.SKULL) {
                GlStateManager.scale(1.05F, -1.05F, -1.05F);
                TileEntitySkullRenderer.instance.renderSkull(-0.5F, 0.0F, -0.5F, EnumFacing.UP, 180.0F, head.getMetadata(), getSkullGameProfile(head), -1, limbSwing);
            } else if (!(item instanceof ItemArmor) || ((ItemArmor) item).getEquipmentSlot() != EntityEquipmentSlot.HEAD) {
                GlStateManager.translate(0.0F, -0.25F, 0.0F);
                GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                GlStateManager.scale(0.625F, -0.625F, -0.625F);
                minecraft.getItemRenderer().renderItem(maid, head, ItemCameraTransforms.TransformType.HEAD);
            }

            GlStateManager.popMatrix();
        }
    }

    @SuppressWarnings("all")
    private GameProfile getSkullGameProfile(ItemStack head) {
        GameProfile gameprofile = null;
        if (head.hasTagCompound()) {
            NBTTagCompound nbttagcompound = head.getTagCompound();
            if (nbttagcompound.hasKey(SKULL_OWNER_TAG, Constants.NBT.TAG_COMPOUND)) {
                gameprofile = NBTUtil.readGameProfileFromNBT(nbttagcompound.getCompoundTag(SKULL_OWNER_TAG));
            } else if (nbttagcompound.hasKey(SKULL_OWNER_TAG, Constants.NBT.TAG_STRING)) {
                String skullOwner = nbttagcompound.getString(SKULL_OWNER_TAG);
                if (!StringUtils.isBlank(skullOwner)) {
                    gameprofile = TileEntitySkull.updateGameProfile(new GameProfile(null, skullOwner));
                    nbttagcompound.setTag(SKULL_OWNER_TAG, NBTUtil.writeGameProfile(new NBTTagCompound(), gameprofile));
                }
            }
        }
        return gameprofile;
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
