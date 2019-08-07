package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import com.github.tartaricacid.touhoulittlemaid.util.MaidRayTraceHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.github.tartaricacid.touhoulittlemaid.item.ItemPhoto.NBT.MAID_INFO;

/**
 * @author TartaricAcid
 * @date 2019/8/6 15:41
 **/
public class ItemCamera extends Item {
    public ItemCamera() {
        setTranslationKey(TouhouLittleMaid.MOD_ID + ".camera");
        setMaxStackSize(1);
        setCreativeTab(MaidItems.TABS);
        setMaxDamage(150);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand handIn) {
        if (handIn == EnumHand.MAIN_HAND) {
            int searchDistance = 8;
            ItemStack camera = playerIn.getHeldItem(handIn);
            Optional<EntityMaid> result = MaidRayTraceHelper.rayTraceMaid(playerIn, searchDistance);
            if (result.isPresent()) {
                EntityMaid maid = result.get();
                if (!worldIn.isRemote) {
                    ItemStack photo = new ItemStack(MaidItems.PHOTO);
                    NBTTagCompound photoTag = new NBTTagCompound();
                    NBTTagCompound maidTag = new NBTTagCompound();
                    maid.writeEntityToNBT(maidTag);
                    photoTag.setTag(MAID_INFO.getNbtName(), maidTag);
                    photo.setTagCompound(photoTag);
                    InventoryHelper.spawnItemStack(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, photo);
                }
                maid.spawnExplosionParticle();
                maid.setDead();
                playerIn.playSound(MaidSoundEvent.CAMERA_USE, 1.0f, 1.0f);
                playerIn.getCooldownTracker().setCooldown(this, 20);
                camera.damageItem(1, playerIn);
                return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
