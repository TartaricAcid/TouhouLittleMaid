package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record SendEffectPackage(int id, Collection<MobEffectInstance> effects) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SendEffectPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("send_effect"));

    public static final StreamCodec<RegistryFriendlyByteBuf, Collection<MobEffectInstance>> COLLECTION_STREAM_CODEC =
            ByteBufCodecs.collection(
                    ArrayList::new,
                    MobEffectInstance.STREAM_CODEC,
                    20);

    public static final StreamCodec<RegistryFriendlyByteBuf, SendEffectPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SendEffectPackage::id,
            COLLECTION_STREAM_CODEC,
            SendEffectPackage::effects,
            SendEffectPackage::new
    );

    public static void handle(SendEffectPackage message, IPayloadContext context) {
        context.enqueueWork(() -> handle(message));
    }

    private static void handle(SendEffectPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity entity = mc.level.getEntity(message.id);
        if (entity instanceof EntityMaid maid && maid.isAlive()) {
            List<EffectData> effectsData = Lists.newArrayList();
            for (MobEffectInstance effect : message.effects) {
                effectsData.add(new EffectData(effect));
            }
            maid.setEffects(effectsData);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class EffectData {
        public String descriptionId;
        public int amplifier;
        public int duration;
        public int category;

        public EffectData(MobEffectInstance effect) {
            this.descriptionId = effect.getDescriptionId();
            this.amplifier = effect.getAmplifier();
            this.duration = effect.getDuration();
            this.category = effect.getEffect().value().getCategory().ordinal();
        }

        public EffectData(String descriptionId, int amplifier, int duration, int category) {
            this.descriptionId = descriptionId;
            this.amplifier = amplifier;
            this.duration = duration;
            this.category = category;
        }

        public static SendEffectPackage.EffectData fromBytes(FriendlyByteBuf buf) {
            return new EffectData(buf.readUtf(), buf.readInt(), buf.readInt(), buf.readInt());
        }

        public void toBytes(FriendlyByteBuf buf) {
            buf.writeUtf(this.descriptionId);
            buf.writeInt(this.amplifier);
            buf.writeInt(this.duration);
            buf.writeInt(this.category);
        }
    }
}
