package com.github.tartaricacid.touhoulittlemaid.api.backpack;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.inventory.ContainerData;

public interface IBackpackData {
    ContainerData getDataAccess();

    void load(CompoundNBT tag, EntityMaid maid);

    void save(CompoundNBT tag, EntityMaid maid);

    void serverTick(EntityMaid maid);
}
