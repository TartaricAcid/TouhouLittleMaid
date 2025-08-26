package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record DismountPackage(int action) implements CustomPacketPayload {
    public static final int DISMOUNT_BROOM = 1;
    public static final CustomPacketPayload.Type<DismountPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("dismount"));
    public static final StreamCodec<RegistryFriendlyByteBuf, DismountPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, DismountPackage::action,
            DismountPackage::new
    );

    public static void handle(DismountPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            Player sender = context.player();
            if (!(sender instanceof ServerPlayer serverPlayer)) {
                return;
            }
            context.enqueueWork(() -> onHandle(message, serverPlayer));
        }
    }

    private static void onHandle(DismountPackage message, ServerPlayer sender) {
        // 处理卸载扫帚的逻辑
        if (message.action() == DISMOUNT_BROOM && sender.getVehicle() instanceof EntityBroom) {
            sender.stopRiding();
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
