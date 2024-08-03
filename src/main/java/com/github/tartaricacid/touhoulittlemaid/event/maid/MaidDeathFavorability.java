package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDeathEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.FavorabilityManager;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber
public class MaidDeathFavorability {
    @SubscribeEvent
    public static void onDeath(MaidDeathEvent event) {
        FavorabilityManager manager = event.getMaid().getFavorabilityManager();
        manager.apply(Type.DEATH);
    }
}
