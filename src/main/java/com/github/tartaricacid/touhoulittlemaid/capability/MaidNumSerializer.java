package com.github.tartaricacid.touhoulittlemaid.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/8/28 16:34
 **/
public class MaidNumSerializer implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(MaidNumHandler.class)
    public static Capability<MaidNumHandler> MAID_NUM_CAP = null;
    private MaidNumHandler instance = MAID_NUM_CAP.getDefaultInstance();

    public static void register() {
        CapabilityManager.INSTANCE.register(MaidNumHandler.class, new Capability.IStorage<MaidNumHandler>() {
            @Override
            public void readNBT(Capability<MaidNumHandler> capability, MaidNumHandler instance, EnumFacing side, NBTBase nbt) {
                instance.set(((NBTTagInt) nbt).getInt());
            }

            @Override
            public NBTBase writeNBT(Capability<MaidNumHandler> capability, MaidNumHandler instance, EnumFacing side) {
                return new NBTTagInt(instance.get());
            }
        }, MaidNumHandler.FACTORY);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == MAID_NUM_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == MAID_NUM_CAP ? MAID_NUM_CAP.cast(this.instance) : null;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        MAID_NUM_CAP.getStorage().readNBT(MAID_NUM_CAP, this.instance, null, nbt);
    }

    @Override
    public NBTBase serializeNBT() {
        return MAID_NUM_CAP.getStorage().writeNBT(MAID_NUM_CAP, this.instance, null);
    }
}
