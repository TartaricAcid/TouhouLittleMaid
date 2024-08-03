package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ChairConfig {
    public static ModConfigSpec.BooleanValue CHAIR_CHANGE_MODEL;
    public static ModConfigSpec.BooleanValue CHAIR_CAN_DESTROYED_BY_ANYONE;

    public static void init(ModConfigSpec.Builder builder) {
        builder.push("chair");

        builder.comment("Chair can switch models freely");
        CHAIR_CHANGE_MODEL = builder.define("ChairChangeModel", true);

        builder.comment("Chair can be destroyed by anyone");
        CHAIR_CAN_DESTROYED_BY_ANYONE = builder.define("ChairCanDestroyedByAnyone", true);

        builder.pop();
    }
}
