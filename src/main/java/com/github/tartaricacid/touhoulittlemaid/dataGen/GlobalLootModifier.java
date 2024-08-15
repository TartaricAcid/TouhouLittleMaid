package com.github.tartaricacid.touhoulittlemaid.dataGen;

import com.github.tartaricacid.touhoulittlemaid.loot.AdditionalLootModifier;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;

import java.util.concurrent.CompletableFuture;

public class GlobalLootModifier extends GlobalLootModifierProvider {
    public GlobalLootModifier(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        super(output, registries, modId);
    }

    @Override
    public void start() {
        add("additional", new AdditionalLootModifier(
                new LootItemCondition[]{LootItemRandomChanceCondition.randomChance(0.05F).build()},
                LootContextParamSets.CHEST,
                LootTableGenerator.ADDITIONAL_LOOT_TABLE.location()));
    }
}