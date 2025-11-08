package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockMaidBed;
import com.github.tartaricacid.touhoulittlemaid.block.BlockScarecrow;
import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.loot.RandomBoardStateFunction;
import com.github.tartaricacid.touhoulittlemaid.loot.SetTankCountFunction;
import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class LootTableGenerator {
    public static final ResourceKey<LootTable> GIVE_SMART_SLAB = getLootTableKey("give_smart_slab");

    public static final ResourceKey<LootTable> ADVANCEMENT_POWER_POINT = getLootTableKey("advancement/power_point");
    public static final ResourceKey<LootTable> CAKE = getLootTableKey("advancement/cake");

    public static final ResourceKey<LootTable> CHEST_POWER_POINT = getLootTableKey("chest/power_point");
    public static final ResourceKey<LootTable> FISHING_POWER_POINT = getLootTableKey("fishing/power_point");

    public static final ResourceKey<LootTable> SHRINE_LESS = getLootTableKey("chest/shrine_less");
    public static final ResourceKey<LootTable> SHRINE_MORE = getLootTableKey("chest/shrine_more");

    public static final ResourceKey<LootTable> SPAWN_BONUS = getLootTableKey("chest/spawn_bonus");
    public static final ResourceKey<LootTable> NORMAL_BACKPACK = getLootTableKey("chest/normal_backpack");
    public static final ResourceKey<LootTable> FURNACE_OR_CRAFTING_TABLE_BACKPACK = getLootTableKey("chest/furnace_or_crafting_table_backpack");
    public static final ResourceKey<LootTable> TANK_BACKPACK = getLootTableKey("chest/tank_backpack");
    public static final ResourceKey<LootTable> ENDER_CHEST_BACKPACK = getLootTableKey("chest/ender_chest_backpack");

    public static final ResourceKey<LootTable> NORMAL_BAUBLE = getLootTableKey("chest/normal_bauble");
    public static final ResourceKey<LootTable> RARE_BAUBLE = getLootTableKey("chest/rare_bauble");
    public static final ResourceKey<LootTable> VERY_RARE_BAUBLE = getLootTableKey("chest/very_rare_bauble");

    public static final ResourceKey<LootTable> STRUCTURE_SPAWN_MAID_GIFT = getLootTableKey("chest/structure_spawn_maid_gift");
    public static final ResourceKey<LootTable> MAID_BURIED_TREASURE = getLootTableKey("chest/maid_buried_treasure");

    public static final ResourceKey<LootTable> RANDOM_BOARD_STATE = getLootTableKey("chest/random_board_state");

    public static ResourceKey<LootTable> getLootTableKey(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, name));
    }

    public static record AdvancementLootTables(HolderLookup.Provider registries) implements LootTableSubProvider {
        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(GIVE_SMART_SLAB, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(InitItems.SMART_SLAB_INIT))));

            consumer.accept(ADVANCEMENT_POWER_POINT, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(5))
                    .add(LootItem.lootTableItem(InitItems.POWER_POINT.get()))));

            consumer.accept(CAKE, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.CAKE))));
        }
    }

    @SuppressWarnings("all")
    public static record ChestLootTables(HolderLookup.Provider registries) implements LootTableSubProvider {
        @Override
        public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            consumer.accept(CHEST_POWER_POINT, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(InitItems.POWER_POINT.get())
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2))))
                    .add(EmptyLootItem.emptyItem().setWeight(2))));

            consumer.accept(FISHING_POWER_POINT, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(InitItems.POWER_POINT.get()))
                    .add(EmptyLootItem.emptyItem().setWeight(9))));

            consumer.accept(SHRINE_LESS, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(InitItems.SHRINE.get()))
                    .add(EmptyLootItem.emptyItem().setWeight(9))));

            consumer.accept(SHRINE_MORE, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(InitItems.SHRINE.get()))
                    .add(EmptyLootItem.emptyItem().setWeight(2))));

            consumer.accept(SPAWN_BONUS, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(InitItems.MAID_BACKPACK_SMALL.get()).setWeight(3))
                            .add(LootItem.lootTableItem(InitItems.MAID_BACKPACK_MIDDLE.get()).setWeight(9))
                            .add(LootItem.lootTableItem(InitItems.MAID_BACKPACK_BIG.get()).setWeight(4)))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(InitItems.POWER_POINT.get())
                                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 9))))
                    ));

            consumer.accept(NORMAL_BACKPACK, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(InitItems.MAID_BACKPACK_SMALL.get()).setWeight(3))
                    .add(LootItem.lootTableItem(InitItems.MAID_BACKPACK_MIDDLE.get()).setWeight(9))
                    .add(LootItem.lootTableItem(InitItems.MAID_BACKPACK_BIG.get()).setWeight(4))
                    .add(EmptyLootItem.emptyItem().setWeight(50))));

            consumer.accept(FURNACE_OR_CRAFTING_TABLE_BACKPACK, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(InitItems.FURNACE_BACKPACK.get()))
                    .add(LootItem.lootTableItem(InitItems.CRAFTING_TABLE_BACKPACK.get()))
                    .add(EmptyLootItem.emptyItem().setWeight(8))));

            var tank1 = LootItem.lootTableItem(InitItems.TANK_BACKPACK.get()).apply(new SetTankCountFunction.Builder(Fluids.LAVA, 9));
            var tank2 = LootItem.lootTableItem(InitItems.TANK_BACKPACK.get()).apply(new SetTankCountFunction.Builder(Fluids.LAVA, 4));
            var tank3 = LootItem.lootTableItem(InitItems.TANK_BACKPACK.get()).apply(new SetTankCountFunction.Builder(Fluids.LAVA, 3));

            consumer.accept(TANK_BACKPACK, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(tank1).add(tank2).add(tank3)
                    .add(EmptyLootItem.emptyItem().setWeight(12))));

            consumer.accept(ENDER_CHEST_BACKPACK, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(InitItems.ENDER_CHEST_BACKPACK.get()).setWeight(1))
                    .add(EmptyLootItem.emptyItem().setWeight(4))));

            consumer.accept(NORMAL_BAUBLE, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(UniformGenerator.between(1, 3))
                    // 有附魔的饰品
                    .add(LootItem.lootTableItem(InitItems.EXPLOSION_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
                    .add(LootItem.lootTableItem(InitItems.FIRE_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
                    .add(LootItem.lootTableItem(InitItems.PROJECTILE_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
                    .add(LootItem.lootTableItem(InitItems.MAGIC_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
                    .add(LootItem.lootTableItem(InitItems.FALL_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
                    .add(LootItem.lootTableItem(InitItems.DROWN_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
                    // 没有附魔的饰品
                    .add(LootItem.lootTableItem(InitItems.EXPLOSION_PROTECT_BAUBLE.get()).setWeight(4))
                    .add(LootItem.lootTableItem(InitItems.FIRE_PROTECT_BAUBLE.get()).setWeight(4))
                    .add(LootItem.lootTableItem(InitItems.PROJECTILE_PROTECT_BAUBLE.get()).setWeight(4))
                    .add(LootItem.lootTableItem(InitItems.MAGIC_PROTECT_BAUBLE.get()).setWeight(4))
                    .add(LootItem.lootTableItem(InitItems.FALL_PROTECT_BAUBLE.get()).setWeight(4))
                    .add(LootItem.lootTableItem(InitItems.DROWN_PROTECT_BAUBLE.get()).setWeight(4))
                    // 其他
                    .add(EmptyLootItem.emptyItem().setWeight(90))));

            consumer.accept(RARE_BAUBLE, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(UniformGenerator.between(1, 2))
                    .add(LootItem.lootTableItem(InitItems.NIMBLE_FABRIC.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
                    .add(LootItem.lootTableItem(InitItems.NIMBLE_FABRIC.get()))
                    .add(LootItem.lootTableItem(InitItems.ITEM_MAGNET_BAUBLE.get()))
                    .add(EmptyLootItem.emptyItem().setWeight(6))));

            consumer.accept(VERY_RARE_BAUBLE, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(InitItems.ULTRAMARINE_ORB_ELIXIR.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment(this.registries)))
                    .add(LootItem.lootTableItem(InitItems.ULTRAMARINE_ORB_ELIXIR.get()).setWeight(2))
                    .add(EmptyLootItem.emptyItem().setWeight(4))));

            var setDamage = SetItemDamageFunction.setDamage(UniformGenerator.between(0.06f, 0.1f));
            consumer.accept(STRUCTURE_SPAWN_MAID_GIFT, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(1, 2))
                            .add(LootItem.lootTableItem(Items.CAKE)))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(InitItems.CAMERA.get()).apply(setDamage))));

            consumer.accept(MAID_BURIED_TREASURE, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(InitItems.SMART_SLAB_EMPTY.get()))
                            .add(EmptyLootItem.emptyItem().setWeight(4)))
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(InitItems.SHRINE.get()))
                            .add(EmptyLootItem.emptyItem())));

            var library = RandomBoardStateFunction.create().addTag("library");
            consumer.accept(RANDOM_BOARD_STATE, LootTable.lootTable()
                    .withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(InitItems.GOMOKU_BOARD_STATE.get()).apply(library))
                            .add(LootItem.lootTableItem(InitItems.CCHESS_BOARD_STATE.get()).apply(library))
                            .add(LootItem.lootTableItem(InitItems.WCHESS_BOARD_STATE.get()).apply(library))));
        }
    }

    public static class EntityLootTables extends EntityLootSubProvider {
        public final Set<EntityType<?>> knownEntities = Sets.newHashSet();

        protected EntityLootTables(HolderLookup.Provider registries) {
            super(FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        public void generate() {
            add(InitEntities.BOX.get(), LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.PAPER))));
        }

        @Override
        protected boolean canHaveLootTable(EntityType<?> type) {
            return true;
        }

        @Override
        protected Stream<EntityType<?>> getKnownEntityTypes() {
            return knownEntities.stream();
        }

        @Override
        protected void add(EntityType<?> type, LootTable.Builder builder) {
            this.add(type, type.getDefaultLootTable(), builder);
        }

        @Override
        protected void add(EntityType<?> type, ResourceKey<LootTable> lootTable, LootTable.Builder builder) {
            super.add(type, lootTable, builder);
            knownEntities.add(type);
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
