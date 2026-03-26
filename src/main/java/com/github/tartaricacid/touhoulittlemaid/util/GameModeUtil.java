package com.github.tartaricacid.touhoulittlemaid.util;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

/**
 * 用于判断当前玩家所处游戏是单人、服务器还是局域网联机等功能
 * <p>
 * 以下方法均应在服务端调用（player.getServer() != null）。
 * <ul>
 *   <li>单人游戏：IntegratedServer 且未开放局域网</li>
 *   <li>局域网联机（主机）：IntegratedServer 且已开放局域网，且该玩家是房主</li>
 *   <li>局域网联机（客机）：IntegratedServer 且已开放局域网，且该玩家不是房主</li>
 *   <li>专用服务器：DedicatedServer</li>
 * </ul>
 */
public class GameModeUtil {
    /**
     * 能否编辑站点信息
     *
     * @param player
     * @return
     */
    @SuppressWarnings("all")
    public static boolean canEditSite(@Nullable Player player) {
        if (player == null) {
            return false;
        }
        if (GameModeUtil.isSinglePlayer(player)) {
            // 如果是单人模式
            return true;
        } else if (GameModeUtil.isLanHost(player)) {
            // 如果是局域网联机，且玩家是房主
            return true;
        } else if (GameModeUtil.isDedicatedServer(player) && player.hasPermissions(2)) {
            // 如果是服务器玩家，且拥有 OP2 权限
            return true;
        }
        return false;
    }

    /**
     * 是否为单人游戏（未开放局域网的 IntegratedServer）
     */
    public static boolean isSinglePlayer(Player player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return false;
        }
        return server.isSingleplayer() && !server.isPublished();
    }

    /**
     * 是否为局域网联机（IntegratedServer 且已开放局域网）
     */
    public static boolean isLanGame(Player player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return false;
        }
        return server.isSingleplayer() && server.isPublished();
    }

    /**
     * 是否为局域网联机的主机玩家（房主）
     */
    public static boolean isLanHost(Player player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return false;
        }
        if (!server.isSingleplayer() || !server.isPublished()) {
            return false;
        }
        return isHostPlayer(server, player);
    }

    /**
     * 是否为局域网联机的客机玩家（非房主）
     */
    public static boolean isLanClient(Player player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return false;
        }
        if (!server.isSingleplayer() || !server.isPublished()) {
            return false;
        }
        return !isHostPlayer(server, player);
    }

    /**
     * 是否为专用服务器（DedicatedServer）
     */
    public static boolean isDedicatedServer(Player player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return false;
        }
        return server.isDedicatedServer();
    }

    /**
     * 判断指定玩家是否为 IntegratedServer 的房主
     */
    private static boolean isHostPlayer(MinecraftServer server, Player player) {
        @Nullable GameProfile hostProfile = server.getSingleplayerProfile();
        if (hostProfile == null) {
            return false;
        }
        return hostProfile.getId() != null && hostProfile.getId().equals(player.getUUID());
    }
}
