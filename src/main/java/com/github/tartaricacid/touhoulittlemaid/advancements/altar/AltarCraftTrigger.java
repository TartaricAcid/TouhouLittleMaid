package com.github.tartaricacid.touhoulittlemaid.advancements.altar;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class AltarCraftTrigger extends SimpleCriterionTrigger<AltarCraftTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "altar/altar_craft");

    @Override
    protected AltarCraftTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        ResourceLocation recipeId = new ResourceLocation(GsonHelper.getAsString(json, "recipe_id"));
        return new AltarCraftTrigger.Instance(entityPredicate, recipeId);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer serverPlayer, ResourceLocation recipeId) {
        super.trigger(serverPlayer, instance -> instance.matches(recipeId));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final ResourceLocation recipeId;

        public Instance(ContextAwarePredicate player, ResourceLocation recipeId) {
            super(ID, player);
            this.recipeId = recipeId;
        }

        public static AltarCraftTrigger.Instance recipe(ResourceLocation recipeId) {
            return new AltarCraftTrigger.Instance(ContextAwarePredicate.ANY, recipeId);
        }

        public boolean matches(ResourceLocation recipeIdIn) {
            return this.recipeId.equals(recipeIdIn);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonObject = super.serializeToJson(context);
            jsonObject.addProperty("recipe_id", this.recipeId.toString());
            return jsonObject;
        }
    }
}
