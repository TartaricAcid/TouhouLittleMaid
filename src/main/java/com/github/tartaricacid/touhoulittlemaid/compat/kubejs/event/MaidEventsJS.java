package com.github.tartaricacid.touhoulittlemaid.compat.kubejs.event;

import com.github.tartaricacid.touhoulittlemaid.compat.kubejs.event.common.*;
import dev.latvian.mods.kubejs.plugin.builtin.event.ItemEvents;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.event.EventTargetType;
import dev.latvian.mods.kubejs.event.TargetedEventHandler;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public interface MaidEventsJS {
    EventGroup GROUP = EventGroup.of("MaidEvents");

    EventHandler ADD_JADE_INFO = GROUP.common("addJadeInfo", () -> AddJadeInfoEventJS.class);
    EventHandler ADD_TOP_INFO = GROUP.common("addTopInfo", () -> AddTopInfoEventJS.class);
    TargetedEventHandler<ResourceKey<Item>> INTERACT_MAID = GROUP.common("interactMaid", () -> InteractMaidEventJS.class).supportsTarget(ItemEvents.TARGET).hasResult();
    TargetedEventHandler<ResourceKey<Item>> MAID_AFTER_EAT = GROUP.common("maidAfterEat", () -> MaidAfterEatEventJS.class).supportsTarget(ItemEvents.TARGET);
    TargetedEventHandler<ResourceLocation> MAID_ATTACK = GROUP.common("maidAttack", () -> MaidAttackEventJS.class).supportsTarget(EventTargetType.ID).hasResult();
    TargetedEventHandler<ResourceLocation> MAID_DAMAGE = GROUP.common("maidDamage", () -> MaidDamageEventJS.class).supportsTarget(EventTargetType.ID).hasResult();
    TargetedEventHandler<ResourceLocation> MAID_DEATH = GROUP.common("maidDeath", () -> MaidDeathEventJS.class).supportsTarget(EventTargetType.ID).hasResult();
    TargetedEventHandler<ResourceKey<Item>> MAID_EQUIP = GROUP.common("maidEquip", () -> MaidEquipEventJS.class).supportsTarget(ItemEvents.TARGET);
    EventHandler MAID_FISHED = GROUP.common("maidFished", () -> MaidFishedEventJS.class);
    TargetedEventHandler<ResourceLocation> MAID_HURT = GROUP.common("maidHurt", () -> MaidHurtEventJS.class).supportsTarget(EventTargetType.ID).hasResult();
    EventHandler MAID_PICKUP_ITEM_RESULT_PRE = GROUP.common("maidPickupItemResultPre", () -> MaidPickupEventJS.ItemResultPre.class).hasResult();
    EventHandler MAID_PICKUP_ITEM_RESULT_POST = GROUP.common("maidPickupItemResultPost", () -> MaidPickupEventJS.ItemResultPost.class);
    EventHandler MAID_PICKUP_EXPERIENCE_RESULT = GROUP.common("maidPickupExperienceResult", () -> MaidPickupEventJS.ExperienceResult.class).hasResult();
    EventHandler MAID_PICKUP_ARROW_RESULT = GROUP.common("maidPickupArrowResult", () -> MaidPickupEventJS.ArrowResult.class).hasResult();
    EventHandler MAID_PICKUP_POWER_POINT_RESULT = GROUP.common("maidPickupPowerPointResult", () -> MaidPickupEventJS.PowerPointResult.class).hasResult();
    EventHandler MAID_PLAY_SOUND = GROUP.common("maidPlaySound", () -> MaidPlaySoundEventJS.class).hasResult();
    EventHandler MAID_TICK = GROUP.common("maidTick", () -> MaidTickEventJS.class).hasResult();
    TargetedEventHandler<ResourceLocation> MAID_TASK_ENABLE = GROUP.common("maidTaskEnable", () -> MaidTaskEnableEventJS.class).supportsTarget(EventTargetType.ID).hasResult();
    EventHandler MAID_TAMED = GROUP.common("maidTamed", () -> MaidTamedEventJS.class);
}