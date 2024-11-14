package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.advancements.altar.AltarCraftTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.maid.MaidEventTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.rewards.GivePatchouliBookConfigTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.rewards.GiveSmartSlabConfigTrigger;
import net.minecraft.advancements.CriteriaTriggers;

public final class InitTrigger {
    public static GiveSmartSlabConfigTrigger GIVE_SMART_SLAB_CONFIG;
    public static GivePatchouliBookConfigTrigger GIVE_PATCHOULI_BOOK_CONFIG;

    public static AltarCraftTrigger ALTAR_CRAFT;

    public static MaidEventTrigger MAID_EVENT;

    public static void init() {
        GIVE_SMART_SLAB_CONFIG = CriteriaTriggers.register(new GiveSmartSlabConfigTrigger());
        GIVE_PATCHOULI_BOOK_CONFIG = CriteriaTriggers.register(new GivePatchouliBookConfigTrigger());

        ALTAR_CRAFT = CriteriaTriggers.register(new AltarCraftTrigger());
        MAID_EVENT = CriteriaTriggers.register(new MaidEventTrigger());
    }
}
