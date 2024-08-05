package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.data.MaidNumAttachment;
import com.github.tartaricacid.touhoulittlemaid.data.PowerAttachment;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record SyncDataPackage(float power, int maidNum) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SyncDataPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("sync_data"));
    public static final StreamCodec<ByteBuf, SyncDataPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT,
            SyncDataPackage::power,
            ByteBufCodecs.VAR_INT,
            SyncDataPackage::maidNum,
            SyncDataPackage::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncDataPackage message, IPayloadContext context) {
        Player player = context.player();
        context.enqueueWork(() -> {
            player.setData(InitDataAttachment.POWER_NUM, new PowerAttachment(message.power));
            player.setData(InitDataAttachment.MAID_NUM, new MaidNumAttachment(message.maidNum));
        });
    }
}
