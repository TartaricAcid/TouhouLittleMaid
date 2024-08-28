package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.poi.MaidPoiManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitPoi {
    public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, TouhouLittleMaid.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> MAID_BED = POI_TYPES.register("maid_bed", MaidPoiManager::getMaidBed);
    public static final DeferredHolder<PoiType, PoiType> JOY_BLOCK = POI_TYPES.register("joy_block", MaidPoiManager::getJoyBlock);
    public static final DeferredHolder<PoiType, PoiType> HOME_MEAL_BLOCK = POI_TYPES.register("home_meal", MaidPoiManager::getHomeMeal);
    public static final DeferredHolder<PoiType, PoiType> SCARECROW = POI_TYPES.register("scarecrow", MaidPoiManager::getScarecrow);
}