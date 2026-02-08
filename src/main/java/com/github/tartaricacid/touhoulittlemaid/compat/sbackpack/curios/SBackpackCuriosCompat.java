package com.github.tartaricacid.touhoulittlemaid.compat.sbackpack.curios;

import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;

public class SBackpackCuriosCompat {
    private static final int DEFAULT_PRIORITY = 100;
    /**
     * Curios 槽位中背包优先级，决定背包的拾取顺序
     * 优先交互 back 槽位中的背包
     */
    private static final Map<String, Integer> SLOT_PRIORITY = Util.make(Maps.newHashMap(), map -> {
        map.put("back", 0);
        map.put("trinkets", 1);
    });

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new BackpackCuriosEquipEventHandler());
        MinecraftForge.EVENT_BUS.register(new BackpackPickupEventHandler());
        MinecraftForge.EVENT_BUS.register(new BackpackRequestItemEventHandler());
    }

    public static int getSlotPriority(String slotType) {
        return SLOT_PRIORITY.getOrDefault(slotType, DEFAULT_PRIORITY);
    }
}
