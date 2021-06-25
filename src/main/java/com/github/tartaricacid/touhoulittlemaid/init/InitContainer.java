package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class InitContainer {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.CONTAINERS, TouhouLittleMaid.MOD_ID);

    public static final RegistryObject<ContainerType<MaidInventory>> MAID_INVENTORY = CONTAINER_TYPE.register("maid_inventory", () -> MaidInventory.TYPE);
}
