package com.github.tartaricacid.touhoulittlemaid.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

/**
 * 与扫帚骑乘有关的消息，从服务端到客户端
 */
public record OpenPlayerInventoryPackage(int action) implements CustomPacketPayload {
    public static final int OPEN_PLAYER_INVENTORY = 0;
    public static final CustomPacketPayload.Type<OpenPlayerInventoryPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("open_player_inventory"));
    public static final StreamCodec<RegistryFriendlyByteBuf, OpenPlayerInventoryPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, OpenPlayerInventoryPackage::action,
            OpenPlayerInventoryPackage::new
    );

    public static void handle(OpenPlayerInventoryPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> onHandle(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(OpenPlayerInventoryPackage message) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (message.action == OPEN_PLAYER_INVENTORY) {
            // 打开玩家背包
            Minecraft.getInstance().setScreen(new InventoryScreen(player));
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
