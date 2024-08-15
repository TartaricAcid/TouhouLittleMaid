package com.github.tartaricacid.touhoulittlemaid.loot;

import com.github.tartaricacid.touhoulittlemaid.dataGen.LootTableGenerator;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AdditionLootModifier extends LootModifier {
    public static final MapCodec<AdditionLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> LootModifier.codecStart(inst).apply(inst, AdditionLootModifier::new));

    public AdditionLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (generatedLoot.getFirst().is(Items.CHEST)) {
            context.getResolver().get(Registries.LOOT_TABLE, LootTableGenerator.POWER_POINT)
                    .ifPresent(extraTable -> extraTable.value().getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add)));
            context.getResolver().get(Registries.LOOT_TABLE, LootTableGenerator.SHRINE)
                    .ifPresent(extraTable -> extraTable.value().getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add)));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
