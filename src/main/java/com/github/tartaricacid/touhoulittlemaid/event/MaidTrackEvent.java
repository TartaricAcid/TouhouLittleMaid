package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncYsmMaidDataMessage;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public class MaidTrackEvent {
    @SubscribeEvent
    public static void onTrackingPlayer(PlayerEvent.StartTracking event) {
        Entity target = event.getTarget();
        Player player = event.getEntity();
        if (target instanceof EntityMaid maid && maid.isYsmModel()) {
            SyncYsmMaidDataMessage message = new SyncYsmMaidDataMessage(maid.getId(), maid.rouletteAnim, maid.rouletteAnimPlaying, maid.roamingVars);
            NetworkHandler.sendToClientPlayer(message, player);
        }
    }
}
