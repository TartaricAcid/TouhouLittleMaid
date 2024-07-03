package com.github.tartaricacid.touhoulittlemaid.compat.tacz.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidHurtEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.tacz.guns.entity.EntityKineticBullet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GunHurtMaidEvent {
    @SubscribeEvent
    public void onMaidHurt(MaidHurtEvent event) {
        DamageSource source = event.getSource();
        EntityMaid maid = event.getMaid();
        if (maid.getOwnerUUID() == null) {
            return;
        }
        if (source instanceof IndirectEntityDamageSource damageSource && damageSource.getDirectEntity() instanceof EntityKineticBullet) {
            event.setCanceled(true);
        }
    }
}
