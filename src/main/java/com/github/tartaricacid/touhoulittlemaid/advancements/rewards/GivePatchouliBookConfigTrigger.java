package com.github.tartaricacid.touhoulittlemaid.advancements.rewards;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class GivePatchouliBookConfigTrigger extends SimpleCriterionTrigger<GivePatchouliBookConfigTrigger.Instance> {
    public void trigger(ServerPlayer serverPlayer) {
        super.trigger(serverPlayer, instance -> MiscConfig.GIVE_PATCHOULI_BOOK.get());
    }

    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public record Instance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
        public static final Codec<GivePatchouliBookConfigTrigger.Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(GivePatchouliBookConfigTrigger.Instance::player))
                .apply(instance, GivePatchouliBookConfigTrigger.Instance::new));

        public static Criterion<GivePatchouliBookConfigTrigger.Instance> instance() {
            return InitTrigger.GIVE_PATCHOULI_BOOK_CONFIG.get().createCriterion(new GivePatchouliBookConfigTrigger.Instance(Optional.empty()));
        }
    }
}
