package com.github.tartaricacid.touhoulittlemaid.compat.ponder;


import net.neoforged.fml.ModList;

public class PonderCompat {
    public static final String MOD_ID = "ponder";

    public static void register() {
        if (ModList.get().isLoaded(MOD_ID)) {
            MaidPonderPlugin.register();
        }
    }
}
