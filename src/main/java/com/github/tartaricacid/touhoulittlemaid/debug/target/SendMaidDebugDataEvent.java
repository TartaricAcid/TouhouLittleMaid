package com.github.tartaricacid.touhoulittlemaid.debug.target;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.VisibleForDebug;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;


@VisibleForDebug
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = TouhouLittleMaid.MOD_ID)
public class SendMaidDebugDataEvent {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!TouhouLittleMaid.DEBUG) {
            return;
        }
        if (event.phase != TickEvent.Phase.END || !event.side.isServer() || !(event.player instanceof ServerPlayer serverPlayer)) {
            return;
        }
        // 每 4 tick 发送一次数据
        if (event.player.tickCount % 4 == 0) {
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
                FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
                byteBuf.writeInt(maid.getId());
                byteBuf.writeFloat(0.5f);
                path.writeToStream(byteBuf);
                player.connection.send(new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.DEBUG_PATHFINDING_PACKET, byteBuf));
            }
        }

        DebugMaidManager.getDebugTargets(maid).forEach(target -> {
            FriendlyByteBuf byteBuf = new FriendlyByteBuf(Unpooled.buffer());
            byteBuf.writeBlockPos(target.pos());
            byteBuf.writeInt(target.color());
            byteBuf.writeUtf(target.text());
            byteBuf.writeInt(target.lifeTime());
            player.connection.send(new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.DEBUG_GAME_TEST_ADD_MARKER, byteBuf));
        });
    }
}
