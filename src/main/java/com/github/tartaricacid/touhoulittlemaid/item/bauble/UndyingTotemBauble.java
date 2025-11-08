package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityEvent;
import net.minecraft.world.item.ItemStack;

public class UndyingTotemBauble implements IMaidBauble {
    @Override
    public boolean onDeath(EntityMaid maid, ItemStack baubleItem, DamageSource source) {
        if (!source.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            baubleItem.shrink(1);
            maid.setHealth(1.0F);
            maid.removeAllEffects();
            maid.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
            maid.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
            maid.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
            maid.level.broadcastEntityEvent(maid, EntityEvent.TALISMAN_ACTIVATE);
            if (maid.getOwner() instanceof ServerPlayer serverPlayer) {
                InitTrigger.MAID_EVENT.get().trigger(serverPlayer, TriggerType.USE_UNDEAD_BAUBLE);
            }
            return true;
        }
        return false;
    }
}
