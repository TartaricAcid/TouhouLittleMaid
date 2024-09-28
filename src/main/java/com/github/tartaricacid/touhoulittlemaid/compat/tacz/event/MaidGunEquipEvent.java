package com.github.tartaricacid.touhoulittlemaid.compat.tacz.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidEquipEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.tacz.guns.api.entity.IGunOperator;
import com.tacz.guns.api.item.IGun;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class MaidGunEquipEvent {
    @SubscribeEvent
    public void onMaidEquip(MaidEquipEvent event) {
        EntityMaid maid = event.getMaid();
        if (IGun.mainhandHoldGun(maid)) {
            IGunOperator operator = IGunOperator.fromLivingEntity(maid);
            operator.draw(maid::getMainHandItem);
        }
    }
}
