package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.poi.MaidPoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitPoi {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, TouhouLittleMaid.MOD_ID);

    public static final RegistryObject<PoiType> MAID_BED = POI_TYPES.register("maid_bed", MaidPoiManager::getMaidBed);
    public static final RegistryObject<PoiType> JOY_BLOCK = POI_TYPES.register("joy_block", MaidPoiManager::getJoyBlock);
    public static final RegistryObject<PoiType> HOME_MEAL_BLOCK = POI_TYPES.register("home_meal", MaidPoiManager::getHomeMeal);
    public static final RegistryObject<PoiType> SCARECROW = POI_TYPES.register("scarecrow", MaidPoiManager::getScarecrow);
}