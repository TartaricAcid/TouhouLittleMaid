package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.*;
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
        generator.addProvider(event.includeServer(), new LootTableProvider(pack, Set.of(),
                List.of(
                        new LootTableProvider.SubProviderEntry(LootTableGenerator.ChestLootTables::new, LootContextParamSets.CHEST),
                        new LootTableProvider.SubProviderEntry(LootTableGenerator.AdvancementLootTables::new, LootContextParamSets.ADVANCEMENT_REWARD),
                        new LootTableProvider.SubProviderEntry(LootTableGenerator.EntityLootTables::new, LootContextParamSets.ENTITY),
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
        generator.addProvider(event.includeServer(), new DamageTypeGenerator(pack, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new EntityTypeGenerator(pack, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new TagRecipeSerializer(pack, event.getLookupProvider(), event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), new TagPaintingVariant(pack, event.getLookupProvider(), event.getExistingFileHelper()));

        // Registry Based Stuff
        DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(pack, event.getLookupProvider());
        generator.addProvider(event.includeServer(), datapackProvider);

        generator.addProvider(event.includeClient(), new ItemModelGenerator(pack, existingFileHelper));
        generator.addProvider(event.includeServer(), new DataMapGenerator(pack, registries));
    }
}
