package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.advancements.maid.TriggerType;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.ChairConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.init.InitTrigger;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record ChairModelPackage(int id, ResourceLocation modelId, float mountedHeight, boolean tameableCanRide,
                                boolean noGravity) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ChairModelPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("chair_model"));
    public static final StreamCodec<ByteBuf, ChairModelPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ChairModelPackage::id,
            ResourceLocation.STREAM_CODEC,
            ChairModelPackage::modelId,
            ByteBufCodecs.FLOAT,
            ChairModelPackage::mountedHeight,
            ByteBufCodecs.BOOL,
            ChairModelPackage::tameableCanRide,
            ByteBufCodecs.BOOL,
            ChairModelPackage::noGravity,
            ChairModelPackage::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(ChairModelPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ServerPlayer sender = (ServerPlayer) context.player();
                Entity entity = sender.level.getEntity(message.id);
                boolean canChangeModel = ChairConfig.CHAIR_CHANGE_MODEL.get() || sender.isCreative();

                if (entity instanceof EntityChair) {
                    if (canChangeModel) {
                        EntityChair chair = (EntityChair) entity;
                        chair.setModelId(message.modelId.toString());
                        chair.setMountedHeight(message.mountedHeight);
                        chair.setTameableCanRide(message.tameableCanRide);
                        chair.setNoGravity(message.noGravity);
                        if (!message.tameableCanRide && !chair.getPassengers().isEmpty()) {
                            chair.ejectPassengers();
                        }
                        InitTrigger.MAID_EVENT.get().trigger(sender, TriggerType.CHANGE_CHAIR_MODEL);
                    } else {
                        if (sender.isAlive()) {
                            sender.sendSystemMessage(Component.translatable("message.touhou_little_maid.change_model.disabled"));
                        }
                    }
                }
            });
        }
    }
}
