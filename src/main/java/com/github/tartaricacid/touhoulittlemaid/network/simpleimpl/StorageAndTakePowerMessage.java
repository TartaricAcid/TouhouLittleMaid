package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerSerializer;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class StorageAndTakePowerMessage implements IMessage {
    private BlockPos pos;
    private float powerNum;
    private boolean isStorage;

    public StorageAndTakePowerMessage() {
    }

    public StorageAndTakePowerMessage(BlockPos pos, float powerNum, boolean isStorage) {
        this.pos = pos;
        this.powerNum = powerNum;
        this.isStorage = isStorage;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        powerNum = buf.readFloat();
        isStorage = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeFloat(powerNum);
        buf.writeBoolean(isStorage);
    }

    public static class Handler implements IMessageHandler<StorageAndTakePowerMessage, IMessage> {
        @Override
        public IMessage onMessage(StorageAndTakePowerMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    EntityPlayerMP player = ctx.getServerHandler().player;
                    World world = player.world;
                    TileEntity te = world.getTileEntity(message.pos);
                    if (te instanceof TileEntityMaidBeacon) {
                        onStorageAndTake((TileEntityMaidBeacon) te, player, message.powerNum, message.isStorage);
                    }
                });
            }
            return null;
        }

        private static void onStorageAndTake(TileEntityMaidBeacon beacon, EntityPlayer player, float powerNum, boolean isStorage) {
            PowerHandler powerHandler = player.getCapability(PowerSerializer.POWER_CAP, null);
            if (powerHandler != null) {
                if (isStorage) {
                    storageLogic(powerNum, powerHandler, beacon);
                } else {
                    takeLogic(powerNum, powerHandler, beacon);
                }
            }
        }

        private static void storageLogic(float powerNum, PowerHandler playerPower, TileEntityMaidBeacon beacon) {
            boolean playerPowerIsEnough = powerNum <= playerPower.get();
            boolean beaconNotFull = powerNum + beacon.getStoragePower() <= beacon.getMaxStorage();
            if (playerPowerIsEnough) {
                if (beaconNotFull) {
                    playerPower.min(powerNum);
                    beacon.setStoragePower(beacon.getStoragePower() + powerNum);
                } else {
                    playerPower.min(beacon.getMaxStorage() - beacon.getStoragePower());
                    beacon.setStoragePower(beacon.getMaxStorage());
                }
            }
        }

        private static void takeLogic(float powerNum, PowerHandler playerPower, TileEntityMaidBeacon beacon) {
            boolean beaconIsEnough = powerNum <= beacon.getStoragePower();
            boolean playerNotFull = powerNum + playerPower.get() < PowerHandler.MAX_POWER;
            if (beaconIsEnough) {
                if (playerNotFull) {
                    beacon.setStoragePower(beacon.getStoragePower() - powerNum);
                    playerPower.add(powerNum);
                } else {
                    beacon.setStoragePower(beacon.getStoragePower() - PowerHandler.MAX_POWER + playerPower.get());
                    playerPower.set(PowerHandler.MAX_POWER);
                }
            }
        }
    }
}
