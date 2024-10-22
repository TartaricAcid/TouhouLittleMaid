package com.github.tartaricacid.touhoulittlemaid.network;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.*;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public final class NetworkHandler {
    private static final String VERSION = "1.0.0";

    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(TouhouLittleMaid.MOD_ID, "network"),
            () -> VERSION, it -> it.equals(VERSION), it -> it.equals(VERSION));

    public static void init() {
        CHANNEL.registerMessage(0, MaidModelMessage.class, MaidModelMessage::encode, MaidModelMessage::decode, MaidModelMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(1, ChairModelMessage.class, ChairModelMessage::encode, ChairModelMessage::decode, ChairModelMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(2, OpenChairGuiMessage.class, OpenChairGuiMessage::encode, OpenChairGuiMessage::decode, OpenChairGuiMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(3, MaidConfigMessage.class, MaidConfigMessage::encode, MaidConfigMessage::decode, MaidConfigMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(4, MaidTaskMessage.class, MaidTaskMessage::encode, MaidTaskMessage::decode, MaidTaskMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(5, SendNameTagMessage.class, SendNameTagMessage::encode, SendNameTagMessage::decode, SendNameTagMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(6, ItemBreakMessage.class, ItemBreakMessage::encode, ItemBreakMessage::decode, ItemBreakMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(7, SpawnParticleMessage.class, SpawnParticleMessage::encode, SpawnParticleMessage::decode, SpawnParticleMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(8, SyncCapabilityMessage.class, SyncCapabilityMessage::encode, SyncCapabilityMessage::decode, SyncCapabilityMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(9, WirelessIOGuiMessage.class, WirelessIOGuiMessage::encode, WirelessIOGuiMessage::decode, WirelessIOGuiMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(10, WirelessIOSlotConfigMessage.class, WirelessIOSlotConfigMessage::encode, WirelessIOSlotConfigMessage::decode, WirelessIOSlotConfigMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(11, OpenBeaconGuiMessage.class, OpenBeaconGuiMessage::encode, OpenBeaconGuiMessage::decode, OpenBeaconGuiMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(12, SetBeaconPotionMessage.class, SetBeaconPotionMessage::encode, SetBeaconPotionMessage::decode, SetBeaconPotionMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(13, StorageAndTakePowerMessage.class, StorageAndTakePowerMessage::encode, StorageAndTakePowerMessage::decode, StorageAndTakePowerMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(14, SetBeaconOverflowMessage.class, SetBeaconOverflowMessage::encode, SetBeaconOverflowMessage::decode, SetBeaconOverflowMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(15, BeaconAbsorbMessage.class, BeaconAbsorbMessage::encode, BeaconAbsorbMessage::decode, BeaconAbsorbMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(16, OpenSwitcherGuiMessage.class, OpenSwitcherGuiMessage::encode, OpenSwitcherGuiMessage::decode, OpenSwitcherGuiMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(17, SaveSwitcherDataMessage.class, SaveSwitcherDataMessage::encode, SaveSwitcherDataMessage::decode, SaveSwitcherDataMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(18, ToggleTabMessage.class, ToggleTabMessage::encode, ToggleTabMessage::decode, ToggleTabMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(19, RequestEffectMessage.class, RequestEffectMessage::encode, RequestEffectMessage::decode, RequestEffectMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(20, SendEffectMessage.class, SendEffectMessage::encode, SendEffectMessage::decode, SendEffectMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(21, PlayMaidSoundMessage.class, PlayMaidSoundMessage::encode, PlayMaidSoundMessage::decode, PlayMaidSoundMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(22, SetMaidSoundIdMessage.class, SetMaidSoundIdMessage::encode, SetMaidSoundIdMessage::decode, SetMaidSoundIdMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(23, GomokuToClientMessage.class, GomokuToClientMessage::encode, GomokuToClientMessage::decode, GomokuToClientMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(24, GomokuToServerMessage.class, GomokuToServerMessage::encode, GomokuToServerMessage::decode, GomokuToServerMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(25, FoxScrollMessage.class, FoxScrollMessage::encode, FoxScrollMessage::decode, FoxScrollMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(26, SetScrollData.class, SetScrollData::encode, SetScrollData::decode, SetScrollData::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(27, CheckSchedulePosMessage.class, CheckSchedulePosMessage::encode, CheckSchedulePosMessage::decode, CheckSchedulePosMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(28, SyncMaidAreaMessage.class, SyncMaidAreaMessage::encode, SyncMaidAreaMessage::decode, SyncMaidAreaMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(29, ServantBellSetMessage.class, ServantBellSetMessage::encode, ServantBellSetMessage::decode, ServantBellSetMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(30, SetMonsterListMessage.class, SetMonsterListMessage::encode, SetMonsterListMessage::decode, SetMonsterListMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(31, MaidSubConfigMessage.class, MaidSubConfigMessage::encode, MaidSubConfigMessage::decode, MaidSubConfigMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(32, RefreshMaidBrainMessage.class, RefreshMaidBrainMessage::encode, RefreshMaidBrainMessage::decode, RefreshMaidBrainMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(33, CChessToClientMessage.class, CChessToClientMessage::encode, CChessToClientMessage::decode, CChessToClientMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(34, CChessToServerMessage.class, CChessToServerMessage::encode, CChessToServerMessage::decode, CChessToServerMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
        CHANNEL.registerMessage(35, WChessToClientMessage.class, WChessToClientMessage::encode, WChessToClientMessage::decode, WChessToClientMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        CHANNEL.registerMessage(36, WChessToServerMessage.class, WChessToServerMessage::encode, WChessToServerMessage::decode, WChessToServerMessage::handle,
                Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

    public static void sendToClientPlayer(Object message, Player player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), message);
    }

    public static void sendToNearby(Level world, BlockPos pos, Object toSend) {
        if (world instanceof ServerLevel) {
            ServerLevel ws = (ServerLevel) world;

            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 192 * 192)
                    .forEach(p -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }

    public static void sendToNearby(Entity entity, Object toSend) {
        if (entity.level instanceof ServerLevel) {
            ServerLevel ws = (ServerLevel) entity.level();
            BlockPos pos = entity.blockPosition();

            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 192 * 192)
                    .forEach(p -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }

    public static void sendToNearby(Entity entity, Object toSend, int distance) {
        if (entity.level instanceof ServerLevel) {
            ServerLevel ws = (ServerLevel) entity.level();
            BlockPos pos = entity.blockPosition();

            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).stream()
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < distance * distance)
                    .forEach(p -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }
}
