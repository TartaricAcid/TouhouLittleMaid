package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ChairConfig {
    public static ForgeConfigSpec.BooleanValue CHAIR_CHANGE_MODEL;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("chair");

        builder.comment("Chair can switch models freely");
        CHAIR_CHANGE_MODEL = builder.define("ChairChangeModel", true);

        builder.pop();
    }
}
