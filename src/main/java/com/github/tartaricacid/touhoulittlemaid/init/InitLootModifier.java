package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.loot.AdditionLootModifier;
import com.github.tartaricacid.touhoulittlemaid.loot.RandomBoardStateFunction;
import com.github.tartaricacid.touhoulittlemaid.loot.SetInitMaidOwnerFunction;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class InitLootModifier {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZER = DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TouhouLittleMaid.MOD_ID);

    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADDITION = GLOBAL_LOOT_MODIFIER_SERIALIZER.register("addition", AdditionLootModifier.CODEC);

    public static final LootItemFunctionType BOARD_STATE_RANDOMLY = new LootItemFunctionType(new RandomBoardStateFunction.Serializer());
    public static final LootItemFunctionType SET_INIT_MAID_OWNER = new LootItemFunctionType(new SetInitMaidOwnerFunction.Serializer());

    @SubscribeEvent
    public static void register(RegisterEvent evt) {
        if (evt.getRegistryKey().equals(Registries.LOOT_FUNCTION_TYPE)) {
            Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, new ResourceLocation(TouhouLittleMaid.MOD_ID, "board_state_randomly"), BOARD_STATE_RANDOMLY);
            Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, new ResourceLocation(TouhouLittleMaid.MOD_ID, "set_init_maid_owner"), SET_INIT_MAID_OWNER);
        }
    }
}