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
    public static final DeferredHolder<CriterionTrigger<?>, GivePatchouliBookConfigTrigger> MADE_TF_PORTAL = TRIGGERS.register("give_patchouli_book", GivePatchouliBookConfigTrigger::new);
    public static final DeferredHolder<CriterionTrigger<?>, GiveSmartSlabConfigTrigger> CONSUME_HYDRA_CHOP = TRIGGERS.register("give_smart_slab", GiveSmartSlabConfigTrigger::new);
}
