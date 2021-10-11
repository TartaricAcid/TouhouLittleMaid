package com.github.tartaricacid.touhoulittlemaid.network;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

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
    }

    public static void sendToClientPlayer(Object message, PlayerEntity player) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), message);
    }

    public static void sendToNearby(World world, BlockPos pos, Object toSend) {
        if (world instanceof ServerWorld) {
            ServerWorld ws = (ServerWorld) world;

            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 192 * 192)
                    .forEach(p -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }

    public static void sendToNearby(Entity entity, Object toSend) {
        if (entity.level instanceof ServerWorld) {
            ServerWorld ws = (ServerWorld) entity.level;
            BlockPos pos = entity.blockPosition();

            ws.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
                    .filter(p -> p.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 192 * 192)
                    .forEach(p -> CHANNEL.send(PacketDistributor.PLAYER.with(() -> p), toSend));
        }
    }
}
