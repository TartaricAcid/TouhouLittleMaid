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

public class HasGuideSerializer implements ICapabilitySerializable<NBTBase> {
    @CapabilityInject(HasGuideHandler.class)
    public static Capability<HasGuideHandler> HAS_GUIDE_CAP = null;
    private HasGuideHandler instance = HAS_GUIDE_CAP.getDefaultInstance();

    public static void register() {
        CapabilityManager.INSTANCE.register(HasGuideHandler.class, new Capability.IStorage<HasGuideHandler>() {
            @Override
            public void readNBT(Capability<HasGuideHandler> capability, HasGuideHandler instance, EnumFacing side, NBTBase nbt) {
                instance.setFirst(((NBTTagInt) nbt).getInt() != 0);
            }

            @Override
            public NBTBase writeNBT(Capability<HasGuideHandler> capability, HasGuideHandler instance, EnumFacing side) {
                int number = instance.isFirst() ? 1 : 0;
                return new NBTTagInt(number);
            }
        }, HasGuideHandler.FACTORY);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == HAS_GUIDE_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == HAS_GUIDE_CAP ? HAS_GUIDE_CAP.cast(this.instance) : null;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        HAS_GUIDE_CAP.getStorage().readNBT(HAS_GUIDE_CAP, this.instance, null, nbt);
    }

    @Override
    public NBTBase serializeNBT() {
        return HAS_GUIDE_CAP.getStorage().writeNBT(HAS_GUIDE_CAP, this.instance, null);
    }
}
