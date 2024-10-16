package com.github.tartaricacid.touhoulittlemaid.advancements.maid;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

public class MaidEventTrigger extends SimpleCriterionTrigger<MaidEventTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "maid/tamed_maid");

    @Override
    protected MaidEventTrigger.Instance createInstance(JsonObject json, ContextAwarePredicate entityPredicate, DeserializationContext conditionsParser) {
        String eventName = GsonHelper.getAsString(json, "event");
        return new MaidEventTrigger.Instance(eventName);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    public void trigger(ServerPlayer serverPlayer, String eventName) {
        super.trigger(serverPlayer, instance -> instance.matches(eventName));
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        private final String eventName;

        public Instance(String eventName) {
            super(ID, ContextAwarePredicate.ANY);
            this.eventName = eventName;
        }

        public boolean matches(String eventNameIn) {
            return this.eventName.equals(eventNameIn);
        }

        @Override
        public JsonObject serializeToJson(SerializationContext context) {
            JsonObject jsonObject = super.serializeToJson(context);
            jsonObject.addProperty("event", this.eventName);
            return jsonObject;
        }
    }
}
