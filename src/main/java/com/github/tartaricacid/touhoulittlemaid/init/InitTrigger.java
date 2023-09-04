package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.advancements.GiveSmartSlabConfigTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public final class InitTrigger {
    public static GiveSmartSlabConfigTrigger GIVE_SMART_SLAB_CONFIG;

    public static void init() {
        GIVE_SMART_SLAB_CONFIG = CriteriaTriggers.register(new GiveSmartSlabConfigTrigger());
    }
}
