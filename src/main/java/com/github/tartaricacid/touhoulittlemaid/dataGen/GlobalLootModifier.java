package com.github.tartaricacid.touhoulittlemaid.dataGen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class GlobalLootModifier extends GlobalLootModifierProvider {
    public GlobalLootModifier(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        super(output, registries, modId);
    }

    @Override
    public void start() {
        var condBuilder = new AnyOfCondition.Builder();
        BuiltInLootTables.all().stream()
                        .filter(lootTable -> lootTable.location().getPath().startsWith("chests/"))
                        .forEach(lootTable -> condBuilder.or(LootTableIdCondition.builder(lootTable.location())));
        add("chests_amendments", new AddTableLootModifier(
                new LootItemCondition[] {
                        LootItemRandomChanceCondition.randomChance(0.05F).build(),
                        condBuilder.build(),
                },
                LootTableGenerator.ADDITIONAL_LOOT_TABLE
        ));
    }
}