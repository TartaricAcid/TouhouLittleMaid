package com.github.tartaricacid.touhoulittlemaid.advancements;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class GivePatchouliBookConfigTrigger extends SimpleCriterionTrigger<GivePatchouliBookConfigTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "give_patchouli_book_config");

    @Override
    protected GivePatchouliBookConfigTrigger.Instance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext conditionsParser) {
        return new GivePatchouliBookConfigTrigger.Instance(entityPredicate);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }


    public void trigger(ServerPlayer serverPlayer) {
        super.trigger(serverPlayer, instance -> MiscConfig.GIVE_PATCHOULI_BOOK.get());
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(EntityPredicate.Composite player) {
            super(ID, player);
        }
    }
}