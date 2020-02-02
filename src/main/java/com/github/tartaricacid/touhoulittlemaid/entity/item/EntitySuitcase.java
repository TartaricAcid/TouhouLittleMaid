package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2020/1/20 14:58
 **/
public class EntitySuitcase extends AbstractEntityTrolley {
    private static final String MAIN_INV_TAG = "MainInventory";
    private final ItemStackHandler mainInv = new ItemStackHandler(27);

    public EntitySuitcase(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.8f, 0.8f);
    }

    @Override
    protected boolean canKillEntity(EntityPlayer player) {
        return true;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_CLOTH_BREAK;
    }

    @Override
    protected Item getWithItem() {
        return MaidItems.SUITCASE;
    }

    @Override
    protected ItemStack getKilledStack() {
        return new ItemStack(getWithItem());
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (player.getHeldItem(hand).interactWithEntity(player, this, hand)) {
            return true;
        }
        player.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.SUITCASE.getId(), world, this.getEntityId(), 0, 0);
        return true;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag(MAIN_INV_TAG, mainInv.serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey(MAIN_INV_TAG, Constants.NBT.TAG_COMPOUND)) {
            mainInv.deserializeNBT(compound.getCompoundTag(MAIN_INV_TAG));
        }
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(mainInv);
        }
        return super.getCapability(capability, facing);
    }
}
