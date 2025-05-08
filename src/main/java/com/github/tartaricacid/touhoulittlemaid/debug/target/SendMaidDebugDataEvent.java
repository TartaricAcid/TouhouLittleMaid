package com.github.tartaricacid.touhoulittlemaid.debug.target;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, modid = TouhouLittleMaid.MOD_ID)
public class SendMaidDebugDataEvent {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        if (event.phase == TickEvent.Phase.END && event.side.isServer()) {
            List<EntityMaid> debuggingMaid = DebugMaidManager.getDebuggingMaid((ServerPlayer) event.player);
            for (EntityMaid maid : debuggingMaid) {
                if (maid != null) {
                    renderForMaid(maid, (ServerPlayer) event.player);
                }
            }
        }
    }

    private static void renderForMaid(EntityMaid maid, ServerPlayer player) {
        //Path
        if (!maid.getNavigation().isDone()) {
            Path path = maid.getNavigation().getPath();
            FriendlyByteBuf friendlybytebuf = new FriendlyByteBuf(Unpooled.buffer());
            friendlybytebuf.writeInt(maid.getId());
            friendlybytebuf.writeFloat(0.5f);
            path.writeToStream(friendlybytebuf);
            player.connection.send(new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.DEBUG_PATHFINDING_PACKET, friendlybytebuf));
        }

        DebugMaidManager.getDebugTargets(maid).forEach(target -> {
            FriendlyByteBuf friendlybytebuf = new FriendlyByteBuf(Unpooled.buffer());
            friendlybytebuf.writeBlockPos(target.pos());
            friendlybytebuf.writeInt(target.color());
            friendlybytebuf.writeUtf(target.text());
            friendlybytebuf.writeInt(100);
            player.connection.send(new ClientboundCustomPayloadPacket(ClientboundCustomPayloadPacket.DEBUG_GAME_TEST_ADD_MARKER, friendlybytebuf));
        });
    }
}
