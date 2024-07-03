package com.github.tartaricacid.touhoulittlemaid.compat.tacz.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidHurtEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.tacz.guns.init.ModDamageTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class GunHurtMaidEvent {
    @SubscribeEvent
    public void onMaidHurt(MaidHurtEvent event) {
        DamageSource source = event.getSource();
        EntityMaid maid = event.getMaid();
        if (maid.getOwnerUUID() == null) {
            return;
        }
        if (source.is(ModDamageTypes.BULLET) || source.is(ModDamageTypes.BULLET_IGNORE_ARMOR)) {
            event.setCanceled(true);
        }
    }
}
