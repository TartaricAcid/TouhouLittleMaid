package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.TeleportHelper;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class TravelToDimensionEvent {
    private static final int MAX_RETRY = 16;

    @SubscribeEvent
    public static void onTravelToDimension(EntityTravelToDimensionEvent event) {
        if (event.getEntity() instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) event.getEntity();
            for (int i = 0; i < MAX_RETRY; ++i) {
                if (TeleportHelper.teleport(maid)) {
                    maid.addEffect(new EffectInstance(Effects.GLOWING, 200, 1, true, false));
                    event.setCanceled(true);
                    return;
                }
            }
        }
    }
}
