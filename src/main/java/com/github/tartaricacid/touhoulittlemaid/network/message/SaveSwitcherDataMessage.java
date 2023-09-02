package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityModelSwitcher;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class SaveSwitcherDataMessage {
    private final BlockPos pos;
    private final List<TileEntityModelSwitcher.ModeInfo> modeInfos;

    public SaveSwitcherDataMessage(BlockPos pos, List<TileEntityModelSwitcher.ModeInfo> modeInfos) {
        this.pos = pos;
        this.modeInfos = modeInfos;
    }

    public static void encode(SaveSwitcherDataMessage message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
        buf.writeVarInt(message.modeInfos.size());
        for (TileEntityModelSwitcher.ModeInfo info : message.modeInfos) {
            info.toBuf(buf);
        }
    }

    public static SaveSwitcherDataMessage decode(PacketBuffer buf) {
        BlockPos pos = buf.readBlockPos();
        int size = buf.readVarInt();
        List<TileEntityModelSwitcher.ModeInfo> modeInfos = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            modeInfos.add(TileEntityModelSwitcher.ModeInfo.fromBuf(buf));
        }
        return new SaveSwitcherDataMessage(pos, modeInfos);
    }

    public static void handle(SaveSwitcherDataMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> {
                ServerPlayerEntity sender = context.getSender();
                if (sender == null) {
                    return;
                }
                World world = sender.level;
                if (world.isLoaded(message.pos)) {
                    TileEntity te = world.getBlockEntity(message.pos);
                    if (te instanceof TileEntityModelSwitcher) {
                        ((TileEntityModelSwitcher) te).setInfoList(message.modeInfos);
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }
}