//package com.github.tartaricacid.touhoulittlemaid.network.message;
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.network.FriendlyByteBuf;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//
//public class SyncCapabilityMessage {
//    private final float power;
//    private final int maidNum;
//
//    public SyncCapabilityMessage(float power, int maidNum) {
//        this.power = power;
//        this.maidNum = maidNum;
//    }
//
//    public static void encode(SyncCapabilityMessage message, FriendlyByteBuf buf) {
//        buf.writeFloat(message.power);
//        buf.writeVarInt(message.maidNum);
//    }
//
//    public static SyncCapabilityMessage decode(FriendlyByteBuf buf) {
//        return new SyncCapabilityMessage(buf.readFloat(), buf.readVarInt());
//    }
//
//    public static void handle(SyncCapabilityMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
//        NetworkEvent.Context context = contextSupplier.get();
//        if (context.getDirection().getReceptionSide().isClient()) {
//            context.enqueueWork(() -> handleCapability(message));
//        }
//        context.setPacketHandled(true);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    private static void handleCapability(SyncCapabilityMessage message) {
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.level == null || mc.player == null) {
//            return;
//        }
//        mc.player.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(cap -> cap.set(message.power));
//        mc.player.getCapability(MaidNumCapabilityProvider.MAID_NUM_CAP).ifPresent(cap -> cap.set(message.maidNum));
//    }
//}
