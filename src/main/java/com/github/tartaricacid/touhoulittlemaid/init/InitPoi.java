package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.poi.MaidPoiManager;
import net.minecraft.village.PointOfInterestType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitPoi {
    public static final DeferredRegister<PointOfInterestType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, TouhouLittleMaid.MOD_ID);

    public static final RegistryObject<PointOfInterestType> MAID_BED = POI_TYPES.register("maid_bed", MaidPoiManager::getMaidBed);
    public static final RegistryObject<PointOfInterestType> JOY_BLOCK = POI_TYPES.register("joy_block", MaidPoiManager::getJoyBlock);
}