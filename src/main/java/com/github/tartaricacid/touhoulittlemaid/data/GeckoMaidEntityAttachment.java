package com.github.tartaricacid.touhoulittlemaid.data;

import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Mob;

public class GeckoMaidEntityAttachment<T extends Mob> {
    //TODO 这怎么写CODEC？？？
    public static final Codec<GeckoMaidEntityAttachment> CODEC;
    private GeckoMaidEntity<T> maid;

    public GeckoMaidEntityAttachment(GeckoMaidEntity<T> maid) {
        this.maid = maid;
    }
}