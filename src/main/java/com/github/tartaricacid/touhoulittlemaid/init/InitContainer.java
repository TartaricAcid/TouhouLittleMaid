package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.*;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class InitContainer {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.CONTAINERS, TouhouLittleMaid.MOD_ID);
    public static final RegistryObject<ContainerType<EmptyBackpackContainer>> MAID_EMPTY_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_empty_backpack_container", () -> EmptyBackpackContainer.TYPE);
    public static final RegistryObject<ContainerType<SmallBackpackContainer>> MAID_SMALL_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_small_backpack_container", () -> SmallBackpackContainer.TYPE);
    public static final RegistryObject<ContainerType<MiddleBackpackContainer>> MAID_MIDDLE_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_middle_backpack_container", () -> MiddleBackpackContainer.TYPE);
    public static final RegistryObject<ContainerType<BigBackpackContainer>> MAID_BIG_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_big_backpack_container", () -> BigBackpackContainer.TYPE);
    public static final RegistryObject<ContainerType<CraftingTableBackpackContainer>> MAID_CRAFTING_TABLE_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_crafting_table_backpack_container", () -> CraftingTableBackpackContainer.TYPE);
    public static final RegistryObject<ContainerType<EnderChestBackpackContainer>> MAID_ENDER_CHEST_CONTAINER = CONTAINER_TYPE.register("maid_ender_chest_container", () -> EnderChestBackpackContainer.TYPE);
    public static final RegistryObject<ContainerType<FurnaceBackpackContainer>> MAID_FURNACE_CONTAINER = CONTAINER_TYPE.register("maid_furnace_container", () -> FurnaceBackpackContainer.TYPE);
    public static final RegistryObject<ContainerType<TankBackpackContainer>> MAID_TANK_CONTAINER = CONTAINER_TYPE.register("maid_tank_container", () -> TankBackpackContainer.TYPE);

    public static final RegistryObject<ContainerType<MaidConfigContainer>> MAID_CONFIG_CONTAINER = CONTAINER_TYPE.register("maid_config_container", () -> MaidConfigContainer.TYPE);
    public static final RegistryObject<ContainerType<WirelessIOContainer>> WIRELESS_IO_CONTAINER = CONTAINER_TYPE.register("wireless_io_container", () -> WirelessIOContainer.TYPE);
}
