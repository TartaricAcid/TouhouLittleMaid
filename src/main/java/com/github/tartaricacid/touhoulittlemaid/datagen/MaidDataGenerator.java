package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.DamageTypeGenerator;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.EntityTypeGenerator;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagBlock;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagItem;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MaidDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        var vanillaPack = generator.getVanillaPack(true);
        CompletableFuture<HolderLookup.Provider> registries = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // Advancement
        generator.addProvider(true, new ForgeAdvancementProvider(
                packOutput, registries, existingFileHelper,
                Collections.singletonList(new AdvancementGenerator())
        ));

        // Loot Tables
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput,
                Set.of(LootTableGenerator.CAKE), List.of(
                new LootTableProvider.SubProviderEntry(LootTableGenerator.AdvancementLootTables::new, LootContextParamSets.ADVANCEMENT_REWARD),
                new LootTableProvider.SubProviderEntry(LootTableGenerator.ChestLootTables::new, LootContextParamSets.CHEST),
                new LootTableProvider.SubProviderEntry(LootTableGenerator.EntityLootTables::new, LootContextParamSets.ENTITY)
        )));

        // Global Loot Modifier
        generator.addProvider(event.includeServer(), new GlobalLootModifier(packOutput));

        // Tags
        var blockTagsProvider = vanillaPack.addProvider(output -> new TagBlock(output, registries, TouhouLittleMaid.MOD_ID, existingFileHelper));
        vanillaPack.addProvider(output -> new TagItem(output, registries, blockTagsProvider.contentsGetter(), TouhouLittleMaid.MOD_ID, existingFileHelper));
        generator.addProvider(event.includeServer(), new DamageTypeGenerator(packOutput, registries, existingFileHelper));
        generator.addProvider(event.includeServer(), new EntityTypeGenerator(packOutput, registries, existingFileHelper));

        //generator.addProvider(true, new LanguageGenerator(packOutput));
        generator.addProvider(event.includeClient(), new ItemModelGenerator(packOutput, existingFileHelper));
    }
}
