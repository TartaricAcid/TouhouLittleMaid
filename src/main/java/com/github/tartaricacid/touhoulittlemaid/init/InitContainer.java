package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.WirelessIOContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class InitContainer {
    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPE = DeferredRegister.create(ForgeRegistries.CONTAINERS, TouhouLittleMaid.MOD_ID);

    public static final RegistryObject<ContainerType<MaidMainContainer>> MAID_MAIN_CONTAINER = CONTAINER_TYPE.register("maid_main_container", () -> MaidMainContainer.TYPE);
    public static final RegistryObject<ContainerType<WirelessIOContainer>> WIRELESS_IO_CONTAINER = CONTAINER_TYPE.register("wireless_io_container", () -> WirelessIOContainer.TYPE);
}
