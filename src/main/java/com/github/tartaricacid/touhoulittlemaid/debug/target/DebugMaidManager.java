package com.github.tartaricacid.touhoulittlemaid.debug.target;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 管理调试数据
 */
public class DebugMaidManager {

    public static ConcurrentHashMap<UUID, Set<UUID>> playerDebuggingMaid = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<UUID, Set<UUID>> maidDebuggingPlayer = new ConcurrentHashMap<>();
    public static List<Function<EntityMaid, List<DebugTarget>>> debugTargets = new ArrayList<>();

    public static void init() {
        debugTargets.addAll(DefaultTargets.getDefaultTargets());
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            debugTargets.addAll(littleMaid.getMaidDebugTargets());
        }
    }

    /**
     * 获取女仆的所有调试目标
     * @param maid 女仆
     * @return 调试目标列表
     */
    public static List<DebugTarget> getDebugTargets(EntityMaid maid) {
        return debugTargets.stream().flatMap(f -> f.apply(maid).stream()).toList();
    }


    //玩家和正在调试的女仆的对应关系

    /**
     * 根据女仆获取调试该女仆的玩家
     *
     * @param maid 女仆
     * @return 玩家列表
     */
    public static List<ServerPlayer> getDebuggingPlayer(EntityMaid maid) {
        Set<UUID> playerId = maidDebuggingPlayer.get(maid.getUUID());
        if (playerId != null) {
            MinecraftServer server = maid.level().getServer();
            return playerId
                    .stream()
                    .map(pid -> server.getPlayerList().getPlayer(pid))
                    .filter(Objects::nonNull)
                    .toList();
        }
        return List.of();
    }

    /**
     * 根据玩家获取正在调试该玩家的女仆
     * @param player 玩家
     * @return 女仆列表
     */
    public static List<EntityMaid> getDebuggingMaid(ServerPlayer player) {
        Set<UUID> maidId = playerDebuggingMaid.get(player.getUUID());
        if (maidId != null) {
            return maidId
                    .stream()
                    .map(mid -> ((ServerLevel) player.level()).getEntity(mid))
                    .filter(Objects::nonNull)
                    .filter(EntityMaid.class::isInstance)
                    .map(EntityMaid.class::cast)
                    .toList();
        }
        return List.of();
    }

    /**
     * 设置正在调试的女仆
     * @param player 玩家
     * @param maid 女仆
     */
    public static void setDebuggingMaid(ServerPlayer player, EntityMaid maid) {
        if (playerDebuggingMaid.containsKey(player.getUUID())) {
            playerDebuggingMaid.get(player.getUUID()).remove(maid.getUUID());
        }
        if (maidDebuggingPlayer.containsKey(maid.getUUID())) {
            maidDebuggingPlayer.get(maid.getUUID()).remove(player.getUUID());
        }
        playerDebuggingMaid.computeIfAbsent(player.getUUID(), k -> new HashSet<>()).add(maid.getUUID());
        maidDebuggingPlayer.computeIfAbsent(maid.getUUID(), k -> new HashSet<>()).add(player.getUUID());
    }

    /**
     * 移除正在调试的女仆
     * @param player 玩家
     * @param maid 女仆
     */
    public static void removeDebuggingMaid(ServerPlayer player, EntityMaid maid) {
        if (playerDebuggingMaid.containsKey(player.getUUID())) {
            playerDebuggingMaid.get(player.getUUID()).remove(maid.getUUID());
        }
        if (maidDebuggingPlayer.containsKey(maid.getUUID())) {
            maidDebuggingPlayer.get(maid.getUUID()).remove(player.getUUID());
        }
    }

    /**
     * 切换该女仆的调试状态
     * @param player 玩家
     * @param maid 女仆
     */
    public static void triggerDebuggingMaid(ServerPlayer player, EntityMaid maid) {
        if (playerDebuggingMaid.containsKey(player.getUUID()) && playerDebuggingMaid.get(player.getUUID()).contains(maid.getUUID())) {
            removeDebuggingMaid(player, maid);
        } else {
            setDebuggingMaid(player, maid);
        }
    }
}
