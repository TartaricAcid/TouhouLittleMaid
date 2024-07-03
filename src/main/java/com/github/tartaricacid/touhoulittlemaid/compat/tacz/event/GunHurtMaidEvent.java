package com.github.tartaricacid.touhoulittlemaid.compat.tacz.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidHurtEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.tacz.guns.init.ModDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GunHurtMaidEvent {
    /**
     * 不伤害自己
     */
    @SubscribeEvent
    public void onMaidHurt(MaidHurtEvent event) {
        DamageSource source = event.getSource();
        EntityMaid maid = event.getMaid();
        if (maid.getOwnerUUID() == null) {
            return;
        }
        if (isBulletDamage(source)) {
            event.setCanceled(true);
        }
    }

    /**
     * 不伤害他人
     */
    @SubscribeEvent
    public void onPlayerHurt(LivingAttackEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();
        if (entity instanceof Player player && isBulletDamage(source)) {
            Entity causingEntity = source.getEntity();
            if (causingEntity instanceof EntityMaid maid && maid.isOwnedBy(player)) {
                event.setCanceled(true);
            }
        }
    }

    private boolean isBulletDamage(DamageSource source) {
        return source.is(ModDamageTypes.BULLET) || source.is(ModDamageTypes.BULLET_IGNORE_ARMOR);
    }
}
