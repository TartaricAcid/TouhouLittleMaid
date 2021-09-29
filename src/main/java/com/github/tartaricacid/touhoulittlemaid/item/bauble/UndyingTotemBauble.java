package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDeathEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class UndyingTotemBauble implements IMaidBauble {
    public UndyingTotemBauble() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(MaidDeathEvent event) {
        EntityMaid maid = event.getMaid();
        DamageSource source = event.getSource();
        if (!source.isBypassInvul()) {
            int slot = ItemsUtil.getBaubleSlotInMaid(maid, this);
            if (slot >= 0) {
                maid.getMaidBauble().setStackInSlot(slot, ItemStack.EMPTY);
                event.setCanceled(true);
                maid.setHealth(1.0F);
                maid.removeAllEffects();
                maid.addEffect(new EffectInstance(Effects.REGENERATION, 900, 1));
                maid.addEffect(new EffectInstance(Effects.ABSORPTION, 100, 1));
                maid.addEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 800, 0));
                maid.level.broadcastEntityEvent(maid, (byte) 35);
            }
        }
    }
}
