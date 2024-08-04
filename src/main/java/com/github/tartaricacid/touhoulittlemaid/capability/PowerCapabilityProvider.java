package com.github.tartaricacid.touhoulittlemaid.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.FloatTag;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PowerCapabilityProvider implements ICapabilitySerializable<FloatTag> {
    public static Capability<PowerCapability> POWER_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    private PowerCapability instance = null;

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == POWER_CAP) {
            return LazyOptional.of(this::createCapability).cast();
        }
        return LazyOptional.empty();
    }

    @Nonnull
    private PowerCapability createCapability() {
        if (instance == null) {
            this.instance = new PowerCapability();
        }
        return instance;
    }

    @Override
    public void deserializeNBT(FloatTag nbt) {
        createCapability().deserializeNBT(nbt);
    }

    @Override
    public FloatTag serializeNBT() {
        return createCapability().serializeNBT();
    }
}
