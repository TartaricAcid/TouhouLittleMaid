package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack;


import net.neoforged.neoforge.common.NeoForge;

public class SBackpackCompat {
    public static void init() {
        NeoForge.EVENT_BUS.register(new BackpackRightClickMaidEvent());
    }
}
