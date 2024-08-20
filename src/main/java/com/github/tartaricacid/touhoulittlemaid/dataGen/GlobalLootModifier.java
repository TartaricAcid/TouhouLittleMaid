package com.github.tartaricacid.touhoulittlemaid.dataGen;

import com.github.tartaricacid.touhoulittlemaid.loot.LootTableTypeCondition;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;

import java.util.concurrent.CompletableFuture;

public class GlobalLootModifier extends GlobalLootModifierProvider {
    public GlobalLootModifier(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, String modId) {
        super(output, registries, modId);
    }

    @Override
    public void start() {
        add("additional", new AddTableLootModifier(
                new LootItemCondition[]{new LootTableTypeCondition("chests/")},
                LootTableGenerator.ADDITIONAL_LOOT_TABLE));
    }
}