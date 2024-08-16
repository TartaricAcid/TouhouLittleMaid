package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.FoxScrollScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
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
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> onHandle(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(FoxScrollPackage message) {
        Minecraft.getInstance().setScreen(new FoxScrollScreen(message.data));
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public record FoxScrollData(BlockPos pos, Component name, long timestamp) {
        public static final StreamCodec<RegistryFriendlyByteBuf, FoxScrollData> FOX_SCROLL_DATA_STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, FoxScrollData::pos,
                ComponentSerialization.STREAM_CODEC, FoxScrollData::name,
                ByteBufCodecs.VAR_LONG, FoxScrollData::timestamp,
                FoxScrollData::new);
    }
}
