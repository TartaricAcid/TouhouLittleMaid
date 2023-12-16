package com.github.tartaricacid.touhoulittlemaid.event.maid;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDeathEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.FavorabilityManager;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MaidDeathFavorability {
    @SubscribeEvent
    public static void onDeath(MaidDeathEvent event) {
        FavorabilityManager manager = event.getMaid().getFavorabilityManager();
        manager.apply(Type.DEATH);
    }
}
