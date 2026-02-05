package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios;

import net.minecraftforge.common.MinecraftForge;

import java.util.HashMap;
import java.util.Map;

public class SBackpackCuriosCompat {
    private static boolean IS_LOADED = false;

    /**
     * Curios 槽位中背包优先级，决定背包的拾取顺序
     * 优先交互 back 槽位中的背包
     */
    private static final Map<String, Integer> SLOT_PRIORITY = new HashMap<>();
    private static final int DEFAULT_PRIORITY = 100;

    static {
        SLOT_PRIORITY.put("back", 0);
        SLOT_PRIORITY.put("trinkets", 1);
    }

    public static void init() {
        if (IS_LOADED) return;
        IS_LOADED = true;
        MinecraftForge.EVENT_BUS.register(new BackpackCuriosEquipEventHandler());
        MinecraftForge.EVENT_BUS.register(new BackpackPickupEventHandler());
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    public static int getSlotPriority(String slotType) {
        return SLOT_PRIORITY.getOrDefault(slotType, DEFAULT_PRIORITY);
    }
}
