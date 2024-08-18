package com.github.tartaricacid.touhoulittlemaid.config.subconfig;

import net.neoforged.neoforge.common.ModConfigSpec;

public class VanillaConfig {
    private static final String translateKey = "config.touhou_little_maid.vanilla";
    public static ModConfigSpec.BooleanValue REPLACE_SLIME_MODEL;
    public static ModConfigSpec.BooleanValue REPLACE_XP_TEXTURE;
    public static ModConfigSpec.BooleanValue REPLACE_TOTEM_TEXTURE;
    public static ModConfigSpec.BooleanValue REPLACE_XP_BOTTLE_TEXTURE;

    public static void init(ModConfigSpec.Builder builder) {
        builder.translation(translateKey).push("vanilla");

        builder.comment("Whether to replace the vanilla slime model with the yukkuri.")
                .translation(translateKey("replace_slime_model"));
        REPLACE_SLIME_MODEL = builder.define("ReplaceSlimeModel", true);

        builder.comment("Whether to replace the vanilla xp orb texture with the point items.")
                .translation(translateKey("replace_xp_texture"));
        REPLACE_XP_TEXTURE = builder.define("ReplaceXPTexture", true);

        builder.comment("Whether to replace the vanilla totem texture with the life point.")
                .translation(translateKey("replace_totem_texture"));
        REPLACE_TOTEM_TEXTURE = builder.define("ReplaceTotemTexture", true);

        builder.comment("Whether to replace the vanilla bottle of xp texture with the point items.")
                .translation(translateKey("replace_xp_bottle_texture"));
        REPLACE_XP_BOTTLE_TEXTURE = builder.define("ReplaceXPBottleTexture", true);

        builder.pop();
    }

    private static String translateKey(String key) {
        return translateKey + "." + key;
    }
}
