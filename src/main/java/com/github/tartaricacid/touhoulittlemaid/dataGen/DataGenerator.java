package com.github.tartaricacid.touhoulittlemaid.dataGen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        var generator = event.getGenerator();
        var registries = event.getLookupProvider();
        var vanillaPack = generator.getVanillaPack(true);
        var existingFileHelper = event.getExistingFileHelper();
        var pack = generator.getPackOutput();

        // Advancements
        generator.addProvider(event.includeServer(), new AdvancementDataGen(pack, registries, existingFileHelper));

        // Loot Tables
        generator.addProvider(event.includeServer(), new LootTableProvider(pack,
                Set.of(
                        LootTableGenerator.ADDITIONAL_LOOT_TABLE,
                        LootTableGenerator.GIVE_SMART_SLAB,
                        LootTableGenerator.GRANT_PATCHOULI_BOOK
                ),
                List.of(
                        new LootTableProvider.SubProviderEntry(LootTableGenerator.ChestLootTables::new, LootContextParamSets.CHEST),
                        new LootTableProvider.SubProviderEntry(LootTableGenerator.AdvancementLootTables::new, LootContextParamSets.ADVANCEMENT_REWARD),
                        new LootTableProvider.SubProviderEntry(LootTableGenerator.BlockLootTables::new, LootContextParamSets.BLOCK)
                ),
                new RegistryDataGenerator(event.getGenerator().getPackOutput(), event.getLookupProvider()).getRegistryProvider()));

        // Global Loot Modifier
        vanillaPack.addProvider(packOutput -> new GlobalLootModifier(packOutput, registries, TouhouLittleMaid.MOD_ID));

        // Recipe
        vanillaPack.addProvider(
                packOutput -> new RecipeGenerator(packOutput, registries));

        // Tags
        var blockTagsProvider = vanillaPack
                .addProvider(packOutput -> new TagBlock(packOutput, registries, TouhouLittleMaid.MOD_ID, existingFileHelper));
        vanillaPack.addProvider(
                packOutput -> new TagItem(packOutput, registries, blockTagsProvider.contentsGetter(), TouhouLittleMaid.MOD_ID, existingFileHelper));

        // Registry Based Stuff
        DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(pack, event.getLookupProvider());
        generator.addProvider(event.includeServer(), datapackProvider);
    }
}
