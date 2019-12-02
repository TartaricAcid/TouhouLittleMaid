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
public class CapabilityOwnerMaidNumHandler implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(OwnerMaidNumHandler.class)
    public static Capability<OwnerMaidNumHandler> OWNER_MAID_NUM_CAP = null;
    private OwnerMaidNumHandler instance = OWNER_MAID_NUM_CAP.getDefaultInstance();

    public static void register() {
        CapabilityManager.INSTANCE.register(OwnerMaidNumHandler.class, new Capability.IStorage<OwnerMaidNumHandler>() {
            @Override
            public void readNBT(Capability<OwnerMaidNumHandler> capability, OwnerMaidNumHandler instance, EnumFacing side, NBTBase nbt) {
                instance.set(((NBTTagInt) nbt).getInt());
            }

            @Override
            public NBTBase writeNBT(Capability<OwnerMaidNumHandler> capability, OwnerMaidNumHandler instance, EnumFacing side) {
                return new NBTTagInt(instance.get());
            }
        }, OwnerMaidNumHandler.FACTORY);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == OWNER_MAID_NUM_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == OWNER_MAID_NUM_CAP ? OWNER_MAID_NUM_CAP.cast(this.instance) : null;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        OWNER_MAID_NUM_CAP.getStorage().readNBT(OWNER_MAID_NUM_CAP, this.instance, null, nbt);
    }

    @Override
    public NBTBase serializeNBT() {
        return OWNER_MAID_NUM_CAP.getStorage().writeNBT(OWNER_MAID_NUM_CAP, this.instance, null);
    }
}
