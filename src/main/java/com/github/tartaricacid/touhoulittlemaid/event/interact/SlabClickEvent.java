package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBackpack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemSmartSlab;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class SlabClickEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        ItemStack itemstack = event.getStack();
        EntityPlayer player = event.getPlayer();
        EntityMaid maid = (EntityMaid) event.getMaid();
        World world = event.getWorld();

        if (itemstack.getItem() == MaidItems.SMART_SLAB && itemstack.getMetadata() == 1) {
            if (!world.isRemote && maid.isEntityAlive() && maid.isOwner(player)) {
                if (maid.getBackLevel() != EntityMaid.EnumBackPackLevel.EMPTY) {
                    EntityBackpack backpack = new EntityBackpack(world, maid.getBackLevel());
                    IItemHandlerModifiable maidBackpackInv = maid.getAllBackpackInv();
                    ItemStackHandler backpackInv = backpack.getInv();
                    for (int i = 0; i < maidBackpackInv.getSlots(); i++) {
                        backpackInv.setStackInSlot(i, maidBackpackInv.getStackInSlot(i));
                    }
                    backpack.setPositionAndRotation(maid.posX, maid.posY, maid.posZ, maid.renderYawOffset, maid.rotationPitch);
                    world.spawnEntity(backpack);
                }
                NBTTagCompound slabTag = new NBTTagCompound();
                if (itemstack.hasTagCompound()) {
                    slabTag = itemstack.getTagCompound();
                }
                NBTTagCompound maidTag = new NBTTagCompound();
                maid.writeEntityToNBT(maidTag);
                // 剔除背包数据
                maidTag.removeTag(EntityMaid.NBT.BACKPACK_LEVEL.getName());
                maidTag.removeTag(EntityMaid.NBT.MAID_SMALL_BACKPACK.getName());
                maidTag.removeTag(EntityMaid.NBT.MAID_MIDDLE_BACKPACK.getName());
                maidTag.removeTag(EntityMaid.NBT.MAID_BIG_BACKPACK.getName());
                slabTag.setTag(ItemSmartSlab.MAID_INFO_TAG, maidTag);
                itemstack.setTagCompound(slabTag);
                itemstack.setItemDamage(2);
                if (maid.hasCustomName()) {
                    itemstack.setStackDisplayName(maid.getCustomNameTag());
                }
                maid.setDead();
            }
            maid.spawnExplosionParticle();

            // 播放音效
            maid.playSound(SoundEvents.ENTITY_PLAYER_SPLASH, 1.0F, world.rand.nextFloat() * 0.1F + 0.9F);
            event.setCanceled(true);
        }
    }
}
