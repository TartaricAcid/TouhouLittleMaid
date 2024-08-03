package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidSoundInstance;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;

public class PlayMaidSoundMessage {
    private final ResourceLocation soundEvent;
    private final String id;
    private final int entityId;

    public PlayMaidSoundMessage(ResourceLocation soundEvent, String id, int entityId) {
        this.soundEvent = soundEvent;
        this.id = id;
        this.entityId = entityId;
    }

    public static void encode(PlayMaidSoundMessage message, FriendlyByteBuf buf) {
        buf.writeResourceLocation(message.soundEvent);
        buf.writeUtf(message.id);
        buf.writeVarInt(message.entityId);
    }

    public static PlayMaidSoundMessage decode(FriendlyByteBuf buf) {
        return new PlayMaidSoundMessage(buf.readResourceLocation(), buf.readUtf(), buf.readVarInt());
    }

    public static void handle(PlayMaidSoundMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> playSound(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void playSound(PlayMaidSoundMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            Entity entity = mc.level.getEntity(message.entityId);
            if (entity instanceof EntityMaid maid) {
                SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(message.soundEvent);
                if (event != null) {
                    mc.getSoundManager().play(new MaidSoundInstance(event, message.id, maid));
                }
            }
        }
    }
}
