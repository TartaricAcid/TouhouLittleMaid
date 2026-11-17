package com.github.tartaricacid.touhoulittlemaid.compat.wab;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.gson.JsonObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家侧「女仆工作模式」的 Job 管理：每个女仆一个采集 Job，由 WabBridge.tick() 每 tick 驱动。
 * 复用 {@link WabActions.Job}（autonomous=false / responder=null，finish 不碰 bridge），
 * 不依赖 AI 是否连接。
 *
 * <p>远征：每轮 Job 的搜索半径从基础值起，完成后 +8 逐轮逼近 WabMaxRadius，到顶回到近处重扫。
 * 放多余方块：Job 完成时按背包里 dropBlock 的增量，在女仆脚边空格放最多 8 个，并从背包扣除。</p>
 */
public final class WabWorkJobs {
    private static final int BASE_RADIUS = 16;
    private static final int RADIUS_STEP = 8;
    private static final int MAX_DROP_PER_JOB = 8;
    private static final int BUDGET_TICKS = 20 * 60 * 5;

    private static final Map<UUID, WorkState> STATES = new ConcurrentHashMap<>();

    private static final class WorkState {
        final EntityMaid maid;
        WabActions.Job job;
        int currentRadius = BASE_RADIUS;
        long lastTickTime = -1L;
        int countBefore;
        String action = "mine";
        boolean counted;

        WorkState(EntityMaid maid) {
            this.maid = maid;
        }
    }

    private WabWorkJobs() {
    }

    /** 由 TaskWabMine / TaskWabChop 的 Behavior 调用：确保该女仆有一个采集 Job 并推进一 tick。 */
    public static void ensureJob(EntityMaid maid, String action) {
        if (maid == null || !maid.isAlive()) {
            return;
        }
        WorkState state = STATES.computeIfAbsent(maid.getUUID(), k -> new WorkState(maid));
        state.action = action == null ? "mine" : action;
        advance(maid, state);
    }

    /** 由 WabBridge.tick() 调用，推进所有女仆的工作 Job。 */
    public static void tick() {
        if (STATES.isEmpty()) {
            return;
        }
        Iterator<Map.Entry<UUID, WorkState>> iterator = STATES.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, WorkState> entry = iterator.next();
            WorkState state = entry.getValue();
            EntityMaid maid = state.maid;
            if (maid == null || !maid.isAlive()) {
                if (state.job != null) {
                    state.job.cancel("gone");
                }
                iterator.remove();
                continue;
            }
            if (!isWabWorkTask(maid)) {
                if (state.job != null) {
                    state.job.cancel("task_changed");
                }
                iterator.remove();
                continue;
            }
            try {
                advance(maid, state);
            } catch (Exception ignored) {
                // 单个女仆的采集异常不应影响桥的主循环
            }
        }
    }

    private static boolean isWabWorkTask(EntityMaid maid) {
        IMaidTask task = maid.getTask();
        if (task == null) {
            return false;
        }
        String path = task.getUid().getPath();
        return "wab_mine".equals(path) || "wab_chop".equals(path);
    }

    private static void advance(EntityMaid maid, WorkState state) {
        if (state.job != null && state.job.isActive()) {
            // Behavior 与 WabBridge.tick 都可能调到这里；同一 gameTime 只推进一次。
            long now = maid.level().getGameTime();
            if (state.lastTickTime != now) {
                state.lastTickTime = now;
                state.job.tick();
            }
            if (!state.job.isActive()) {
                onJobDone(maid, state);
            }
            return;
        }
        startJob(maid, state);
    }

    private static void startJob(EntityMaid maid, WorkState state) {
        String action = "chop".equals(state.action) ? "chop" : "mine";
        JsonObject params = new JsonObject();
        params.addProperty("action", action);
        params.addProperty("count", 16);
        params.addProperty("filter", WabWorkConfig.getFilter(maid));
        int maxRadius = WabWorkConfig.getMaxRadius(maid);
        int radius = Math.min(state.currentRadius, maxRadius);
        params.addProperty("radius", radius);

        String dropBlock = WabWorkConfig.getDropBlock(maid);
        state.countBefore = countDropBlock(maid, dropBlock);
        state.counted = true;
        state.job = WabActions.startGather(WabBridge.get(), maid, params, BUDGET_TICKS, false, null);

        state.currentRadius += RADIUS_STEP;
        if (state.currentRadius > maxRadius) {
            state.currentRadius = BASE_RADIUS;
        }
    }

    private static void onJobDone(EntityMaid maid, WorkState state) {
        if (state.counted && WabWorkConfig.getDropExtra(maid)) {
            String dropBlock = WabWorkConfig.getDropBlock(maid);
            int after = countDropBlock(maid, dropBlock);
            int extra = after - state.countBefore;
            if (extra > 0) {
                dropExtraBlocks(maid, dropBlock, Math.min(extra, MAX_DROP_PER_JOB));
            }
        }
        state.counted = false;
        // 干完一轮回到近处重新扫，资源枯竭时靠 currentRadius 再逐轮往外推。
        state.currentRadius = BASE_RADIUS;
    }

    private static int countDropBlock(EntityMaid maid, String blockId) {
        int count = 0;
        CombinedInvWrapper inv = maid.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && blockId.equals(WabSensors.itemId(stack))) {
                count += stack.getCount();
            }
        }
        return count;
    }

    /** 从女仆背包扣掉要放出的方块；扣不齐就一个都不放，避免凭空造物。 */
    private static boolean consumeDropBlock(EntityMaid maid, String blockId, int count) {
        int need = count;
        CombinedInvWrapper inv = maid.getAvailableInv(true);
        for (int i = 0; i < inv.getSlots() && need > 0; i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.isEmpty() || !blockId.equals(WabSensors.itemId(stack))) {
                continue;
            }
            ItemStack taken = inv.extractItem(i, need, false);
            need -= taken.getCount();
        }
        return need == 0;
    }

    private static void dropExtraBlocks(EntityMaid maid, String blockId, int count) {
        if (!(maid.level() instanceof ServerLevel level)) {
            return;
        }
        ResourceLocation id = ResourceLocation.tryParse(blockId);
        if (id == null) {
            return;
        }
        Block block = BuiltInRegistries.BLOCK.get(id);
        if (block == null || block == Blocks.AIR) {
            return;
        }
        BlockPos base = maid.blockPosition();
        List<BlockPos> spots = new ArrayList<>();
        for (int dy = 0; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (dx == 0 && dz == 0 && dy == 0) {
                        continue;
                    }
                    BlockPos pos = base.offset(dx, dy, dz);
                    if (level.isLoaded(pos) && level.getBlockState(pos).canBeReplaced()) {
                        spots.add(pos);
                    }
                }
            }
        }
        if (spots.isEmpty()) {
            return;
        }
        int place = Math.min(count, spots.size());
        if (!consumeDropBlock(maid, blockId, place)) {
            return;
        }
        BlockState state = block.defaultBlockState();
        for (int i = 0; i < place; i++) {
            level.setBlockAndUpdate(spots.get(i), state);
        }
    }
}
