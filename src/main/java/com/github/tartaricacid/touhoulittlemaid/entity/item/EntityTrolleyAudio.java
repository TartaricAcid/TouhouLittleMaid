package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.TrolleyAudioSoundMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
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
 * @date 2020/2/1 20:56
 **/
public class EntityTrolleyAudio extends AbstractEntityTrolley {
    private static final String RECORD_ITEM = "RecordItem";
    private static final DataParameter<Boolean> STOP_PLAY = EntityDataManager.createKey(EntityTrolleyAudio.class, DataSerializers.BOOLEAN);
    private final ItemStackHandler recordInv = new ItemStackHandler() {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            return stack.getItem() instanceof ItemRecord;
        }
    };

    public EntityTrolleyAudio(World worldIn) {
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
        return MaidItems.TROLLEY_AUDIO;
    }

    @Override
    protected ItemStack getKilledStack() {
        return new ItemStack(getWithItem());
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(STOP_PLAY, true);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (player.getHeldItem(hand).interactWithEntity(player, this, hand)) {
            return true;
        }
        if (!world.isRemote) {
            insertRecord(player, world, player.getHeldItem(hand));
        }
        return true;
    }

    private void insertRecord(EntityPlayer player, World world, ItemStack stack) {
        if (!this.recordInv.getStackInSlot(0).isEmpty()) {
            this.entityDropItem(this.recordInv.getStackInSlot(0), 0.6F);
            this.recordInv.setStackInSlot(0, ItemStack.EMPTY);
            this.setStop(true);
        } else {
            if (stack.getItem() instanceof ItemRecord) {
                ItemRecord record = (ItemRecord) stack.getItem();
                this.recordInv.setStackInSlot(0, stack.copy());
                this.setStop(false);
                CommonProxy.INSTANCE.sendToDimension(new TrolleyAudioSoundMessage(record.displayName, record.getSound(), this), world.provider.getDimension());
                if (!player.isCreative()) {
                    stack.shrink(1);
                }
            }
        }
    }

    public boolean isStop() {
        return this.dataManager.get(STOP_PLAY);
    }

    private void setStop(boolean isStop) {
        this.dataManager.set(STOP_PLAY, isStop);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag(RECORD_ITEM, recordInv.serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey(RECORD_ITEM, Constants.NBT.TAG_COMPOUND)) {
            recordInv.deserializeNBT(compound.getCompoundTag(RECORD_ITEM));
        }
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(recordInv);
        }
        return super.getCapability(capability, facing);
    }
}
