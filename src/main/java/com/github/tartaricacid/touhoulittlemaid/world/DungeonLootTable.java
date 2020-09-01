package com.github.tartaricacid.touhoulittlemaid.world;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class DungeonLootTable {
    private static final ResourceLocation POWER_POINT_LOOT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "power_point");

    @SubscribeEvent
    public static void onLootLoad(LootTableLoadEvent event) {
        if (event.getName().getPath().startsWith("chests")) {
            LootPool pool = event.getLootTableManager().getLootTableFromLocation(POWER_POINT_LOOT).getPool("power_point");
            event.getTable().addPool(pool);
        }
    }

    public static ResourceLocation getPowerPointLoot() {
        return POWER_POINT_LOOT;
    }
}
