package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.TabIndex;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.Nullable;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record OpenMaidGuiPackage(int entityId, int tabId) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenMaidGuiPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("open_maid_gui"));
    public static final StreamCodec<ByteBuf, OpenMaidGuiPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, OpenMaidGuiPackage::entityId,
            ByteBufCodecs.VAR_INT, OpenMaidGuiPackage::tabId,
            OpenMaidGuiPackage::new
    );

    public OpenMaidGuiPackage(int entityId) {
        this(entityId, TabIndex.MAIN);
    }

    public static void handle(OpenMaidGuiPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> handle(message, (ServerPlayer) context.player()));
        }
    }

    private static void handle(OpenMaidGuiPackage message, @Nullable ServerPlayer player) {
        if (player == null) {
            return;
        }
        Entity entity = player.level.getEntity(message.entityId);
        if (entity instanceof EntityMaid maid && stillValid(player, maid)) {
            maid.openMaidGui(player, message.tabId);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static boolean stillValid(Player playerIn, EntityMaid maid) {
        return maid.isOwnedBy(playerIn) && !maid.isSleeping() && maid.isAlive() && maid.distanceTo(playerIn) < 5.0F;
    }
}
