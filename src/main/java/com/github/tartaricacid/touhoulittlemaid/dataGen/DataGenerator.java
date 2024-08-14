package com.github.tartaricacid.touhoulittlemaid.dataGen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.core.HolderLookup;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

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

        //registry-based stuff
        DatapackBuiltinEntriesProvider datapackProvider = new RegistryDataGenerator(pack, event.getLookupProvider());
        generator.addProvider(event.includeServer(), datapackProvider);

        //LootTable
        vanillaPack.addProvider(
                packOutput -> new LootModifierGenerator(packOutput, registries, TouhouLittleMaid.MOD_ID));

        //recipe
        vanillaPack.addProvider(
                packOutput -> new RecipeGenerator(packOutput,registries));

        //Tags
        var blockTagsProvider = vanillaPack
                .addProvider(packOutput -> new TagBlock(packOutput, registries, TouhouLittleMaid.MOD_ID, existingFileHelper));
        vanillaPack.addProvider(
                packOutput -> new TagItem(packOutput, registries, blockTagsProvider.contentsGetter(), TouhouLittleMaid.MOD_ID, existingFileHelper));
    }
}
