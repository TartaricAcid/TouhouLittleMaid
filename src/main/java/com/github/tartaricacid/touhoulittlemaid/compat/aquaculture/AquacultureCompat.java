package com.github.tartaricacid.touhoulittlemaid.compat.aquaculture;

import com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.client.AquacultureClientRegister;
import com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.entity.AquacultureFishingHook;
import com.github.tartaricacid.touhoulittlemaid.compat.aquaculture.entity.AquacultureFishingType;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing.FishingTypeManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public class AquacultureCompat {
    private static final String MOD_ID = "aquaculture";
    private static boolean INSTALLED;

    public static void init() {
        INSTALLED = LoadingModList.get().getModFileById(MOD_ID) != null;
        if (INSTALLED) {
            registerAll();
        }
    }

    public static void registerFishingType(FishingTypeManager manager) {
        if (INSTALLED) {
            manager.addFishingType(new AquacultureFishingType());
        }
    }

    private static void registerAll() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(new AquacultureCompat());
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.register(new AquacultureClientRegister());
        }
    }

    @SubscribeEvent
    public void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ENTITY_TYPES, helper -> helper.register("aquaculture_fishing_hook", AquacultureFishingHook.TYPE));
    }
}
