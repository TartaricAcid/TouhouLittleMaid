package com.github.tartaricacid.touhoulittlemaid.dataGen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class DataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        ExistingFileHelper efh = event.getExistingFileHelper();
        var generator = event.getGenerator();
        var registries = event.getLookupProvider();
        var vanillaPack = generator.getVanillaPack(true);
        var existingFileHelper = event.getExistingFileHelper();
        var pack = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> completableFuture = event.getLookupProvider();

        // loot tables
        generator.addProvider(event.includeServer(), new LootTableProvider(pack,
                Set.of(LootTableGenerator.POWER_POINT, LootTableGenerator.SHRINE),
                List.of(new LootTableProvider.SubProviderEntry(LootTableGenerator::new, LootContextParamSets.CHEST)),
                new RegistryDataGenerator(event.getGenerator().getPackOutput(), event.getLookupProvider()).getRegistryProvider()));

        //global loot table
        vanillaPack.addProvider(
                packOutput -> new GlobalLootModifier(packOutput, registries, TouhouLittleMaid.MOD_ID)
        );


        //recipe
        vanillaPack.addProvider(
                packOutput -> new RecipeGenerator(packOutput,registries));

        //Tags
        var blockTagsProvider = vanillaPack
                .addProvider(packOutput -> new TagBlock(packOutput, registries, TouhouLittleMaid.MOD_ID, existingFileHelper));
        vanillaPack.addProvider(
                packOutput -> new TagItem(packOutput, registries, blockTagsProvider.contentsGetter(), TouhouLittleMaid.MOD_ID, existingFileHelper));

        //registry-based stuff
        DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(pack, event.getLookupProvider());
        generator.addProvider(event.includeServer(), datapackProvider);
    }
}
