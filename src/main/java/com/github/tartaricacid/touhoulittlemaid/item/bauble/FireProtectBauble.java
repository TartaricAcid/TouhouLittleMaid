package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityExtinguishingAgent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableFloat;

public class FireProtectBauble implements IMaidBauble {
    @Override
    public boolean onInjured(EntityMaid maid, ItemStack baubleItem, DamageSource source, MutableFloat damage) {
        if (source.is(DamageTypeTags.IS_FIRE)) {
            maid.hurtAndBreak(baubleItem, 1);
            maid.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300));
            if (!maid.level.isClientSide) {
                maid.level.addFreshEntity(new EntityExtinguishingAgent(maid.level, maid.position()));
            }
            if (maid.getOwner() instanceof ServerPlayer serverPlayer) {
                InitTrigger.MAID_EVENT.get().trigger(serverPlayer, TriggerType.USE_PROTECT_BAUBLE);
            }
            return true;
        }
        return false;
    }
}
