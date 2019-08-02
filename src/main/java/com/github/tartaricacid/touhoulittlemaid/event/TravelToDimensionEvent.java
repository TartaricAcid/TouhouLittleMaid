package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author TartaricAcid
 * @date 2019/8/2 12:53
 **/
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class TravelToDimensionEvent {
    @SubscribeEvent
    public static void onTravelToDimension(EntityTravelToDimensionEvent event) {
        Entity entity = event.getEntity();
        // 骑乘扫把，或者扫把骑乘，全都不允许传送
        if (entity.getRidingEntity() instanceof EntityMarisaBroom || entity.getControllingPassenger() instanceof EntityMarisaBroom) {
            event.setCanceled(true);
        }
    }
}
