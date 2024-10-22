package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.block.BlockScarecrow;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;

public class LootTableGenerator {
    public static final ResourceKey<LootTable> ADDITIONAL_LOOT_TABLE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "chests/additional_loot_table"));
    public static final ResourceKey<LootTable> GIVE_SMART_SLAB = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "give_smart_slab"));
    public static final ResourceKey<LootTable> POWER_POINT = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "advancement/power_point"));
    public static final ResourceKey<LootTable> CAKE = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "advancement/cake"));

    public record ChestLootTables(HolderLookup.Provider provider) implements LootTableSubProvider {
        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(ADDITIONAL_LOOT_TABLE, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .name("power_point")
                            .setRolls(ConstantValue.exactly(1))
                            .when(LootItemRandomChanceCondition.randomChance(0.05F))
                            .add(LootItem.lootTableItem(InitItems.POWER_POINT)))
                    .withPool(LootPool.lootPool()
                            .name("shrine")
                            .setRolls(ConstantValue.exactly(1))
                            .when(LootItemRandomChanceCondition.randomChance(0.05F))
                            .add(LootItem.lootTableItem(InitItems.SHRINE))));
        }
    }

    public record AdvancementLootTables(HolderLookup.Provider provider) implements LootTableSubProvider {
        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(GIVE_SMART_SLAB, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(InitItems.SMART_SLAB_INIT))));

            consumer.accept(POWER_POINT, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(5))
                    .add(LootItem.lootTableItem(InitItems.POWER_POINT.get()))));

            consumer.accept(CAKE, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.CAKE))));
        }
    }

    public static class BlockLootTables extends BlockLootSubProvider {
        public final Set<Block> knownBlocks = new HashSet<>();

        public BlockLootTables(HolderLookup.Provider provider) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
        }

        @Override
        public void generate() {
            add(InitBlocks.MAID_BED.get(), block -> createSinglePropConditionTable(block, BlockMaidBed.PART, BedPart.HEAD));
            add(InitBlocks.SCARECROW.get(), block -> createSinglePropConditionTable(block, BlockScarecrow.HALF, DoubleBlockHalf.LOWER));

            dropSelf(InitBlocks.MODEL_SWITCHER.get());
            dropSelf(InitBlocks.KEYBOARD.get());
            dropSelf(InitBlocks.BOOKSHELF.get());
            dropSelf(InitBlocks.COMPUTER.get());
            dropSelf(InitBlocks.SHRINE.get());
        }

        @Override
        public void add(Block block, LootTable.Builder builder) {
            this.knownBlocks.add(block);
            super.add(block, builder);
        }

        @Override
        public Iterable<Block> getKnownBlocks() {
            return this.knownBlocks;
        }
    }
}