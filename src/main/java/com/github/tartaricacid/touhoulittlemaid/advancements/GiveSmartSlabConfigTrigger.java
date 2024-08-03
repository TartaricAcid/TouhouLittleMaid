package com.github.tartaricacid.touhoulittlemaid.advancements;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public class GiveSmartSlabConfigTrigger extends SimpleCriterionTrigger<GiveSmartSlabConfigTrigger.Instance> {
    public static final ResourceLocation ID = new ResourceLocation(TouhouLittleMaid.MOD_ID, "give_smart_slab_config");
    public void trigger(ServerPlayer serverPlayer) {
        super.trigger(serverPlayer, instance -> MiscConfig.GIVE_SMART_SLAB.get());
    }

    @Override
    public Codec<Instance> codec() {
        return Instance.CODEC;
    }

    public record Instance(Optional<ContextAwarePredicate> player) implements SimpleInstance {
        public static final Codec<GiveSmartSlabConfigTrigger.Instance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(GiveSmartSlabConfigTrigger.Instance::player))
                .apply(instance, GiveSmartSlabConfigTrigger.Instance::new));
    }
}
