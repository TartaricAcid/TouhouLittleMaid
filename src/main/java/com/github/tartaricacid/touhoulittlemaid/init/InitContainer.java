package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidConfigContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitContainer {
    public static final DeferredRegister<MenuType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.MENU_TYPES, TouhouLittleMaid.MOD_ID);

    public static final RegistryObject<MenuType<MaidMainContainer>> MAID_MAIN_CONTAINER = CONTAINER_TYPE.register("maid_main_container", () -> MaidMainContainer.TYPE);
    public static final RegistryObject<MenuType<MaidConfigContainer>> MAID_CONFIG_CONTAINER = CONTAINER_TYPE.register("maid_config_container", () -> MaidConfigContainer.TYPE);
    public static final RegistryObject<MenuType<WirelessIOContainer>> WIRELESS_IO_CONTAINER = CONTAINER_TYPE.register("wireless_io_container", () -> WirelessIOContainer.TYPE);
}
