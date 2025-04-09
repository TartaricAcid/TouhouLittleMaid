package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.EntityTypeGenerator;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagBlock;
import com.github.tartaricacid.touhoulittlemaid.datagen.tag.TagItem;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MaidDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // Advancement
        generator.addProvider(true, new AdvancementGenerator(generator, helper));

        // Loot Tables
        generator.addProvider(event.includeServer(), new LootTableGenerator.AdvancementLootTables(generator));

        // Tags
        BlockTagsProvider blocktagsprovider = new TagBlock(generator, TouhouLittleMaid.MOD_ID, helper);
        generator.addProvider(event.includeServer(), new EntityTypeGenerator(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeServer(), blocktagsprovider);
        generator.addProvider(event.includeServer(), new TagItem(generator, blocktagsprovider, TouhouLittleMaid.MOD_ID, helper));
    }
}
