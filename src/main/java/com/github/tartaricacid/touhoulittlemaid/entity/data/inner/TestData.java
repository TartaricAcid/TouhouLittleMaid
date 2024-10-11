package com.github.tartaricacid.touhoulittlemaid.entity.data.inner;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.Function;

public record TestData(ItemStack stack, boolean test, int number, List<String> text) {
    public static final Codec<List<String>> TEXT_CODEC = Codec.STRING.listOf().xmap(Lists::newArrayList, Function.identity());

    public static final Codec<TestData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("Item").forGetter(TestData::stack),
            Codec.BOOL.fieldOf("Test").forGetter(TestData::test),
            Codec.INT.fieldOf("Number").forGetter(TestData::number),
            TEXT_CODEC.fieldOf("Text").forGetter(TestData::text)
    ).apply(instance, TestData::new));
}
