package com.github.tartaricacid.touhoulittlemaid.datagen;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.google.common.collect.Sets;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.data.loot.packs.VanillaChestLoot;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantRandomlyFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class LootTableGenerator {
    public static final ResourceLocation ADVANCEMENT_POWER_POINT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "advancement/power_point");
    public static final ResourceLocation CAKE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "advancement/cake");

    public static final ResourceLocation CHEST_POWER_POINT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/power_point");
    public static final ResourceLocation FISHING_POWER_POINT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "fishing/power_point");

    public static final ResourceLocation SHRINE_LESS = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/shrine_less");
    public static final ResourceLocation SHRINE_MORE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/shrine_more");

    public static final ResourceLocation SPAWN_BONUS = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/spawn_bonus");
    public static final ResourceLocation NORMAL_BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/normal_backpack");
    public static final ResourceLocation FURNACE_OR_CRAFTING_TABLE_BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/furnace_or_crafting_table_backpack");
    public static final ResourceLocation TANK_BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/tank_backpack");
    public static final ResourceLocation ENDER_CHEST_BACKPACK = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/ender_chest_backpack");

    public static final ResourceLocation NORMAL_BAUBLE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/normal_bauble");
    public static final ResourceLocation RARE_BAUBLE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/rare_bauble");
    public static final ResourceLocation VERY_RARE_BAUBLE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/very_rare_bauble");

    public static final ResourceLocation STRUCTURE_SPAWN_MAID_GIFT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/structure_spawn_maid_gift");
    public static final ResourceLocation MAID_BURIED_TREASURE = new ResourceLocation(TouhouLittleMaid.MOD_ID, "chest/maid_buried_treasure");

    public static class AdvancementLootTables implements LootTableSubProvider {
        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
            consumer.accept(ADVANCEMENT_POWER_POINT, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(5))
                    .add(LootItem.lootTableItem(InitItems.POWER_POINT.get()))));

            consumer.accept(CAKE, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.CAKE))));
        }
    }

    @SuppressWarnings("all")
    public static class ChestLootTables extends VanillaChestLoot {
        @Override
        public void generate(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
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

            var tank1 = LootItem.lootTableItem(InitItems.TANK_BACKPACK.get()).apply(SetNbtFunction.setTag(getLavaFluidStackTag(9)));
            var tank2 = LootItem.lootTableItem(InitItems.TANK_BACKPACK.get()).apply(SetNbtFunction.setTag(getLavaFluidStackTag(4)));
            var tank3 = LootItem.lootTableItem(InitItems.TANK_BACKPACK.get()).apply(SetNbtFunction.setTag(getLavaFluidStackTag(3)));

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
                    .add(LootItem.lootTableItem(InitItems.EXPLOSION_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                    .add(LootItem.lootTableItem(InitItems.FIRE_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                    .add(LootItem.lootTableItem(InitItems.PROJECTILE_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                    .add(LootItem.lootTableItem(InitItems.MAGIC_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                    .add(LootItem.lootTableItem(InitItems.FALL_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                    .add(LootItem.lootTableItem(InitItems.DROWN_PROTECT_BAUBLE.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
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
                    .add(LootItem.lootTableItem(InitItems.NIMBLE_FABRIC.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
                    .add(LootItem.lootTableItem(InitItems.NIMBLE_FABRIC.get()))
                    .add(LootItem.lootTableItem(InitItems.ITEM_MAGNET_BAUBLE.get()))
                    .add(EmptyLootItem.emptyItem().setWeight(6))));

            consumer.accept(VERY_RARE_BAUBLE, LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(InitItems.ULTRAMARINE_ORB_ELIXIR.get()).apply(EnchantRandomlyFunction.randomApplicableEnchantment()))
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
        }

        @NotNull
        private CompoundTag getLavaFluidStackTag(int count) {
            CompoundTag tankTag = new CompoundTag();
            FluidStack fluidStack = new FluidStack(Fluids.LAVA, count * FluidType.BUCKET_VOLUME);
            tankTag.put("Tanks", fluidStack.writeToNBT(new CompoundTag()));
            return tankTag;
        }
    }

    public static class EntityLootTables extends EntityLootSubProvider {
        public final Set<EntityType<?>> knownEntities = Sets.newHashSet();

        protected EntityLootTables() {
            super(FeatureFlags.REGISTRY.allFlags());
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
        protected void add(EntityType<?> type, ResourceLocation lootTable, LootTable.Builder builder) {
            super.add(type, lootTable, builder);
            knownEntities.add(type);
        }
    }
}
