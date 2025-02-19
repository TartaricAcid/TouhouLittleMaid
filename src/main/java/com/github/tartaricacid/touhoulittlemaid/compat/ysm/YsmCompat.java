package com.github.tartaricacid.touhoulittlemaid.compat.ysm;

import com.github.tartaricacid.touhoulittlemaid.compat.ysm.data.YsmModelData;
import net.minecraftforge.fml.ModList;

public class YsmCompat {

    private static final String MOD_ID = "yes_steve_model";
    private static boolean INSTALLED = false;

    public static void init() {
        INSTALLED = ModList.get().isLoaded(MOD_ID);
    }

    public static boolean isInstalled() {
        return INSTALLED;
    }

    public static void initYsmModelData() {
        if (INSTALLED) {
            YsmModelData.buildYsmMaidInfos();
        }
    }
}
