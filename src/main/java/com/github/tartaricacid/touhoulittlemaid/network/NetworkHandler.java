package com.github.tartaricacid.touhoulittlemaid.network;

import com.github.tartaricacid.touhoulittlemaid.network.message.*;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NetworkHandler {
    private static final String VERSION = "1.0.0";

    public static void registerPacket(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(VERSION).optional();

        registrar.playToServer(MaidModelPackage.TYPE, MaidModelPackage.STREAM_CODEC, MaidModelPackage::handle);
        registrar.playToServer(ChairModelPackage.TYPE, ChairModelPackage.STREAM_CODEC, ChairModelPackage::handle);
        registrar.playToClient(OpenChairGuiPackage.TYPE, OpenChairGuiPackage.STREAM_CODEC, OpenChairGuiPackage::handle);
        registrar.playToServer(MaidConfigPackage.TYPE, MaidConfigPackage.STREAM_CODEC, MaidConfigPackage::handle);
        registrar.playToServer(MaidTaskPackage.TYPE, MaidTaskPackage.STREAM_CODEC, MaidTaskPackage::handle);
        registrar.playToServer(SendNameTagPackage.TYPE, SendNameTagPackage.STREAM_CODEC, SendNameTagPackage::handle);
        registrar.playToClient(ItemBreakPackage.TYPE, ItemBreakPackage.STREAM_CODEC, ItemBreakPackage::handle);
        registrar.playToClient(SpawnParticlePackage.TYPE, SpawnParticlePackage.STREAM_CODEC, SpawnParticlePackage::handle);
        registrar.playToClient(SyncDataPackage.TYPE, SyncDataPackage.STREAM_CODEC, SyncDataPackage::handle);
        registrar.playToServer(WirelessIOGuiPackage.TYPE, WirelessIOGuiPackage.STREAM_CODEC, WirelessIOGuiPackage::handle);
        registrar.playToServer(WirelessIOSlotConfigPackage.TYPE, WirelessIOSlotConfigPackage.STREAM_CODEC, WirelessIOSlotConfigPackage::handle);
        registrar.playToClient(OpenBeaconGuiPackage.TYPE, OpenBeaconGuiPackage.STREAM_CODEC, OpenBeaconGuiPackage::handle);
        registrar.playToServer(SetBeaconPotionPackage.TYPE, SetBeaconPotionPackage.STREAM_CODEC, SetBeaconPotionPackage::handle);
        registrar.playToServer(StorageAndTakePowerPackage.TYPE, StorageAndTakePowerPackage.STREAM_CODEC, StorageAndTakePowerPackage::handle);
        registrar.playToServer(SetBeaconOverflowPackage.TYPE, SetBeaconOverflowPackage.STREAM_CODEC, SetBeaconOverflowPackage::handle);
        registrar.playToClient(BeaconAbsorbPackage.TYPE, BeaconAbsorbPackage.STREAM_CODEC, BeaconAbsorbPackage::handle);
        registrar.playToClient(OpenSwitcherGuiPackage.TYPE, OpenSwitcherGuiPackage.STREAM_CODEC, OpenSwitcherGuiPackage::handle);
        registrar.playToServer(SaveSwitcherDataPackage.TYPE, SaveSwitcherDataPackage.STREAM_CODEC, SaveSwitcherDataPackage::handle);
        registrar.playToServer(ToggleTabPackage.TYPE, ToggleTabPackage.STREAM_CODEC, ToggleTabPackage::handle);
        registrar.playToServer(RequestEffectPackage.TYPE, RequestEffectPackage.STREAM_CODEC, RequestEffectPackage::handle);
        registrar.playToClient(SendEffectPackage.TYPE, SendEffectPackage.STREAM_CODEC, SendEffectPackage::handle);
        registrar.playToClient(PlayMaidSoundPackage.TYPE, PlayMaidSoundPackage.STREAM_CODEC, PlayMaidSoundPackage::handle);
        registrar.playToServer(SetMaidSoundIdPackage.TYPE, SetMaidSoundIdPackage.STREAM_CODEC, SetMaidSoundIdPackage::handle);
        registrar.playToClient(ChessDataClientPackage.TYPE, ChessDataClientPackage.STREAM_CODEC, ChessDataClientPackage::handle);
        registrar.playToServer(ChessDataServerPackage.TYPE, ChessDataServerPackage.STREAM_CODEC, ChessDataServerPackage::handle);
        registrar.playToClient(FoxScrollPackage.TYPE, FoxScrollPackage.STREAM_CODEC, FoxScrollPackage::handle);
        registrar.playToServer(SetScrollPackage.TYPE, SetScrollPackage.STREAM_CODEC, SetScrollPackage::handle);
        registrar.playToClient(CheckSchedulePosPacket.TYPE, CheckSchedulePosPacket.STREAM_CODEC, CheckSchedulePosPacket::handle);
        registrar.playToClient(SyncMaidAreaPackage.TYPE, SyncMaidAreaPackage.STREAM_CODEC, SyncMaidAreaPackage::handle);
        registrar.playToServer(ServantBellSetPackage.TYPE, ServantBellSetPackage.STREAM_CODEC, ServantBellSetPackage::handle);
        registrar.playToServer(SetMonsterListMessage.TYPE, SetMonsterListMessage.STREAM_CODEC, SetMonsterListMessage::handle);
    }

    public static void sendToNearby(Entity entity, CustomPacketPayload toSend) {
        if (entity.level instanceof ServerLevel) {
            PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, toSend);
        }
    }

    public static void sendToNearby(Entity entity, CustomPacketPayload toSend, int distance) {
        if (entity.level instanceof ServerLevel serverLevel) {
            BlockPos pos = entity.blockPosition();
            PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX(), pos.getY(), pos.getZ(), distance, toSend);
        }
    }
}
