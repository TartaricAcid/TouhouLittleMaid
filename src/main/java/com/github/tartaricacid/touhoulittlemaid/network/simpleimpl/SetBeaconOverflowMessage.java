package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class SetBeaconOverflowMessage implements IMessage {
    private BlockPos pos;
    private boolean overflowDelete;

    public SetBeaconOverflowMessage() {
    }

    public SetBeaconOverflowMessage(BlockPos pos, boolean overflowDelete) {
        this.pos = pos;
        this.overflowDelete = overflowDelete;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        overflowDelete = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeBoolean(overflowDelete);
    }

    public static class Handler implements IMessageHandler<SetBeaconOverflowMessage, IMessage> {
        @Override
        public IMessage onMessage(SetBeaconOverflowMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    World world = ctx.getServerHandler().player.world;
                    if (world.isBlockLoaded(message.pos)) {
                        TileEntity te = world.getTileEntity(message.pos);
                        if (te instanceof TileEntityMaidBeacon) {
                            ((TileEntityMaidBeacon) te).setOverflowDelete(message.overflowDelete);
                        }
                    }
                });
            }
            return null;
        }
    }
}
