package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.ChairConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ChairModelMessage {
    private final int id;
    private final ResourceLocation modelId;
    private final float mountedHeight;
    private final boolean tameableCanRide;
    private final boolean noGravity;

    public ChairModelMessage(int id, ResourceLocation modelId, float mountedHeight, boolean tameableCanRide, boolean noGravity) {
        this.id = id;
        this.modelId = modelId;
        this.mountedHeight = mountedHeight;
        this.tameableCanRide = tameableCanRide;
        this.noGravity = noGravity;
    }

    public static void encode(ChairModelMessage message, PacketBuffer buf) {
        buf.writeInt(message.id);
        buf.writeResourceLocation(message.modelId);
        buf.writeFloat(message.mountedHeight);
        buf.writeBoolean(message.tameableCanRide);
        buf.writeBoolean(message.noGravity);
    }

    public static ChairModelMessage decode(PacketBuffer buf) {
        return new ChairModelMessage(buf.readInt(), buf.readResourceLocation(), buf.readFloat(), buf.readBoolean(), buf.readBoolean());
    }

    public static void handle(ChairModelMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
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
                    } else {
                        if (sender.isAlive()) {
                            sender.sendMessage(new TranslationTextComponent("message.touhou_little_maid.change_model.disabled"),
                                    ChatType.GAME_INFO, Util.NIL_UUID);
                        }
                    }
                }
            });
        }
    }
}
