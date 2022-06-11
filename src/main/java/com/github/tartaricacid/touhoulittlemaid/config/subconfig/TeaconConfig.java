package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class TeaconConfig {
    public static ForgeConfigSpec.ConfigValue<List<String>> DOWNLOAD_MODEL_PACK;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("teacon");

        builder.comment("The model pack downloaded at game launch");
        DOWNLOAD_MODEL_PACK = builder.define("DownloadModelPack", Lists.newArrayList());

        builder.pop();
    }
}
