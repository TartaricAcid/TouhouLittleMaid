package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * headInfo 开着时，把绑定女仆的紧凑数值周期性推给主人的客户端（头顶渲染用）。
 * 详细的分页数据是另一条按需通道（WabDetailsRequest），这里只求常驻、小、快。
 */
public final class WabHeadSync {
    private WabHeadSync() {
    }

    public static void tick(WabBridge bridge) {
        if (!WabConfig.headInfo()) return;
        ServerPlayer owner = bridge.currentOwner();
        if (owner == null) return;
        List<WabClientHeads.Entry> list = new ArrayList<>();
        for (EntityMaid maid : bridge.ownedMaids()) {
            WabHud.Stats stats = bridge.stats(maid.getUUID());
            if (stats == null) continue;
            String name = WabBridge.cleanName(maid);
            // 头顶第一行显示 WAB 角色名（宿主在 stats 帧里下发 characterName）；
            // 老宿主只发角色 id 时退回 id，两者都没有就照旧显示女仆自己的名字。
            String l1 = !stats.characterName.isEmpty() ? stats.characterName
                    : !stats.character.isEmpty() ? stats.character : name;
            String l2 = "好感 " + stats.affection + "  信任 " + stats.trust
                    + "  心情 " + stats.mood + "  精力 " + stats.energy;
            list.add(new WabClientHeads.Entry(maid.getUUID(), l1, l2));
        }
        NetworkHandler.sendToClientPlayer(new WabHeadInfo(list), owner);
    }
}
