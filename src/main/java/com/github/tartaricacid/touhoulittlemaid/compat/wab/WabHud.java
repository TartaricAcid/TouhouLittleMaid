package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

/**
 * 游戏端的好感度/信任度等数值只是「显示」，权威数据在 WAB 后台。
 * 这里负责把 WAB 下发到各女仆的 stats 数值，用动作栏形式悄悄展示给玩家，
 * 不刷屏、不打断操作。
 */
public final class WabHud {
    public static class Stats {
        public String character = "";
        public String characterName = "";
        public int affection;
        public int affectionPoints;
        public int trust;
        public int energy;
        public int mood;
        public long receivedAt;
    }

    private WabHud() {
    }

    public static void tick(WabBridge bridge) {
        if (!WabConfig.showHud()) return;
        ServerPlayer owner = bridge.currentOwner();
        if (owner == null) return;
        StringBuilder sb = new StringBuilder("WAB ");
        for (EntityMaid maid : bridge.ownedMaids()) {
            Stats stats = bridge.stats(maid.getUUID());
            if (stats == null) continue;
            sb.append(WabBridge.cleanName(maid));
            sb.append("(好感 ").append(stats.affection).append(" 信任 ").append(stats.trust);
            String shown = !stats.characterName.isEmpty() ? stats.characterName : stats.character;
            if (!shown.isEmpty()) sb.append(" @").append(shown);
            sb.append(") ");
        }
        owner.displayClientMessage(Component.literal(sb.toString()).withStyle(ChatFormatting.DARK_GRAY), true);
    }
}
