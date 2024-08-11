package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.loot.AdditionLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public final class InitLootModifier {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZER = DeferredRegister.create(NeoForgeRegistries.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TouhouLittleMaid.MOD_ID);

    public static final Supplier<MapCodec<AdditionLootModifier>> ADDITION = GLOBAL_LOOT_MODIFIER_SERIALIZER.register("addition", AdditionLootModifier.CODEC);
}