package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.FoxScrollScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record FoxScrollPackage(Map<String, List<FoxScrollData>> data) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<FoxScrollPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("fox_scroll"));

    public static final StreamCodec<RegistryFriendlyByteBuf, List<FoxScrollData>> LIST_STREAM_CODEC = ByteBufCodecs.collection(
            ArrayList::new,
            FoxScrollData.FOX_SCROLL_DATA_STREAM_CODEC,
            20
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, Map<String, List<FoxScrollData>>> BYTE_BUF_MAP_STREAM_CODEC = ByteBufCodecs.map(
            HashMap::new,
            ByteBufCodecs.STRING_UTF8,
            LIST_STREAM_CODEC,
            20
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, FoxScrollPackage> STREAM_CODEC = StreamCodec.composite(
            BYTE_BUF_MAP_STREAM_CODEC,
            FoxScrollPackage::data,
            FoxScrollPackage::new
    );

    public static void handle(FoxScrollPackage message, IPayloadContext context) {
        context.enqueueWork(() -> onHandle(message));
    }

    private static void onHandle(FoxScrollPackage message) {
        Minecraft.getInstance().setScreen(new FoxScrollScreen(message.data));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class FoxScrollData {
        //TODO : component的CODEC
        public static final StreamCodec<RegistryFriendlyByteBuf, FoxScrollData> FOX_SCROLL_DATA_STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, FoxScrollData::getPos,
                );
        private final BlockPos pos;
        private final Component name;
        private final long timestamp;

        public FoxScrollData(BlockPos pos, Component name, long timestamp) {
            this.pos = pos;
            this.name = name;
            this.timestamp = timestamp;
        }

        public static void encode(FoxScrollData data, FriendlyByteBuf buf) {
            buf.writeBlockPos(data.pos);
            buf.writeComponent(data.name);
            buf.writeLong(data.timestamp);
        }

        public static FoxScrollData decode(FriendlyByteBuf buf) {
            return new FoxScrollData(buf.readBlockPos(), buf.readComponent(), buf.readLong());
        }

        public BlockPos getPos() {
            return pos;
        }

        public Component getName() {
            return name;
        }

        public long getTimestamp() {
            return timestamp;
        }
    }
}
