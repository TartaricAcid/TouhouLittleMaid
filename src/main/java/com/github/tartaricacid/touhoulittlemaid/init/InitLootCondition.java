package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.loot.LootTableTypeCondition;
import com.github.tartaricacid.touhoulittlemaid.loot.SetTankCountFunction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class InitLootCondition {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, TouhouLittleMaid.MOD_ID);
    public static final DeferredRegister<LootItemFunctionType<?>> LOOT_CONDITION_FUNCTIONS = DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, TouhouLittleMaid.MOD_ID);

    public static final Supplier<LootItemConditionType> LOOT_TABLE_TYPE = LOOT_CONDITION_TYPES.register("loot_table_type", () -> new LootItemConditionType(LootTableTypeCondition.CODEC));
    public static final Supplier<LootItemFunctionType<? extends LootItemConditionalFunction>> SET_TANK_COUNT_FUNCTION = LOOT_CONDITION_FUNCTIONS.register("set_tank_count", () -> new LootItemFunctionType<>(SetTankCountFunction.CODEC));
}