//package com.github.tartaricacid.touhoulittlemaid.network.message;
//
//import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public class SetBeaconPotionMessage {
//    private final BlockPos pos;
//    private final int potionIndex;
//
//    public SetBeaconPotionMessage(BlockPos pos, int potionIndex) {
//        this.pos = pos;
//        this.potionIndex = potionIndex;
//    }
//
//    public static void encode(SetBeaconPotionMessage message, FriendlyByteBuf buf) {
//        buf.writeBlockPos(message.pos);
//        buf.writeInt(message.potionIndex);
//    }
//
//    public static SetBeaconPotionMessage decode(FriendlyByteBuf buf) {
//        return new SetBeaconPotionMessage(buf.readBlockPos(), buf.readInt());
//    }
//
//    public static void handle(SetBeaconPotionMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
//        NetworkEvent.Context context = contextSupplier.get();
//        if (context.getDirection().getReceptionSide().isServer()) {
//            context.enqueueWork(() -> {
//                ServerPlayer sender = context.getSender();
//                if (sender == null) {
//                    return;
//                }
//                Level world = sender.level();
//                if (world.isLoaded(message.pos)) {
//                    BlockEntity te = world.getBlockEntity(message.pos);
//                    if (te instanceof TileEntityMaidBeacon) {
//                        ((TileEntityMaidBeacon) te).setPotionIndex(message.potionIndex);
//                    }
//                }
//            });
//        }
//        context.setPacketHandled(true);
//    }
//}
