package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBackpack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.init.MaidSoundEvent;
import com.github.tartaricacid.touhoulittlemaid.util.MaidRayTraceHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
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
        setCreativeTab(MaidItems.MAIN_TABS);
        setMaxDamage(50);
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
                if (!worldIn.isRemote && maid.isEntityAlive() && maid.isOwner(playerIn)) {
                    if (maid.getBackLevel() != EntityMaid.EnumBackPackLevel.EMPTY) {
                        spawnMaidBackpack(worldIn, maid);
                    }
                    spawnMaidPhoto(worldIn, maid, playerIn);
                    maid.setDead();
                    playerIn.getCooldownTracker().setCooldown(this, 20);
                    camera.damageItem(1, playerIn);
                }
                maid.spawnExplosionParticle();
                playerIn.playSound(MaidSoundEvent.CAMERA_USE, 1.0f, 1.0f);
                return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private void spawnMaidBackpack(World worldIn, EntityMaid maid) {
        EntityBackpack backpack = new EntityBackpack(worldIn, maid.getBackLevel());
        IItemHandlerModifiable maidBackpackInv = maid.getAllBackpackInv();
        ItemStackHandler backpackInv = backpack.getInv();
        for (int i = 0; i < maidBackpackInv.getSlots(); i++) {
            backpackInv.setStackInSlot(i, maidBackpackInv.getStackInSlot(i));
        }
        backpack.setPositionAndRotation(maid.posX, maid.posY, maid.posZ, maid.renderYawOffset, maid.rotationPitch);
        worldIn.spawnEntity(backpack);
    }

    private void spawnMaidPhoto(World worldIn, EntityMaid maid, EntityPlayer playerIn) {
        ItemStack photo = new ItemStack(MaidItems.PHOTO);
        NBTTagCompound photoTag = new NBTTagCompound();
        NBTTagCompound maidTag = new NBTTagCompound();
        maid.writeEntityToNBT(maidTag);
        // 剔除背包数据
        removeMaidBackpackTagData(maidTag);
        photoTag.setTag(MAID_INFO.getNbtName(), maidTag);
        photo.setTagCompound(photoTag);
        InventoryHelper.spawnItemStack(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, photo);
    }

    private void removeMaidBackpackTagData(NBTTagCompound nbt) {
        nbt.removeTag(EntityMaid.NBT.BACKPACK_LEVEL.getName());
        nbt.removeTag(EntityMaid.NBT.MAID_SMALL_BACKPACK.getName());
        nbt.removeTag(EntityMaid.NBT.MAID_MIDDLE_BACKPACK.getName());
        nbt.removeTag(EntityMaid.NBT.MAID_BIG_BACKPACK.getName());
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        // 返回 true，阻止打开女仆 GUI
        if (stack.getItem() == this && target.isEntityAlive() && target instanceof EntityMaid && ((EntityMaid) target).isOwner(playerIn)) {
            onItemRightClick(playerIn.world, playerIn, hand);
            return true;
        }
        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.DARK_GREEN + I18n.format("tooltips.touhou_little_maid.camera.desc"));
    }
}
