package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ChairConfig {
    private static final String TRANSLATE_KEY = "config.touhou_little_maid.chair";
    public static ModConfigSpec.BooleanValue CHAIR_CHANGE_MODEL;
    public static ModConfigSpec.BooleanValue CHAIR_CAN_DESTROYED_BY_ANYONE;

    public static void init(ModConfigSpec.Builder builder) {
        builder.translation(TRANSLATE_KEY).push("chair");

        builder.comment("Chair can switch models freely")
                .translation(translateKey("chair_change_model"));
        CHAIR_CHANGE_MODEL = builder.define("ChairChangeModel", true);

        builder.comment("Chair can be destroyed by anyone")
                .translation(translateKey("chair_can_destroyed_by_anyone"));
        CHAIR_CAN_DESTROYED_BY_ANYONE = builder.define("ChairCanDestroyedByAnyone", true);

        builder.pop();
    }

    private static String translateKey(String key) {
        return TRANSLATE_KEY + "." + key;
    }
}
