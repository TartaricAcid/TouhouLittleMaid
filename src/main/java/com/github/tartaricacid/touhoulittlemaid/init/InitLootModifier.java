package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.loot.AdditionLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitLootModifier {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLOBAL_LOOT_MODIFIER_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, TouhouLittleMaid.MOD_ID);

    public static RegistryObject<AdditionLootModifier.Serializer> ADDITION = GLOBAL_LOOT_MODIFIER_SERIALIZER.register("addition", AdditionLootModifier.Serializer::new);
}
