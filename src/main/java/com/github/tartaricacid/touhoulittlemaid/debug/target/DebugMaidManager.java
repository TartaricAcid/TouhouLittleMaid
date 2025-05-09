package com.github.tartaricacid.touhoulittlemaid.debug.target;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.VisibleForDebug;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 管理调试数据
 */
@VisibleForDebug
public class DebugMaidManager {
    public static ConcurrentHashMap<UUID, Set<UUID>> PLAYER_DEBUGGING_MAID = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, Set<UUID>> MAID_DEBUGGING_PLAYER = new ConcurrentHashMap<>();
    public static List<Function<EntityMaid, List<DebugTarget>>> DEBUG_TARGETS = new ArrayList<>();

    public static void init() {
        DEBUG_TARGETS.addAll(DefaultTargets.getDefaultTargets());
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            DEBUG_TARGETS.addAll(littleMaid.getMaidDebugTargets());
        }
    }

    /**
     * 获取女仆的所有调试目标
     *
     * @param maid 女仆
     * @return 调试目标列表
     */
    public static List<DebugTarget> getDebugTargets(EntityMaid maid) {
        return DEBUG_TARGETS.stream().flatMap(f -> f.apply(maid).stream()).toList();
    }

    /**
     * 根据女仆获取调试该女仆的玩家
     *
     * @param maid 女仆
     * @return 玩家列表
     */
    public static List<ServerPlayer> getDebuggingPlayer(EntityMaid maid) {
        Set<UUID> playerId = MAID_DEBUGGING_PLAYER.get(maid.getUUID());
        if (playerId == null) {
            return List.of();
        }
        MinecraftServer server = maid.level.getServer();
        if (server == null) {
            return List.of();
        }
        return playerId.stream()
                .map(uuid -> server.getPlayerList().getPlayer(uuid))
                .filter(Objects::nonNull).toList();
    }

    /**
     * 根据玩家获取正在调试该玩家的女仆
     *
     * @param player 玩家
     * @return 女仆列表
     */
    public static List<EntityMaid> getDebuggingMaid(ServerPlayer player) {
        Set<UUID> maidId = PLAYER_DEBUGGING_MAID.get(player.getUUID());
        if (maidId == null) {
            return List.of();
        }
        return maidId.stream()
                .map(uuid -> player.serverLevel().getEntity(uuid))
                .filter(Objects::nonNull)
                .filter(EntityMaid.class::isInstance)
                .map(EntityMaid.class::cast).toList();
    }

    /**
     * 设置正在调试的女仆
     *
     * @param player 玩家
     * @param maid   女仆
     */
    public static void setDebuggingMaid(ServerPlayer player, EntityMaid maid) {
        removeDebuggingMaid(player, maid);
        PLAYER_DEBUGGING_MAID.computeIfAbsent(player.getUUID(), k -> new HashSet<>()).add(maid.getUUID());
        MAID_DEBUGGING_PLAYER.computeIfAbsent(maid.getUUID(), k -> new HashSet<>()).add(player.getUUID());
    }

    /**
     * 移除正在调试的女仆
     *
     * @param player 玩家
     * @param maid   女仆
     */
    public static void removeDebuggingMaid(ServerPlayer player, EntityMaid maid) {
        if (PLAYER_DEBUGGING_MAID.containsKey(player.getUUID())) {
            PLAYER_DEBUGGING_MAID.get(player.getUUID()).remove(maid.getUUID());
        }
        if (MAID_DEBUGGING_PLAYER.containsKey(maid.getUUID())) {
            MAID_DEBUGGING_PLAYER.get(maid.getUUID()).remove(player.getUUID());
        }
    }

    /**
     * 切换该女仆的调试状态
     *
     * @param player 玩家
     * @param maid   女仆
     */
    public static void triggerDebuggingMaid(ServerPlayer player, EntityMaid maid) {
        if (PLAYER_DEBUGGING_MAID.containsKey(player.getUUID()) && PLAYER_DEBUGGING_MAID.get(player.getUUID()).contains(maid.getUUID())) {
            removeDebuggingMaid(player, maid);
            player.sendSystemMessage(Component.translatable("debug.touhou_little_maid.debug_stick.show_path_finder.disable"));
        } else {
            setDebuggingMaid(player, maid);
            player.sendSystemMessage(Component.translatable("debug.touhou_little_maid.debug_stick.show_path_finder.enable"));
        }
    }
}
