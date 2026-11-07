package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * 女仆戴上 WAB 手机后，定时往 WAB 发一条「她想说说话」的情境帧。
 * 只在桥连着的时候被 WabBridge.tick() 调用；断线期间整段逻辑不跑、计时也冻结，
 * 所以重连后沿原节奏继续，不会一口气补发，也不会有任何离线副作用。
 */
public final class WabPhone {
    /** tick() 每 40 游戏 tick 才被调一次，下面的阈值用「调用次数」记。 */
    private static final long FIRST_DELAY_CALLS = 10;   // 10 * 40 = 400 tick ≈ 20 秒
    private static final long REPEAT_CALLS = 150;       // 150 * 40 = 6000 tick = 5 分钟

    private static long ticks;
    /** 女仆 UUID → 下一次该发消息的 tick 计数。摘下手机的女仆不留在表里。 */
    private static final Map<UUID, Long> NEXT_SEND = new HashMap<>();

    private WabPhone() {
    }

    public static void tick(WabBridge bridge) {
        if (bridge == null || !bridge.connected()) {
            return;
        }
        ticks++;
        Set<UUID> wearing = new HashSet<>();
        for (EntityMaid maid : bridge.ownedMaids()) {
            if (maid == null || !maid.isAlive()) {
                continue;
            }
            ItemStack stack = maid.getWabPhone().getStackInSlot(0);
            if (stack.isEmpty() || stack.getItem() != InitItems.WAB_PHONE.get()) {
                continue;
            }
            UUID id = maid.getUUID();
            wearing.add(id);
            Long due = NEXT_SEND.get(id);
            if (due == null) {
                // 刚戴上：约 20 秒后发第一条
                NEXT_SEND.put(id, ticks + FIRST_DELAY_CALLS);
                continue;
            }
            if (ticks < due) {
                continue;
            }
            NEXT_SEND.put(id, ticks + REPEAT_CALLS);
            bridge.sendPhoneMessage(maid, buildText(bridge, maid));
        }
        // 摘下手机、或已不在名册里的女仆，计时记录一并清掉，表不会越攒越长。
        NEXT_SEND.keySet().retainAll(wearing);
    }

    /** 情境文本：单行、不带裸换行（NDJSON 一行一帧）。 */
    private static String buildText(WabBridge bridge, EntityMaid maid) {
        String name = WabBridge.cleanName(maid);
        String world = bridge.worldName();
        String where = world == null || world.isEmpty() ? "这个世界" : world;
        return name + "在" + where + "里忙了一阵，想跟你说说话。";
    }
}
