package com.github.tartaricacid.touhoulittlemaid.dataGen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public record LootTableGenerator(HolderLookup.Provider provider) implements LootTableSubProvider {
    public static final ResourceKey<LootTable> POWER_POINT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "chests/power_point"));
    public static final ResourceKey<LootTable> SHRINE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "chests/shrine"));

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
        consumer.accept(POWER_POINT,
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .name("power_point")
                        .setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.05F))
                        .add(LootItem.lootTableItem(InitItems.POWER_POINT))));
        consumer.accept(SHRINE,
                LootTable.lootTable().withPool(LootPool.lootPool()
                        .name("shrine")
                        .setRolls(ConstantValue.exactly(1))
                        .when(LootItemRandomChanceCondition.randomChance(0.05F))
                        .add(LootItem.lootTableItem(InitItems.SHRINE))));
    }
}