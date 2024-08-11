package com.github.tartaricacid.touhoulittlemaid.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class AdditionLootModifier extends LootModifier {
    public static final MapCodec<AdditionLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            LootModifier.codecStart(inst).and(inst.group(
                    Codec.STRING.fieldOf("string").forGetter(o -> o.string),
                    BuiltInRegistries.ITEM.byNameCodec().listOf().fieldOf("items").forGetter(o -> o.items)
            )).apply(inst, AdditionLootModifier::new)
    );
    //TODO 这两个field代表什么
    List<Item> items;
    String string;


    public AdditionLootModifier(LootItemCondition[] conditionsIn, String string, List<Item> items) {
        super(conditionsIn);
        this.string = string;
        this.items = items;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        items.forEach(item -> generatedLoot.add(new ItemStack(item)));
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}