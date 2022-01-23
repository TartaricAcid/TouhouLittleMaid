package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StorageAndTakePowerMessage {
    private final BlockPos pos;
    private final float powerNum;
    private final boolean isStorage;

    public StorageAndTakePowerMessage(BlockPos pos, float powerNum, boolean isStorage) {
        this.pos = pos;
        this.powerNum = powerNum;
        this.isStorage = isStorage;
    }

    public static void encode(StorageAndTakePowerMessage message, PacketBuffer buf) {
        buf.writeBlockPos(message.pos);
        buf.writeFloat(message.powerNum);
        buf.writeBoolean(message.isStorage);
    }

    public static StorageAndTakePowerMessage decode(PacketBuffer buf) {
        return new StorageAndTakePowerMessage(buf.readBlockPos(), buf.readFloat(), buf.readBoolean());
    }

    public static void handle(StorageAndTakePowerMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
                    if (te instanceof TileEntityMaidBeacon) {
                        TileEntityMaidBeacon beacon = (TileEntityMaidBeacon) te;
                        sender.getCapability(PowerCapabilityProvider.POWER_CAP).ifPresent(power -> {
                            if (message.isStorage) {
                                storageLogic(message.powerNum, power, beacon);
                            } else {
                                takeLogic(message.powerNum, power, beacon);
                            }
                        });
                    }
                }
            });
        }
        context.setPacketHandled(true);
    }

    private static void storageLogic(float powerNum, PowerCapability playerPower, TileEntityMaidBeacon beacon) {
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

    private static void takeLogic(float powerNum, PowerCapability playerPower, TileEntityMaidBeacon beacon) {
        boolean beaconIsEnough = powerNum <= beacon.getStoragePower();
        boolean playerNotFull = powerNum + playerPower.get() < PowerCapability.MAX_POWER;
        if (beaconIsEnough) {
            if (playerNotFull) {
                beacon.setStoragePower(beacon.getStoragePower() - powerNum);
                playerPower.add(powerNum);
            } else {
                beacon.setStoragePower(beacon.getStoragePower() - PowerCapability.MAX_POWER + playerPower.get());
                playerPower.set(PowerCapability.MAX_POWER);
            }
        }
    }
}
