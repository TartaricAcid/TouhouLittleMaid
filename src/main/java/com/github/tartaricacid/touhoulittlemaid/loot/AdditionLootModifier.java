package com.github.tartaricacid.touhoulittlemaid.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;


public class AdditionLootModifier extends LootModifier {
    public static final Supplier<Codec<AdditionLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(inst.group(
                            ResourceLocation.CODEC.fieldOf("parameter_set_name").forGetter((m) -> m.parameterSet),
                            ResourceLocation.CODEC.fieldOf("addition_loot_table").forGetter((m) -> m.parameterSet)
                    ))
                    .apply(inst, AdditionLootModifier::new)));

    private final ResourceLocation parameterSet;
    private final ResourceLocation additionLootTable;

    public AdditionLootModifier(LootItemCondition[] conditionsIn, ResourceLocation parameterSet, ResourceLocation additionLootTable) {
        super(conditionsIn);
        this.parameterSet = parameterSet;
        this.additionLootTable = additionLootTable;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ResourceLocation currentLootTable = context.getQueriedLootTableId();
        if (!currentLootTable.equals(additionLootTable) && parameterSetEquals(context)) {
            LootTable additionTable = context.getLootTable(additionLootTable);
            generatedLoot.addAll(additionTable.getRandomItems(context));
        }
        return generatedLoot;
    }

    private boolean parameterSetEquals(LootContext context) {
        ResourceLocation currentLootTable = context.getQueriedLootTableId();
        LootTable lootTable = context.getLootTable(currentLootTable);
        return Objects.equals(lootTable.getParamSet(), LootContextParamSets.get(parameterSet));
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
