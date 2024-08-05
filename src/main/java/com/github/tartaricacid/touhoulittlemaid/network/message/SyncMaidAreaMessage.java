//package com.github.tartaricacid.touhoulittlemaid.network.message;
//
//import com.github.tartaricacid.touhoulittlemaid.client.event.MaidAreaRenderEvent;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.SchedulePos;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.resources.ResourceLocation;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//import net.minecraftforge.network.NetworkEvent;
//
//import java.util.function.Supplier;
//
//public class SyncMaidAreaMessage {
//    private final int id;
//    private final SchedulePos schedulePos;
//
//    public SyncMaidAreaMessage(int id, SchedulePos schedulePos) {
//        this.id = id;
//        this.schedulePos = schedulePos;
//    }
//
//    public static void encode(SyncMaidAreaMessage message, FriendlyByteBuf buf) {
//        SchedulePos pos = message.schedulePos;
//        buf.writeVarInt(message.id);
//        buf.writeBlockPos(pos.getWorkPos());
//        buf.writeBlockPos(pos.getIdlePos());
//        buf.writeBlockPos(pos.getSleepPos());
//        buf.writeResourceLocation(pos.getDimension());
//    }
//
//    public static SyncMaidAreaMessage decode(FriendlyByteBuf buf) {
//        int maidId = buf.readVarInt();
//        BlockPos workPos = buf.readBlockPos();
//        BlockPos idlePos = buf.readBlockPos();
//        BlockPos sleepPos = buf.readBlockPos();
//        ResourceLocation dimension = buf.readResourceLocation();
//        SchedulePos pos = new SchedulePos(workPos, idlePos, sleepPos, dimension);
//        return new SyncMaidAreaMessage(maidId, pos);
//    }
//
//    public static void handle(SyncMaidAreaMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
//        NetworkEvent.Context context = contextSupplier.get();
//        if (context.getDirection().getReceptionSide().isClient()) {
//            context.enqueueWork(() -> writePos(message));
//        }
//        context.setPacketHandled(true);
//    }
//
//    @OnlyIn(Dist.CLIENT)
//    private static void writePos(SyncMaidAreaMessage message) {
//        MaidAreaRenderEvent.addSchedulePos(message.id, message.schedulePos);
//    }
//}
