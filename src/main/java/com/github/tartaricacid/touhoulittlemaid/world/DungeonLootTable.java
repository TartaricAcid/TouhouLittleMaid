package com.github.tartaricacid.touhoulittlemaid.world;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class DungeonLootTable {
    private static final ResourceLocation POWER_POINT_LOOT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "power_point");
    private static final ResourceLocation SMART_SLAB_LOOT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "smart_slab");

    @SubscribeEvent
    public static void onLootLoad(LootTableLoadEvent event) {
        if (event.getName().getPath().startsWith("chests")) {
            LootPool powerPointPool = event.getLootTableManager().getLootTableFromLocation(POWER_POINT_LOOT).getPool("power_point");
            event.getTable().addPool(powerPointPool);

            if (GeneralConfig.MAID_CONFIG.spawnSlabInLootChest) {
                LootPool smartSlabPool = event.getLootTableManager().getLootTableFromLocation(SMART_SLAB_LOOT).getPool("smart_slab");
                event.getTable().addPool(smartSlabPool);
            }
        }
    }

    public static ResourceLocation getPowerPointLoot() {
        return POWER_POINT_LOOT;
    }

    public static ResourceLocation getSmartSlabLoot() {
        return SMART_SLAB_LOOT;
    }
}
