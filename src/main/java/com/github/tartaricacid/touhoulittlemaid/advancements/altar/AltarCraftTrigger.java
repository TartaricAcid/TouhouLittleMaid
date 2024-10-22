package com.github.tartaricacid.touhoulittlemaid.advancements.altar;

import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class AltarCraftTrigger extends SimpleCriterionTrigger<AltarCraftTrigger.Instance> {
    public void trigger(ServerPlayer serverPlayer, ResourceLocation recipeId) {
        super.trigger(serverPlayer, instance -> instance.matches(recipeId));
    }

    @Override
    public Codec<AltarCraftTrigger.Instance> codec() {
        return AltarCraftTrigger.Instance.CODEC;
    }

    public record Instance(Optional<ContextAwarePredicate> player,
                           ResourceLocation recipeId) implements SimpleInstance {
        public static final Codec<AltarCraftTrigger.Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(AltarCraftTrigger.Instance::player),
                        ResourceLocation.CODEC.fieldOf("recipe_id").forGetter(AltarCraftTrigger.Instance::recipeId))
                .apply(instance, AltarCraftTrigger.Instance::new));

        public boolean matches(ResourceLocation recipeIdIn) {
            return this.recipeId.equals(recipeIdIn);
        }

        public static Criterion<Instance> recipe(ResourceLocation recipeId) {
            return InitTrigger.ALTAR_CRAFT.get().createCriterion(new AltarCraftTrigger.Instance(Optional.empty(), recipeId));
        }
    }
}
