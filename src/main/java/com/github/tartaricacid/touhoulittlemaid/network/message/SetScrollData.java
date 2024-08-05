//package com.github.tartaricacid.touhoulittlemaid.network.message;
//
//import com.github.tartaricacid.touhoulittlemaid.item.ItemFoxScroll;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public class SetScrollData {
//    private final String dimension;
//    private final BlockPos pos;
//
//    public SetScrollData(String dimension, BlockPos pos) {
//        this.dimension = dimension;
//        this.pos = pos;
//    }
//
//    public static void encode(SetScrollData message, FriendlyByteBuf buf) {
//        buf.writeUtf(message.dimension);
//        buf.writeBlockPos(message.pos);
//    }
//
//    public static SetScrollData decode(FriendlyByteBuf buf) {
//        return new SetScrollData(buf.readUtf(), buf.readBlockPos());
//    }
//
//    public static void handle(SetScrollData message, Supplier<NetworkEvent.Context> contextSupplier) {
//        NetworkEvent.Context context = contextSupplier.get();
//        if (context.getDirection().getReceptionSide().isServer()) {
//            context.enqueueWork(() -> {
//                ServerPlayer sender = context.getSender();
//                if (sender == null) {
//                    return;
//                }
//                ItemStack item = sender.getMainHandItem();
//                if (item.getItem() instanceof ItemFoxScroll) {
//                    ItemFoxScroll.setTrackInfo(item, message.dimension, message.pos);
//                }
//            });
//        }
//        context.setPacketHandled(true);
//    }
//}
