package com.github.tartaricacid.touhoulittlemaid.config.subconfig;


import net.neoforged.neoforge.common.ModConfigSpec;

public class RenderConfig {
    private static final String TRANSLATE_KEY = "config.touhou_little_maid.render";

    public static ModConfigSpec.BooleanValue ENABLE_COMPASS_TIP;
    public static ModConfigSpec.BooleanValue ENABLE_GOLDEN_APPLE_TIP;
    public static ModConfigSpec.BooleanValue ENABLE_POTION_TIP;
    public static ModConfigSpec.BooleanValue ENABLE_MILK_BUCKET_TIP;
    public static ModConfigSpec.BooleanValue ENABLE_SCRIPT_BOOK_TIP;
    public static ModConfigSpec.BooleanValue ENABLE_GLASS_BOTTLE_TIP;
    public static ModConfigSpec.BooleanValue ENABLE_NAME_TAG_TIP;
    public static ModConfigSpec.BooleanValue ENABLE_LEAD_TIP;
    public static ModConfigSpec.BooleanValue ENABLE_SADDLE_TIP;
    public static ModConfigSpec.BooleanValue ENABLE_SHEARS_TIP;

    public static void init(ModConfigSpec.Builder builder) {
        builder.translation(TRANSLATE_KEY).push("render");

        ENABLE_COMPASS_TIP = builder.translation(translateKey("enable_compass_tip")).define("EnableCompassTip", true);
        ENABLE_GOLDEN_APPLE_TIP = builder.translation(translateKey("enable_golden_apple_tip")).define("EnableGoldenAppleTip", true);
        ENABLE_POTION_TIP = builder.translation(translateKey("enable_potion_tip")).define("EnablePotionTip", true);
        ENABLE_MILK_BUCKET_TIP = builder.translation(translateKey("enable_milk_bucket_tip")).define("EnableMilkBucketTip", true);
        ENABLE_SCRIPT_BOOK_TIP = builder.translation(translateKey("enable_script_book_tip")).define("EnableScriptBookTip", true);
        ENABLE_GLASS_BOTTLE_TIP = builder.translation(translateKey("enable_glass_bottle_tip")).define("EnableGlassBottleTip", true);
        ENABLE_NAME_TAG_TIP = builder.translation(translateKey("enable_name_tag_tip")).define("EnableNameTagTip", true);
        ENABLE_LEAD_TIP = builder.translation(translateKey("enable_lead_tip")).define("EnableLeadTip", true);
        ENABLE_SADDLE_TIP = builder.translation(translateKey("enable_saddle_tip")).define("EnableSaddleTip", true);
        ENABLE_SHEARS_TIP = builder.translation(translateKey("enable_shears_tip")).define("EnableShearsTip", true);

        builder.pop();
    }

    private static String translateKey(String key) {
        return TRANSLATE_KEY + "." + key;
    }
}
