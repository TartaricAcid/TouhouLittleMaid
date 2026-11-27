package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 头顶信息在客户端最近的一份缓存：服务器每 2 秒推一次（WabHeadSync），
 * 超过 15 秒没推就当过期不再画。故意的纯类，不带任何客户端专属类型，
 * 两边都能加载，服务端多挂一份空实现毫无代价。
 */
public final class WabClientHeads {
    public record Entry(UUID maid, String l1, String l2) {
    }

    private static final Map<UUID, Entry> ENTRIES = new HashMap<>();
    private static final long STALE_MS = 15000;
    private static volatile long receivedAt;

    private WabClientHeads() {
    }

    public static void update(List<Entry> list) {
        synchronized (WabClientHeads.class) {
            ENTRIES.clear();
            if (list != null) {
                for (Entry entry : list) ENTRIES.put(entry.maid(), entry);
            }
            receivedAt = System.currentTimeMillis();
        }
    }

    public static Entry get(UUID maid) {
        synchronized (WabClientHeads.class) {
            if (System.currentTimeMillis() - receivedAt > STALE_MS) return null;
            return ENTRIES.get(maid);
        }
    }
}
