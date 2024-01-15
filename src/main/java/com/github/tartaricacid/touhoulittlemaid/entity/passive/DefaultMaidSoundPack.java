package com.github.tartaricacid.touhoulittlemaid.entity.passive;

public final class DefaultMaidSoundPack {
    public static final String DEFAULT_SOUND_PACK_ID = "touhou_little_maid";
    public static final String PECO_SOUND_PACK_ID = "littlemaid_peco";
    private static final double PECO_CHANCE = 0.75;

    public static String getInitSoundPackId() {
        if (Math.random() < PECO_CHANCE) {
            return PECO_SOUND_PACK_ID;
        }
        return DEFAULT_SOUND_PACK_ID;
    }
}
