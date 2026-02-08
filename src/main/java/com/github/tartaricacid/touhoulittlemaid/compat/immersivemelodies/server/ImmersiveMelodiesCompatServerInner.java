package com.github.tartaricacid.touhoulittlemaid.compat.immersivemelodies.server;

import immersive_melodies.item.InstrumentItem;
import net.minecraft.world.item.ItemStack;

public class ImmersiveMelodiesCompatServerInner {
    static boolean isInstrumentItem(ItemStack stack) {
        return stack.getItem() instanceof InstrumentItem;
    }
}
