package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLocationUtil.getResourceLocation;

public record SendEffectPackage(int id, Collection<MobEffectInstance> effects) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SendEffectPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("send_effect"));

    public static final StreamCodec<RegistryFriendlyByteBuf, Collection<MobEffectInstance>> COLLECTION_STREAM_CODEC =
            ByteBufCodecs.collection(
                    ArrayList::new,
                    MobEffectInstance.STREAM_CODEC,
                    256);

    public static final StreamCodec<RegistryFriendlyByteBuf, SendEffectPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            SendEffectPackage::id,
            COLLECTION_STREAM_CODEC,
            SendEffectPackage::effects,
            SendEffectPackage::new
    );

    public static void handle(SendEffectPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> handle(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle(SendEffectPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity entity = mc.level.getEntity(message.id);
        if (entity instanceof EntityMaid maid && maid.isAlive()) {
            maid.setEffects(Lists.newArrayList(message.effects));
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}