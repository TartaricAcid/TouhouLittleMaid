package com.github.tartaricacid.touhoulittlemaid.init.registry;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.world.level.biome.Biome.BiomeCategory.*;


@Mod.EventBusSubscriber
public final class MobSpawnInfoRegistry {
    @SubscribeEvent
    public static void addMobSpawnInfo(BiomeLoadingEvent event) {
        if (biomeIsOkay(event.getCategory())) {
            event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(InitEntities.FAIRY.get(), MiscConfig.MAID_FAIRY_SPAWN_PROBABILITY.get(), 2, 6));
        }
    }

    private static boolean biomeIsOkay(Biome.BiomeCategory category) {
        // TODO: 2021/10/17 添加配置文件，管控可生成的生物群系
        return category != NETHER && category != THEEND && category != NONE && category != MUSHROOM;
    }
}
