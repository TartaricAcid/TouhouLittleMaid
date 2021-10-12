package com.github.tartaricacid.touhoulittlemaid.loot;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class AdditionLootModifier extends LootModifier {
    private final ResourceLocation parameterSet;
    private final ResourceLocation additionLootTable;

    public AdditionLootModifier(ILootCondition[] conditionsIn, ResourceLocation parameterSet, ResourceLocation additionLootTable) {
        super(conditionsIn);
        this.parameterSet = parameterSet;
        this.additionLootTable = additionLootTable;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
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
        return Objects.equals(lootTable.getParamSet(), LootParameterSets.get(parameterSet));
    }

    public static class Serializer extends GlobalLootModifierSerializer<AdditionLootModifier> {
        @Override
        public AdditionLootModifier read(ResourceLocation location, JsonObject object, ILootCondition[] conditions) {
            String parameterSet = JSONUtils.getAsString(object, "parameter_set_name");
            String additionLootTable = JSONUtils.getAsString(object, "addition_loot_table");
            return new AdditionLootModifier(conditions, new ResourceLocation(parameterSet), new ResourceLocation(additionLootTable));
        }

        @Override
        public JsonObject write(AdditionLootModifier instance) {
            JsonObject object = makeConditions(instance.conditions);
            object.addProperty("parameter_set_name", instance.parameterSet.toString());
            object.addProperty("addition_loot_table", instance.additionLootTable.toString());
            return object;
        }
    }
}
