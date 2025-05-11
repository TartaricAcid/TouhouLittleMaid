package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.loot.AdditionLootModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

import javax.annotation.Nullable;
import java.util.Optional;

public class GlobalLootModifier extends GlobalLootModifierProvider {
    private static final ResourceLocation CHEST = new ResourceLocation("chest");
    private static final ResourceLocation FISHING = new ResourceLocation("fishing");

    public GlobalLootModifier(PackOutput output) {
        super(output, TouhouLittleMaid.MOD_ID);
    }

    @Override
    public void start() {
        addAllChestLootModifier("chest_power_point", LootTableGenerator.CHEST_POWER_POINT);

        addChestLootModifier("spawn_bonus_chest", BuiltInLootTables.SPAWN_BONUS_CHEST, LootTableGenerator.SPAWN_BONUS);
        addChestLootModifier("village_temple", BuiltInLootTables.VILLAGE_TEMPLE, LootTableGenerator.NORMAL_BAUBLE);
        addChestLootModifier("desert_pyramid", BuiltInLootTables.DESERT_PYRAMID, LootTableGenerator.RARE_BAUBLE);
        addChestLootModifier("jungle_temple", BuiltInLootTables.JUNGLE_TEMPLE, LootTableGenerator.RARE_BAUBLE);
        addChestLootModifier("woodland_mansion_bauble", BuiltInLootTables.WOODLAND_MANSION, LootTableGenerator.VERY_RARE_BAUBLE);
        addChestLootModifier("simple_dungeon", BuiltInLootTables.SIMPLE_DUNGEON, LootTableGenerator.FURNACE_OR_CRAFTING_TABLE_BACKPACK);
        addChestLootModifier("abandoned_mineshaft", BuiltInLootTables.ABANDONED_MINESHAFT, LootTableGenerator.NORMAL_BACKPACK);
        addChestLootModifier("nether_bridge", BuiltInLootTables.NETHER_BRIDGE, LootTableGenerator.TANK_BACKPACK);
        addChestLootModifier("stronghold_corridor", BuiltInLootTables.STRONGHOLD_CORRIDOR, LootTableGenerator.ENDER_CHEST_BACKPACK);
        addChestLootModifier("stronghold_library", BuiltInLootTables.STRONGHOLD_LIBRARY, LootTableGenerator.SHRINE_LESS);
        addChestLootModifier("ancient_city", BuiltInLootTables.ANCIENT_CITY, LootTableGenerator.SHRINE_LESS);
        addChestLootModifier("bastion_treasure", BuiltInLootTables.BASTION_TREASURE, LootTableGenerator.SHRINE_LESS);
        addChestLootModifier("end_city_treasure", BuiltInLootTables.END_CITY_TREASURE, LootTableGenerator.SHRINE_MORE);

        addChestLootModifier("maid_buried_treasure", BuiltInLootTables.BURIED_TREASURE, LootTableGenerator.MAID_BURIED_TREASURE);

        addChestLootModifier("pillager_outpost_gift", BuiltInLootTables.PILLAGER_OUTPOST, LootTableGenerator.STRUCTURE_SPAWN_MAID_GIFT);
        addChestLootModifier("woodland_mansion_gift", BuiltInLootTables.WOODLAND_MANSION, LootTableGenerator.STRUCTURE_SPAWN_MAID_GIFT);

        addFishLootModifier("fishing_power_point", BuiltInLootTables.FISHING_JUNK, LootTableGenerator.FISHING_POWER_POINT);
    }

    private void addChestLootModifier(String name, @Nullable ResourceLocation lootTableId, ResourceLocation lootTableAdd) {
        var conditions = new LootItemCondition[]{};
        this.add(name, new AdditionLootModifier(conditions, CHEST, Optional.ofNullable(lootTableId), lootTableAdd));
    }

    private void addAllChestLootModifier(String name, ResourceLocation lootTableAdd) {
        this.addChestLootModifier(name, null, lootTableAdd);
    }

    private void addFishLootModifier(String name, @Nullable ResourceLocation lootTableId, ResourceLocation lootTableAdd) {
        var conditions = new LootItemCondition[]{};
        this.add(name, new AdditionLootModifier(conditions, FISHING, Optional.ofNullable(lootTableId), lootTableAdd));
    }
}
