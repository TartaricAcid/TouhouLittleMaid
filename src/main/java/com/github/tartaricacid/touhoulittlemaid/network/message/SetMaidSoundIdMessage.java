//package com.github.tartaricacid.touhoulittlemaid.network.message;
//
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.Entity;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public class SetMaidSoundIdMessage {
//    private final int entityId;
//    private final String soundId;
//
//    public SetMaidSoundIdMessage(int entityId, String soundId) {
//        this.entityId = entityId;
//        this.soundId = soundId;
//    }
//
//    public static void encode(SetMaidSoundIdMessage message, FriendlyByteBuf buf) {
//        buf.writeInt(message.entityId);
//        buf.writeUtf(message.soundId);
//    }
//
//    public static SetMaidSoundIdMessage decode(FriendlyByteBuf buf) {
//        return new SetMaidSoundIdMessage(buf.readInt(), buf.readUtf());
//    }
//
//    public static void handle(SetMaidSoundIdMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
//        NetworkEvent.Context context = contextSupplier.get();
//        if (context.getDirection().getReceptionSide().isServer()) {
//            context.enqueueWork(() -> {
//                ServerPlayer sender = context.getSender();
//                if (sender == null) {
//                    return;
//                }
//                Entity entity = sender.level.getEntity(message.entityId);
//                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
//                    maid.setSoundPackId(message.soundId);
//                }
//            });
//        }
//        context.setPacketHandled(true);
//    }
//}
