package com.github.tartaricacid.touhoulittlemaid.entity.data.inner;

import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public record AttackListData(Map<ResourceLocation, MonsterType> attackGroups) {
    private static final Codec<Map<ResourceLocation, MonsterType>> LIST_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, MonsterType.CODEC)
            .xmap(HashMap::new, Function.identity());
    public static final Codec<AttackListData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            LIST_CODEC.fieldOf("attack_groups").forGetter(AttackListData::attackGroups)
    ).apply(instance, AttackListData::new));

    public static AttackListData empty() {
        return new AttackListData(Maps.newHashMap());
    }
}
