//package com.github.tartaricacid.touhoulittlemaid.network.message;
//
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.entity.Entity;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public class RequestEffectMessage {
//    private final int id;
//
//    public RequestEffectMessage(int id) {
//        this.id = id;
//    }
//
//    public static void encode(RequestEffectMessage message, FriendlyByteBuf buf) {
//        buf.writeInt(message.id);
//    }
//
//    public static RequestEffectMessage decode(FriendlyByteBuf buf) {
//        return new RequestEffectMessage(buf.readInt());
//    }
//
//    public static void handle(RequestEffectMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
//        NetworkEvent.Context context = contextSupplier.get();
//        if (context.getDirection().getReceptionSide().isServer()) {
//            context.enqueueWork(() -> {
//                ServerPlayer sender = context.getSender();
//                if (sender == null) {
//                    return;
//                }
//                Entity entity = sender.level.getEntity(message.id);
//                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
//                    SendEffectMessage sendEffectMessage = new SendEffectMessage(message.id, maid.getActiveEffects());
//                    NetworkHandler.sendToClientPlayer(sendEffectMessage, sender);
//                }
//            });
//        }
//        context.setPacketHandled(true);
//    }
//}
