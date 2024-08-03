package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class SendEffectMessage {
    private final int id;
    private List<EffectData> effects = Lists.newArrayList();

    public SendEffectMessage(int id, Collection<MobEffectInstance> effects) {
        this.id = id;
        for (MobEffectInstance effect : effects) {
            this.effects.add(new EffectData(effect));
        }
    }

    public SendEffectMessage(int id, List<EffectData> effects) {
        this.id = id;
        this.effects = effects;
    }

    public static void encode(SendEffectMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.id);
        buf.writeInt(message.effects.size());
        for (EffectData effect : message.effects) {
            effect.toBytes(buf);
        }
    }

    public static SendEffectMessage decode(FriendlyByteBuf buf) {
        int id = buf.readInt();
        int size = buf.readInt();
        List<EffectData> effects = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            effects.add(EffectData.fromBytes(buf));
        }
        return new SendEffectMessage(id, effects);
    }

    public static void handle(SendEffectMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> handle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle(SendEffectMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity entity = mc.level.getEntity(message.id);
        if (entity instanceof EntityMaid maid && maid.isAlive()) {
            maid.setEffects(message.effects);
        }
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
            this.category = effect.getEffect().getCategory().ordinal();
        }

        public EffectData(String descriptionId, int amplifier, int duration, int category) {
            this.descriptionId = descriptionId;
            this.amplifier = amplifier;
            this.duration = duration;
            this.category = category;
        }

        public static EffectData fromBytes(FriendlyByteBuf buf) {
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
