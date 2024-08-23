package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.item.ItemFoxScroll;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record SetScrollPackage(String dimension, BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetScrollPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("set_scroll"));
    public static final StreamCodec<ByteBuf, SetScrollPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8,
            SetScrollPackage::dimension,
            BlockPos.STREAM_CODEC,
            SetScrollPackage::pos,
            SetScrollPackage::new
    );

    public static void handle(SetScrollPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                ItemStack item = sender.getMainHandItem();
                if (item.getItem() instanceof ItemFoxScroll) {
                    ItemFoxScroll.setTrackInfo(item, message.dimension, message.pos);
                }
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
