package com.github.tartaricacid.touhoulittlemaid;

import com.github.tartaricacid.touhoulittlemaid.config.Config;
import com.github.tartaricacid.touhoulittlemaid.init.InitContainer;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TouhouLittleMaid.MOD_ID)
public final class TouhouLittleMaid {
    public static final String MOD_ID = "touhou_little_maid";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public TouhouLittleMaid() {
        InitEntities.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        InitEntities.ATTRIBUTES.register(FMLJavaModLoadingContext.get().getModEventBus());
        InitEntities.MEMORY_MODULE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        InitEntities.SENSOR_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        InitEntities.SCHEDULES.register(FMLJavaModLoadingContext.get().getModEventBus());
        InitEntities.DATA_SERIALIZERS.register(FMLJavaModLoadingContext.get().getModEventBus());
        InitItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        InitContainer.CONTAINER_TYPE.register(FMLJavaModLoadingContext.get().getModEventBus());
        InitSounds.SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.initConfig());
        DeferredWorkQueue.runLater(NetworkHandler::init);
    }
}
