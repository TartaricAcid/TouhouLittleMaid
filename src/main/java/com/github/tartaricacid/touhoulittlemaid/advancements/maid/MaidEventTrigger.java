package com.github.tartaricacid.touhoulittlemaid.advancements.maid;

import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class MaidEventTrigger extends SimpleCriterionTrigger<MaidEventTrigger.Instance> {
    public static Criterion<Instance> create(String eventName) {
        return InitTrigger.MAID_EVENT.get().createCriterion(new MaidEventTrigger.Instance(Optional.empty(), eventName));
    }

    public void trigger(ServerPlayer serverPlayer, String eventName) {
        super.trigger(serverPlayer, instance -> instance.matches(eventName));
    }

    @Override
    public Codec<MaidEventTrigger.Instance> codec() {
        return MaidEventTrigger.Instance.CODEC;
    }

    public record Instance(Optional<ContextAwarePredicate> player, String eventName) implements SimpleInstance {
        public static final Codec<MaidEventTrigger.Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(MaidEventTrigger.Instance::player),
                        Codec.STRING.fieldOf("event").forGetter(MaidEventTrigger.Instance::eventName))
                .apply(instance, MaidEventTrigger.Instance::new));

        public boolean matches(String eventNameIn) {
            return this.eventName.equals(eventNameIn);
        }
    }
}
