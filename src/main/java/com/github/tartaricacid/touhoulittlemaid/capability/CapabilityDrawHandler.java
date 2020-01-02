package com.github.tartaricacid.touhoulittlemaid.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2020/1/2 21:04
 **/
public class CapabilityDrawHandler implements ICapabilitySerializable<NBTBase> {
    private static final String TOTAL_DRAW_TIMES = "TotalDrawTimes";
    private static final String DRAW_INFO = "DrawInfo";
    private static final String MODEL_ID = "ModelId";
    private static final String TIMES = "Times";
    @CapabilityInject(DrawHandler.class)
    public static Capability<DrawHandler> DRAW_CAP = null;
    private DrawHandler instance = DRAW_CAP.getDefaultInstance();

    public static void register() {
        CapabilityManager.INSTANCE.register(DrawHandler.class, new Capability.IStorage<DrawHandler>() {
            @Override
            public void readNBT(Capability<DrawHandler> capability, DrawHandler instance, EnumFacing side, NBTBase nbt) {
                NBTTagCompound tag = (NBTTagCompound) nbt;
                instance.setTotalDrawTimes(tag.getInteger(TOTAL_DRAW_TIMES));
                NBTTagList drawInfoList = tag.getTagList(DRAW_INFO, Constants.NBT.TAG_COMPOUND);
                for (NBTBase nbtBase : drawInfoList) {
                    NBTTagCompound data = (NBTTagCompound) nbtBase;
                    instance.addDrawnInfo(data.getString(MODEL_ID), data.getInteger(TIMES));
                }
            }

            @Override
            public NBTBase writeNBT(Capability<DrawHandler> capability, DrawHandler instance, EnumFacing side) {
                NBTTagCompound nbt = new NBTTagCompound();
                nbt.setInteger(TOTAL_DRAW_TIMES, instance.getTotalDrawTimes());
                NBTTagList drawInfoList = new NBTTagList();
                for (String modelId : instance.getDrawInfoMaps().keySet()) {
                    NBTTagCompound data = new NBTTagCompound();
                    data.setString(MODEL_ID, modelId);
                    data.setInteger(TIMES, instance.getDrawInfo(modelId));
                    drawInfoList.appendTag(data);
                }
                nbt.setTag(DRAW_INFO, drawInfoList);
                return nbt;
            }
        }, DrawHandler.FACTORY);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == DRAW_CAP;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == DRAW_CAP ? DRAW_CAP.cast(this.instance) : null;
    }

    @Override
    public void deserializeNBT(NBTBase nbt) {
        DRAW_CAP.getStorage().readNBT(DRAW_CAP, this.instance, null, nbt);
    }

    @Override
    public NBTBase serializeNBT() {
        return DRAW_CAP.getStorage().writeNBT(DRAW_CAP, this.instance, null);
    }
}

