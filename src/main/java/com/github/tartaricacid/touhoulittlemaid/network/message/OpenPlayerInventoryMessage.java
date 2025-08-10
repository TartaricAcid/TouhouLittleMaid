package com.github.tartaricacid.touhoulittlemaid.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * 与扫帚骑乘有关的消息，从服务端到客户端
 */
public class OpenPlayerInventoryMessage {
    public static final int OPEN_PLAYER_INVENTORY = 0;

    private final int action;

    public OpenPlayerInventoryMessage(int action) {
        this.action = action;
    }

    public static OpenPlayerInventoryMessage decode(FriendlyByteBuf buf) {
        int action = buf.readInt();
        return new OpenPlayerInventoryMessage(action);
    }

    public static void encode(OpenPlayerInventoryMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.action);
    }

    public static void handle(OpenPlayerInventoryMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(OpenPlayerInventoryMessage message) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        if (message.action == OPEN_PLAYER_INVENTORY) {
            // 打开玩家背包
            Minecraft.getInstance().setScreen(new InventoryScreen(player));
        }
    }
}
