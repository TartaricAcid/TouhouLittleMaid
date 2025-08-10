package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack;

import net.minecraftforge.common.MinecraftForge;

public class SBackpackCompat {
    public static void init() {
        MinecraftForge.EVENT_BUS.register(new BackpackRightClickMaidEvent());
    }
}
