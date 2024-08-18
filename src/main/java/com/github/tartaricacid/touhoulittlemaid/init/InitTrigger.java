package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.advancements.GivePatchouliBookConfigTrigger;
import com.github.tartaricacid.touhoulittlemaid.advancements.GiveSmartSlabConfigTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class InitTrigger {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGERS = DeferredRegister.create(Registries.TRIGGER_TYPE, TouhouLittleMaid.MOD_ID);
    public static final DeferredHolder<CriterionTrigger<?>, GiveSmartSlabConfigTrigger> GIVE_SMART_SLAB_CONFIG = TRIGGERS.register("give_smart_slab_config", GiveSmartSlabConfigTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, GivePatchouliBookConfigTrigger> GIVE_PATCHOULI_BOOK_CONFIG = TRIGGERS.register("give_patchouli_book_config", GivePatchouliBookConfigTrigger::new);
}
