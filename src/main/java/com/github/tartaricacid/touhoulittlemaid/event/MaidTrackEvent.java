package com.github.tartaricacid.touhoulittlemaid.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SyncBaubleMessage;
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
        if (target instanceof EntityMaid maid) {
            // 如果是 ysm 模型，那么同步 ysm 模型信息
            if (maid.isYsmModel()) {
                SyncYsmMaidDataMessage message = new SyncYsmMaidDataMessage(maid.getId(), maid.rouletteAnim, maid.rouletteAnimPlaying, maid.roamingVars);
                NetworkHandler.sendToClientPlayer(message, player);
            }

            // 如果包含需要同步到客户端的饰品信息，那么同步
            var syncClientBauble = maid.getMaidBauble().getSyncClientBauble(maid);
            if (!syncClientBauble.isEmpty()) {
                SyncBaubleMessage msg = SyncBaubleMessage.fullSync(maid.getId(), syncClientBauble);
                NetworkHandler.sendToClientPlayer(msg, player);
            }
        }
    }
}
