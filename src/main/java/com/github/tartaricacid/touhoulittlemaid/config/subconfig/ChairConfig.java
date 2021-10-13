package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ChairConfig {
    public static ForgeConfigSpec.BooleanValue CHAIR_CHANGE_MODEL;
    public static ForgeConfigSpec.BooleanValue CHAIR_CAN_DESTROYED_BY_ANYONE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("chair");

        builder.comment("Chair can switch models freely");
        CHAIR_CHANGE_MODEL = builder.define("ChairChangeModel", true);

        builder.comment("Chair can be destroyed by anyone");
        CHAIR_CAN_DESTROYED_BY_ANYONE = builder.define("ChairCanDestroyedByAnyone", true);

        builder.pop();
    }
}
