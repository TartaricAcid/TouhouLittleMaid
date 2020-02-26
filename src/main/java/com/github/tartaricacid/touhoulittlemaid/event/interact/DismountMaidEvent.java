package com.github.tartaricacid.touhoulittlemaid.event.interact;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.InteractMaidEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class DismountMaidEvent {
    @SubscribeEvent
    public static void onInteract(InteractMaidEvent event) {
        EntityPlayer player = event.getPlayer();
        AbstractEntityMaid maid = event.getMaid();

        if (player.isSneaking()) {
            // 满足其一，返回 true
            if (maid.getRidingEntity() != null || maid.getControllingPassenger() != null) {
                // 取消骑乘状态
                if (maid.getRidingEntity() != null) {
                    maid.dismountRidingEntity();
                }
                // 取消被骑乘状态
                if (maid.getControllingPassenger() != null) {
                    maid.getControllingPassenger().dismountRidingEntity();
                }
                event.setCanceled(true);
            }
        }
    }
}
