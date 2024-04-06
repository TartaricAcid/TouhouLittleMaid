package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.PicnicBasketContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.*;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitContainer {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TouhouLittleMaid.MOD_ID);

    public static final RegistryObject<MenuType<EmptyBackpackContainer>> MAID_EMPTY_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_empty_backpack_container", () -> EmptyBackpackContainer.TYPE);
    public static final RegistryObject<MenuType<SmallBackpackContainer>> MAID_SMALL_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_small_backpack_container", () -> SmallBackpackContainer.TYPE);
    public static final RegistryObject<MenuType<MiddleBackpackContainer>> MAID_MIDDLE_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_middle_backpack_container", () -> MiddleBackpackContainer.TYPE);
    public static final RegistryObject<MenuType<BigBackpackContainer>> MAID_BIG_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_big_backpack_container", () -> BigBackpackContainer.TYPE);
    public static final RegistryObject<MenuType<CraftingTableBackpackContainer>> MAID_CRAFTING_TABLE_BACKPACK_CONTAINER = CONTAINER_TYPE.register("maid_crafting_table_backpack_container", () -> CraftingTableBackpackContainer.TYPE);
    public static final RegistryObject<MenuType<EnderChestBackpackContainer>> MAID_ENDER_CHEST_CONTAINER = CONTAINER_TYPE.register("maid_ender_chest_container", () -> EnderChestBackpackContainer.TYPE);
    public static final RegistryObject<MenuType<FurnaceBackpackContainer>> MAID_FURNACE_CONTAINER = CONTAINER_TYPE.register("maid_furnace_container", () -> FurnaceBackpackContainer.TYPE);
    public static final RegistryObject<MenuType<TankBackpackContainer>> MAID_TANK_CONTAINER = CONTAINER_TYPE.register("maid_tank_container", () -> TankBackpackContainer.TYPE);

    public static final RegistryObject<MenuType<MaidConfigContainer>> MAID_CONFIG_CONTAINER = CONTAINER_TYPE.register("maid_config_container", () -> MaidConfigContainer.TYPE);
    public static final RegistryObject<MenuType<WirelessIOContainer>> WIRELESS_IO_CONTAINER = CONTAINER_TYPE.register("wireless_io_container", () -> WirelessIOContainer.TYPE);
    public static final RegistryObject<MenuType<PicnicBasketContainer>> PICNIC_BASKET_CONTAINER = CONTAINER_TYPE.register("picnic_basket_container", () -> PicnicBasketContainer.TYPE);
}
