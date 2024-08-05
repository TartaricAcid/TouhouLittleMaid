package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.data.PowerAttachment;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataAttachment;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityMaidBeacon;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record StorageAndTakePowerPackage(BlockPos pos, float powerNum,
                                         boolean isStorage) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<StorageAndTakePowerPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("save_and_take_power"));
    public static final StreamCodec<ByteBuf, StorageAndTakePowerPackage> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            StorageAndTakePowerPackage::pos,
            ByteBufCodecs.FLOAT,
            StorageAndTakePowerPackage::powerNum,
            ByteBufCodecs.BOOL,
            StorageAndTakePowerPackage::isStorage,
            StorageAndTakePowerPackage::new
    );

    public static void handle(StorageAndTakePowerPackage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer sender = (ServerPlayer) context.player();
            if (sender == null) {
                return;
            }
            Level world = sender.level();
            if (world.isLoaded(message.pos)) {
                BlockEntity te = world.getBlockEntity(message.pos);
                if (te instanceof TileEntityMaidBeacon) {
                    TileEntityMaidBeacon beacon = (TileEntityMaidBeacon) te;
                    PowerAttachment power = sender.getData(InitDataAttachment.POWER_NUM);
                    if (message.isStorage) {
                        storageLogic(message.powerNum, power, beacon);
                    } else {
                        takeLogic(message.powerNum, power, beacon);
                    }
                }
            }
        });
    }

    private static void storageLogic(float powerNum, PowerAttachment playerPower, TileEntityMaidBeacon beacon) {
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

    private static void takeLogic(float powerNum, PowerAttachment playerPower, TileEntityMaidBeacon beacon) {
        boolean beaconIsEnough = powerNum <= beacon.getStoragePower();
        boolean playerNotFull = powerNum + playerPower.get() < PowerAttachment.MAX_POWER;
        if (beaconIsEnough) {
            if (playerNotFull) {
                beacon.setStoragePower(beacon.getStoragePower() - powerNum);
                playerPower.add(powerNum);
            } else {
                beacon.setStoragePower(beacon.getStoragePower() - PowerAttachment.MAX_POWER + playerPower.get());
                playerPower.set(PowerAttachment.MAX_POWER);
            }
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
