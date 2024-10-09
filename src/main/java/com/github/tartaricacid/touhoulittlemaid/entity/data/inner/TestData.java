package com.github.tartaricacid.touhoulittlemaid.entity.data.inner;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record TestData(ItemStack stack, boolean test, int number) {
    public static final Codec<TestData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("Item").forGetter(TestData::stack),
            Codec.BOOL.fieldOf("Test").forGetter(TestData::test),
            Codec.INT.fieldOf("Number").forGetter(TestData::number)
    ).apply(instance, TestData::new));
}
