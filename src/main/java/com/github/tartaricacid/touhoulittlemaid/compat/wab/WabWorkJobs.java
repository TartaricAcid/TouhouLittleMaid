package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * 玩家侧（右键工作模式）驱动的挖矿 / 伐木 job。
 * <p>
 * 每台女仆同时最多一个 job；job 本身不注册进 {@code WabDispatcher}（否则会被 dispatcher.tick 重复驱动），
 * 而是由 {@link #tick()} 在这里自己驱动。{@code tick()} 由 {@code WabBridge.tick()} 每帧调用。
 * 远征半径、放多余方块都在这里按契约处理，失败一律吞掉，不影响任何原有功能。
 */
public final class WabWorkJobs {
    private static final int BASE_RADIUS = 16;
    private static final int RADIUS_STEP = 8;
    private static final int MAX_DROP_PER_JOB = 8;

    /** 正在跑的 job，按女仆 UUID 索引。 */
    private static final Map<UUID, Entry> JOBS = new HashMap<>();
    /** 每台女仆下次起 job 用的半径（跨 job 保留，实现「逐步向远处进发」）。 */
    private static final Map<UUID, Integer> RADII = new HashMap<>();

    private WabWorkJobs() {
    }

    private static final class Entry {
        final WabActions.Job job;
        final String action;
        final int inventoryBefore;
        int radius;

        Entry(WabActions.Job job, String action, int radius, int inventoryBefore) {
            this.job = job;
            this.action = action;
            this.radius = radius;
            this.inventoryBefore = inventoryBefore;
        }
    }

    /** 该女仆是否已有玩家侧工作 job 在跑。 */
    public static boolean hasJob(EntityMaid maid) {
        if (maid == null) return false;
        Entry entry = JOBS.get(maid.getUUID());
        return entry != null && entry.job.isActive();
    }

    /**
     * 若这台女仆还没有活跃 job，就按她的（或全局模板的）配置起一个采集 job。
     *
     * @param action "mine" / "chop"
     */
    public static void ensureJob(EntityMaid maid, String action) {
        if (maid == null || !maid.isAlive() || !(maid.level() instanceof ServerLevel)) return;
        if (hasJob(maid)) return;
        try {
            WabWorkConfig.Settings settings = WabWorkConfig.of(maid);
            UUID uuid = maid.getUUID();
            int radius = clamp(RADII.getOrDefault(uuid, BASE_RADIUS),
                    WabWorkConfig.MIN_RADIUS, settings.maxRadius());

            JsonObject params = new JsonObject();
            params.addProperty("action", action == null ? "mine" : action);
            params.addProperty("filter", settings.filter());
            params.addProperty("count", 16);
            params.addProperty("radius", radius);

            // 半径越大，允许女仆花越多 tick 走过去；上限 12000 tick，到点自然收工。
            int budgetTicks = (int) Math.min(12000L, Math.max(600L, radius * 200L));
            // autonomous=false 且 responder=null：Job.finish() 不会碰 bridge，bridge 为 null 也安全。
            WabActions.Job job = WabActions.startGather(WabBridge.get(), maid, params, budgetTicks, false, null);
            Entry entry = new Entry(job, action, radius, inventoryCount(maid));
            JOBS.put(uuid, entry);
        } catch (Throwable ignored) {
            // 装饰性功能，失败不影响原版行为
        }
    }

    /** 由 WabBridge.tick() 每帧驱动：推进所有玩家侧 job，处理收尾。 */
    public static void tick() {
        if (JOBS.isEmpty()) return;
        for (Iterator<Map.Entry<UUID, Entry>> it = JOBS.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<UUID, Entry> kv = it.next();
            Entry entry = kv.getValue();
            if (!entry.job.isActive()) {
                try {
                    onFinished(entry);
                } catch (Throwable ignored) {
                }
                it.remove();
                continue;
            }
            try {
                entry.job.tick();
            } catch (Throwable t) {
                entry.job.cancel("工作出了点问题，先停下。");
            }
            if (!entry.job.isActive()) {
                try {
                    onFinished(entry);
                } catch (Throwable ignored) {
                }
                it.remove();
            }
        }
    }

    /** job 结束：放多余方块 + 更新下一次的远征半径。 */
    private static void onFinished(Entry entry) {
        EntityMaid maid = entry.job.maid;
        if (maid == null || !maid.isAlive()) return;
        UUID uuid = maid.getUUID();
        WabWorkConfig.Settings settings = WabWorkConfig.of(maid);
        int gained = Math.max(0, inventoryCount(maid) - entry.inventoryBefore);

        if (settings.dropExtra() && gained > 0) {
            placeExtraBlocks(maid, settings.dropBlock(), Math.min(gained, MAX_DROP_PER_JOB));
        }

        int next;
        if (gained > 0) {
            // 这一轮挖到了东西：下一轮回到近处重新扫。
            next = BASE_RADIUS;
        } else {
            // 近处没得挖了：向更远处推进，到顶后回到近处循环。
            next = entry.radius + RADIUS_STEP;
            if (next > settings.maxRadius()) next = BASE_RADIUS;
        }
        RADII.put(uuid, clamp(next, WabWorkConfig.MIN_RADIUS, settings.maxRadius()));
    }

    /** 在女仆脚边空格放下多余方块（服务端世界写入）。 */
    private static void placeExtraBlocks(EntityMaid maid, String blockId, int count) {
        if (count <= 0 || !(maid.level() instanceof ServerLevel level)) return;
        ResourceLocation key = ResourceLocation.tryParse(blockId);
        if (key == null || !BuiltInRegistries.BLOCK.containsKey(key)) return;
        Block block = BuiltInRegistries.BLOCK.get(key);
        if (block == Blocks.AIR) return;

        BlockPos base = maid.blockPosition();
        int placed = 0;
        for (int dy = -1; dy <= 1 && placed < count; dy++) {
            for (int dx = -2; dx <= 2 && placed < count; dx++) {
                for (int dz = -2; dz <= 2 && placed < count; dz++) {
                    BlockPos pos = base.offset(dx, dy, dz);
                    // 不要把自己和头顶堵住
                    if (pos.equals(base) || pos.equals(base.above())) continue;
                    if (!level.isLoaded(pos)) continue;
                    if (!level.getBlockState(pos).canBeReplaced()) continue;
                    level.setBlockAndUpdate(pos, block.defaultBlockState());
                    placed++;
                }
            }
        }
    }

    /** 用背包物品总数近似本轮收获量，避免改 WabActions.Job（got 是 private）。 */
    private static int inventoryCount(EntityMaid maid) {
        try {
            CombinedInvWrapper inv = maid.getAvailableInv(true);
            int total = 0;
            for (int i = 0; i < inv.getSlots(); i++) {
                total += inv.getStackInSlot(i).getCount();
            }
            return total;
        } catch (Throwable t) {
            return 0;
        }
    }

    private static int clamp(int value, int min, int max) {
        if (max < min) max = min;
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
