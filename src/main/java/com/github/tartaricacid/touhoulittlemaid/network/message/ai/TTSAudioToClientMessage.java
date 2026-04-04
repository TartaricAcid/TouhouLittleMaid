package com.github.tartaricacid.touhoulittlemaid.network.message.ai;

import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidAISoundInstance;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TTSAudioToClientMessage {
    private final int maidId;
    private final byte[] data;

    public TTSAudioToClientMessage(int maidId, byte[] data) {
        this.maidId = maidId;
        this.data = data;
    }

    public static void encode(TTSAudioToClientMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.maidId);
        buf.writeByteArray(message.data);
    }

    public static TTSAudioToClientMessage decode(FriendlyByteBuf buf) {
        return new TTSAudioToClientMessage(buf.readVarInt(), buf.readByteArray());
    }

    public static void handle(TTSAudioToClientMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(TTSAudioToClientMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity entity = mc.level.getEntity(message.maidId);
        if (!(entity instanceof EntityMaid maid)) {
            return;
        }
        if (maid.isAlive()) {
            MaidAISoundInstance instance = new MaidAISoundInstance(maid, message.data);
            Minecraft.getInstance().getSoundManager().play(instance);
        }
    }
}
