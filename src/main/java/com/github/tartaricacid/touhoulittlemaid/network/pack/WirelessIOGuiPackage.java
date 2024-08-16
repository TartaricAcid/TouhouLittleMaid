package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemWirelessIO;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record WirelessIOGuiPackage(boolean isMaidToChest, boolean isBlacklist) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<WirelessIOGuiPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("wireless_io_gui"));
    public static final StreamCodec<ByteBuf, WirelessIOGuiPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            WirelessIOGuiPackage::isMaidToChest,
            ByteBufCodecs.BOOL,
            WirelessIOGuiPackage::isBlacklist,
            WirelessIOGuiPackage::new
    );

    public static void handle(WirelessIOGuiPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                ItemStack handItem = sender.getMainHandItem();
                if (handItem.getItem() == InitItems.WIRELESS_IO.get()) {
                    ItemWirelessIO.setMode(handItem, message.isMaidToChest);
                    ItemWirelessIO.setFilterMode(handItem, message.isBlacklist);
                }
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
