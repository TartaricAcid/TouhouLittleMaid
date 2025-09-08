package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDamageEvent;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidHurtEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 监听原版的 LivingEntity 相关事件，然后将女仆的单独拎出来触发成专门的事件
 * <p>
 * 相当于特化版本的 LivingEntityEvent，方便其他开发者使用
 */
@Mod.EventBusSubscriber
public class MaidLivingEntityEvent {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityMaid maid) {
            MaidHurtEvent maidHurtEvent = new MaidHurtEvent(maid, event.getSource(), event.getAmount());
            float damageAmount = MinecraftForge.EVENT_BUS.post(maidHurtEvent) ? 0 : maidHurtEvent.getAmount();
            event.setAmount(damageAmount);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof EntityMaid maid) {
            MaidDamageEvent maidDamageEvent = new MaidDamageEvent(maid, event.getSource(), event.getAmount());
            float damageAfterAbsorption = MinecraftForge.EVENT_BUS.post(maidDamageEvent) ? 0 : maidDamageEvent.getAmount();
            event.setAmount(damageAfterAbsorption);
        }
    }
}
