package com.github.tartaricacid.touhoulittlemaid.compat.neat;

import java.lang.reflect.Field;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class NeatCompat {

    static {
        MinecraftForge.EVENT_BUS.register(NeatCompat.class);
    }

    public static void init() {
        try {
            Class<?> clazz = Class.forName("vazkii.neat.NeatConfig");
            Field field = clazz.getDeclaredField("blacklist");
            List<String> blacklist = (List<String>) field.get(null);
            blacklist = Lists.newArrayList(blacklist);
            if (!blacklist.contains("touhou_little_maid.marisa_broom")) {
                blacklist.add("touhou_little_maid.marisa_broom");
            }
            if (!blacklist.contains("touhou_little_maid.chair")) {
                blacklist.add("touhou_little_maid.chair");
            }
            field.set(null, blacklist);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals("neat")) {
            init();
        }
    }
}
