package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack;

import com.github.tartaricacid.touhoulittlemaid.compat.curios.CuriosCompat;
import com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios.SBackpackCuriosCompat;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;

public class SBackpackCompat {
    private static boolean IS_LOADED = false;

    public static void init() {
        IS_LOADED = true;
        MinecraftForge.EVENT_BUS.register(new BackpackRightClickMaidEvent());
        
        if (CuriosCompat.isLoaded()) SBackpackCuriosCompat.init();
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    public static boolean isBackpack(ItemStack stack) {
        if (stack.isEmpty()) return false;
        return stack.getItem() instanceof BackpackItem;
    }
}
