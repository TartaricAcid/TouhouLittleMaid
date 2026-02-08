package com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies.server;

import net.minecraft.world.item.ItemStack;

public class ImmersiveMelodiesServerCompat {
    private static boolean IS_LOADED = false;

    public static void init() {
        IS_LOADED = true;
    }

    public static boolean isInstrumentItem(ItemStack stack) {
        if (IS_LOADED) {
            return ImmersiveMelodiesCompatServerInner.isInstrumentItem(stack);
        }
        return false;
    }
}
