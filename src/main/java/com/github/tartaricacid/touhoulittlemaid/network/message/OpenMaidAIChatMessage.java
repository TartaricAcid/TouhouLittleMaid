package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public record OpenMaidAIChatMessage(int entityId) {
    public OpenMaidAIChatMessage(EntityMaid maid) {
        this(maid.getId());
    }

    public static void encode(OpenMaidAIChatMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityId);
    }

    public static OpenMaidAIChatMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readVarInt();
        return new OpenMaidAIChatMessage(entityId);
    }

    public static void handle(OpenMaidAIChatMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> handle(message, contextSupplier.get().getSender()));
        }
        context.setPacketHandled(true);
    }

    private static void handle(OpenMaidAIChatMessage message, @Nullable ServerPlayer player) {
        if (player == null) {
            return;
        }
        Entity entity = player.level.getEntity(message.entityId);
        if (entity instanceof EntityMaid maid && stillValid(player, maid)) {
            // 发送同步信息
            NetworkHandler.sendToClientPlayer(new SyncMaidAIDataMessage(maid), player);
        }
    }

    // TODO：服务端鉴权
    private static boolean stillValid(Player playerIn, EntityMaid maid) {
        return maid.isOwnedBy(playerIn) && !maid.isSleeping() && maid.isAlive() && maid.distanceTo(playerIn) < 5.0F;
    }
}
