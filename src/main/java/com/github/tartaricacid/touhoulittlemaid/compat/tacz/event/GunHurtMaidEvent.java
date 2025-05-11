package com.github.tartaricacid.touhoulittlemaid.compat.tacz.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidHurtEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.tacz.guns.api.event.common.EntityHurtByGunEvent;
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
     * 避免通过事件引入伤害的附属模组打死玩家
     */
    @SubscribeEvent
    public void onGunHurt(EntityHurtByGunEvent.Pre event) {
        Entity hurtEntity = event.getHurtEntity();
        LivingEntity attacker = event.getAttacker();

        // 不伤害自己 x2
        if (attacker instanceof EntityMaid maid) {
            // 主人和同 Team 玩家免伤
            if (hurtEntity instanceof Player player && maid.isAlliedTo(player)) {
                event.setCanceled(true);
            }
        }

        // 不伤害他人 x2
        if (attacker instanceof Player player) {
            // 主人和同 Team 玩家免伤
            if (hurtEntity instanceof EntityMaid maid && maid.isAlliedTo(player)) {
                event.setCanceled(true);
            }
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
            // 主人和同 Team 玩家免伤
            if (causingEntity instanceof EntityMaid maid && maid.isAlliedTo(player)) {
                event.setCanceled(true);
            }
        }
    }

    private boolean isBulletDamage(DamageSource source) {
        return source.is(ModDamageTypes.BULLETS_TAG);
    }
}
