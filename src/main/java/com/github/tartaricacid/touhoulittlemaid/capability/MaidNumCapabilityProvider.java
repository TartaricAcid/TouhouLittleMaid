package com.github.tartaricacid.touhoulittlemaid.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MaidNumCapabilityProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(MaidNumCapability.class)
    public static Capability<MaidNumCapability> MAID_NUM_CAP = null;
    private final MaidNumCapability instance = MAID_NUM_CAP.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == MAID_NUM_CAP && instance != null) {
            return LazyOptional.of(() -> instance).cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        MAID_NUM_CAP.readNBT(instance, null, nbt);
    }

    @Override
    public INBT serializeNBT() {
        return MAID_NUM_CAP.writeNBT(instance, null);
    }
}
