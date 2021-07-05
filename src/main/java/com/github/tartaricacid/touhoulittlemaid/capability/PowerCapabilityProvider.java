package com.github.tartaricacid.touhoulittlemaid.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PowerCapabilityProvider implements ICapabilitySerializable<INBT> {
    @CapabilityInject(PowerCapability.class)
    public static Capability<PowerCapability> POWER_CAP = null;
    private final PowerCapability instance = POWER_CAP.getDefaultInstance();

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == POWER_CAP && instance != null) {
            return LazyOptional.of(() -> instance).cast();
        }
        return LazyOptional.empty();
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        POWER_CAP.readNBT(instance, null, nbt);
    }

    @Override
    public INBT serializeNBT() {
        return POWER_CAP.writeNBT(instance, null);
    }
}
