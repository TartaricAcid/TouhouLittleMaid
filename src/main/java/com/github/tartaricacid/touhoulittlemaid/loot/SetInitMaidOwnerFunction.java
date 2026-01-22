package com.github.tartaricacid.touhoulittlemaid.loot;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitLootModifier;
import com.github.tartaricacid.touhoulittlemaid.item.ItemSmartSlab;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

import java.util.UUID;

public class SetInitMaidOwnerFunction extends LootItemConditionalFunction {
    protected SetInitMaidOwnerFunction(LootItemCondition[] predicates) {
        super(predicates);
    }

    @Override
    protected ItemStack run(ItemStack stack, LootContext context) {
        if (stack.is(InitItems.SMART_SLAB_INIT.get())) {
            Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
            if (entity instanceof Player player) {
                UUID uuid = player.getUUID();
                ItemSmartSlab.setInitMaidOwner(stack, uuid);
            }
        }
        return stack;
    }

    @Override
    public LootItemFunctionType getType() {
        return InitLootModifier.SET_INIT_MAID_OWNER;
    }

    public static class Serializer extends LootItemConditionalFunction.Serializer<SetInitMaidOwnerFunction> {
        @Override
        public SetInitMaidOwnerFunction deserialize(JsonObject json, JsonDeserializationContext context, LootItemCondition[] predicates) {
            return new SetInitMaidOwnerFunction(predicates);
        }
    }
}
