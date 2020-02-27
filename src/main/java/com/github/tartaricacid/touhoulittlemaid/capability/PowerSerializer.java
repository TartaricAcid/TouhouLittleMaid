package com.github.tartaricacid.touhoulittlemaid.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
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
public class PowerSerializer implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(PowerHandler.class)
    public static Capability<PowerHandler> POWER_CAP = null;
    private PowerHandler instance = POWER_CAP.getDefaultInstance();

    public static void register() {
        CapabilityManager.INSTANCE.register(PowerHandler.class, new Capability.IStorage<PowerHandler>() {
            @Override
            public void readNBT(Capability<PowerHandler> capability, PowerHandler instance, EnumFacing side, NBTBase nbt) {
                instance.set(((NBTTagFloat) nbt).getFloat());
            }

            @Override
            public NBTBase writeNBT(Capability<PowerHandler> capability, PowerHandler instance, EnumFacing side) {
                return new NBTTagFloat(instance.get());
            }
        }, PowerHandler.FACTORY);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == POWER_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == POWER_CAP ? POWER_CAP.cast(this.instance) : null;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        POWER_CAP.getStorage().readNBT(POWER_CAP, this.instance, null, nbt);
    }

    @Override
    public NBTBase serializeNBT() {
        return POWER_CAP.getStorage().writeNBT(POWER_CAP, this.instance, null);
    }
}
