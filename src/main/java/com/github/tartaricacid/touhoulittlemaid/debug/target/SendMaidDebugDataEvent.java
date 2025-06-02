package com.github.tartaricacid.touhoulittlemaid.debug.target;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.GameTestAddMarkerDebugPayload;
import net.minecraft.network.protocol.common.custom.PathfindingDebugPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.pathfinder.Path;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import javax.annotation.Nullable;
import java.util.List;


@VisibleForDebug
@EventBusSubscriber(bus = EventBusSubscriber.Bus.GAME, modid = TouhouLittleMaid.MOD_ID)
public class SendMaidDebugDataEvent {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Pre event) {
        if (!TouhouLittleMaid.DEBUG) {
            return;
        }
        if (event.getEntity().level.isClientSide || !(event.getEntity() instanceof ServerPlayer serverPlayer)) {
            return;
        }
        // 每 4 tick 发送一次数据
        if (serverPlayer.tickCount % 4 == 0) {
            List<EntityMaid> debuggingMaid = DebugMaidManager.getDebuggingMaid(serverPlayer);
            for (EntityMaid maid : debuggingMaid) {
                renderForMaid(maid, serverPlayer);
            }
        }
    }

    private static void renderForMaid(@Nullable EntityMaid maid, ServerPlayer player) {
        if (maid == null) {
            return;
        }

        if (!maid.getNavigation().isDone()) {
            Path path = maid.getNavigation().getPath();
            if (path != null) {
                player.connection.send(new ClientboundCustomPayloadPacket(new PathfindingDebugPayload(maid.getId(), path, 0.5f)));
            }
        }

        DebugMaidManager.getDebugTargets(maid).forEach(target -> {
            GameTestAddMarkerDebugPayload payload = new GameTestAddMarkerDebugPayload(
                    target.pos(), target.color(), target.text(), target.lifeTime());
            player.connection.send(new ClientboundCustomPayloadPacket(payload));
        });
    }
}
