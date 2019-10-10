package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public class SetBeaconPotionMessage implements IMessage {
    private UUID playerUuid;
    private BlockPos pos;
    private int potionIndex;

    public SetBeaconPotionMessage() {
    }

    public SetBeaconPotionMessage(UUID playerUuid, BlockPos pos, int potionIndex) {
        this.playerUuid = playerUuid;
        this.pos = pos;
        this.potionIndex = potionIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerUuid = new UUID(buf.readLong(), buf.readLong());
        pos = BlockPos.fromLong(buf.readLong());
        potionIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(playerUuid.getMostSignificantBits());
        buf.writeLong(playerUuid.getLeastSignificantBits());
        buf.writeLong(pos.toLong());
        buf.writeInt(potionIndex);
    }

    public static class Handler implements IMessageHandler<SetBeaconPotionMessage, IMessage> {
        @Override
        public IMessage onMessage(SetBeaconPotionMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.playerUuid);
                    if (entity instanceof EntityPlayer) {
                        EntityPlayer player = (EntityPlayer) entity;
                        World world = player.world;
                        TileEntity te = world.getTileEntity(message.pos);
                        if (te instanceof TileEntityMaidBeacon) {
                            ((TileEntityMaidBeacon) te).setPotionIndex(message.potionIndex);
                        }
                    }
                });
            }
            return null;
        }
    }
}
