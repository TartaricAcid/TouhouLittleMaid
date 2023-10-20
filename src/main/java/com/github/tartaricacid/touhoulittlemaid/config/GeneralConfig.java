package com.github.tartaricacid.touhoulittlemaid.config;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.ChairConfig;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.VanillaConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public final class GeneralConfig {
    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        MaidConfig.init(builder);
        ChairConfig.init(builder);
        MiscConfig.init(builder);
        VanillaConfig.init(builder);
        return builder.build();
    }
}
