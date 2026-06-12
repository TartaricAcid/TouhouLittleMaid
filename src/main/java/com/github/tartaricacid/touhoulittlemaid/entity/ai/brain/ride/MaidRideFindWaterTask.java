package com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.ride;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.entity.fishing.IFishingType;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.task.MaidCheckRateTask;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing.FishingTypeManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.MaidFishingHook;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class MaidRideFindWaterTask extends MaidCheckRateTask {
    private static final int MAX_DELAY_TIME = 100;
    private static final int MAX_BFS_SIZE = 300; // 安全熔断，正常水体在 6 格半径内不会超过此值

    private final int verticalSearchRange;
    private final int searchRange;

    protected int verticalSearchStart;
    private IFishingType fishingType = null;
    private BlockPos waterPos = null;
    private long chatBubbleKey = -1;
    private String lastBubbleLangKey = null; // 追踪上次气泡类型，消息变化时强制替换

    public MaidRideFindWaterTask(int searchRange, int verticalSearchRange) {
        super(ImmutableMap.of());
        this.searchRange = searchRange;
        this.verticalSearchRange = verticalSearchRange;
        this.setMaxCheckRate(MAX_DELAY_TIME);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel worldIn, EntityMaid owner) {
        return super.checkExtraStartConditions(worldIn, owner) && owner.fishing == null;
    }

    @Override
    protected void start(ServerLevel worldIn, EntityMaid maid, long gameTimeIn) {
        ItemStack mainHandItem = maid.getMainHandItem();
        this.fishingType = FishingTypeManager.getFishingType(mainHandItem);

        if (this.fishingType.isFishingRod(mainHandItem)) {
            // 每次抛竿前重新搜水，保证水域变化（填湖/扩湖/水位变化）后自动更新出杆位置
            if (waterPos == null) {
                this.searchForDestination(worldIn, maid, mainHandItem);
                if (waterPos == null) {
                    showChatBubble(maid, "chat_bubble.touhou_little_maid.inner.fishing.no_water");
                    return;
                }
            }
            if (this.fishingType.suitableFishingHook(maid, worldIn, mainHandItem, waterPos)) {
                clearChatBubble(maid);
                Vec3 centerPos = Vec3.atCenterOf(this.waterPos);

                MaidFishingHook fishingHook = this.fishingType.getFishingHook(maid, worldIn, mainHandItem, centerPos);
                worldIn.addFreshEntity(fishingHook);

                worldIn.playSound(null, maid.getX(), maid.getY(), maid.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (worldIn.getRandom().nextFloat() * 0.4F + 0.8F));
                maid.swing(InteractionHand.MAIN_HAND);
                maid.getLookControl().setLookAt(centerPos);
                waterPos = null; // 抛完强制下次重搜，探测水域变化
            } else {
                waterPos = null;
            }
        } else {
            showChatBubble(maid, "chat_bubble.touhou_little_maid.inner.fishing.no_rod");
        }
    }

    /**
     * 显示聊天气泡，消息类型变化时强制替换旧气泡
     */
    private void showChatBubble(EntityMaid maid, String langKey) {
        if (!langKey.equals(this.lastBubbleLangKey)) {
            clearChatBubble(maid);
            this.lastBubbleLangKey = langKey;
        }
        this.chatBubbleKey = maid.getChatBubbleManager().addTextChatBubbleIfTimeout(langKey, this.chatBubbleKey);
    }

    /**
     * 清除当前气泡（抛竿成功或找到坐具后调用）
     */
    private void clearChatBubble(EntityMaid maid) {
        if (this.chatBubbleKey >= 0) {
            maid.getChatBubbleManager().removeChatBubble(this.chatBubbleKey);
            maid.getChatBubbleManager().forceUpdateChatBubble();
            this.chatBubbleKey = -1;
        }
        this.lastBubbleLangKey = null;
    }

    protected final void searchForDestination(ServerLevel worldIn, EntityMaid maid, ItemStack rod) {
        BlockPos centrePos = maid.blockPosition();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        Set<BlockPos> visitedWater = new HashSet<>();
        BlockPos firstWaterBlock = null;
        for (int y = this.verticalSearchStart; y <= this.verticalSearchRange; y = y > 0 ? -y : 1 - y) {
            for (int i = 0; i < searchRange; ++i) {
                for (int x = 0; x <= i; x = x > 0 ? -x : 1 - x) {
                    for (int z = x < i && x > -i ? i : 0; z <= i; z = z > 0 ? -z : 1 - z) {
                        mutableBlockPos.setWithOffset(centrePos, x, y - 1, z);
                        if (!maid.isWithinRestriction(mutableBlockPos)) { continue; }
                        if (!this.fishingType.suitableFishingHook(maid, worldIn, rod, mutableBlockPos)) { continue; }
                        BlockPos waterBlock = mutableBlockPos.immutable();
                        if (visitedWater.contains(waterBlock)) continue; // 已探索过的水体，跳过
                        if (firstWaterBlock == null) firstWaterBlock = waterBlock;
                        // BFS 洪水填充，映射整个连通水体
                        Map<Integer, Integer> layerCount = new HashMap<>();
                        int[] bbox = new int[6];
                        Set<BlockPos> waterBody = floodFillWaterBody(worldIn, waterBlock, maid, layerCount, bbox);
                        visitedWater.addAll(waterBody);
                        if (waterBody.size() >= 50) {
                            BlockPos center = findBestOpenWaterCenter(worldIn, waterBody, layerCount, bbox);
                            if (center != null) {
                                mutableBlockPos.set(center);
                                maid.getLookControl().setLookAt(mutableBlockPos.getX(), mutableBlockPos.getY(), mutableBlockPos.getZ());
                                this.waterPos = mutableBlockPos.immutable();
                                this.setNextCheckTickCount(5);
                                return;
                            }
                        }
                        // 此水体无合格开阔水域 → 继续螺旋搜索下一个不连通的水坑
                    }
                }
            }
        }
        // 终极回退：所有水坑都不满足，用第一个水方块 P 抛竿
        if (firstWaterBlock != null) {
            mutableBlockPos.set(firstWaterBlock);
            maid.getLookControl().setLookAt(mutableBlockPos.getX(), mutableBlockPos.getY(), mutableBlockPos.getZ());
            this.waterPos = mutableBlockPos.immutable();
            this.setNextCheckTickCount(5);
            return;
        }
    }


    /**
     * BFS 洪水填充：从 seed 出发，6-邻接搜索所有连通的水方块
     *
     * @return 连通水体的所有方块坐标集合
     */
    private Set<BlockPos> floodFillWaterBody(ServerLevel world, BlockPos seed, EntityMaid maid,
                                              Map<Integer, Integer> layerCountOut, int[] bboxOut) {
        Set<BlockPos> waterBody = new HashSet<>();
        Queue<BlockPos> queue = new ArrayDeque<>();
        queue.add(seed);
        waterBody.add(seed);

        int minX = seed.getX(), maxX = seed.getX();
        int minY = seed.getY(), maxY = seed.getY();
        int minZ = seed.getZ(), maxZ = seed.getZ();

        // 水平：以女仆为参照系，(searchRange-1)格射程 + 2格5×5窗口余量，减去seed到女仆的距离
        int seedToMaidC = Math.max(Math.abs(seed.getX() - maid.blockPosition().getX()),
                                    Math.abs(seed.getZ() - maid.blockPosition().getZ()));
        int hLimit = this.searchRange + 1 - seedToMaidC;
        int vLimit = this.verticalSearchRange;

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            layerCountOut.merge(pos.getY(), 1, Integer::sum);

            for (Direction dir : Direction.values()) {
                BlockPos neighbor = pos.relative(dir);
                if (waterBody.contains(neighbor)) continue;
                // 水平距离限定：防止向海洋无限扩张
                if (Math.abs(neighbor.getX() - seed.getX()) > hLimit) continue;
                if (Math.abs(neighbor.getZ() - seed.getZ()) > hLimit) continue;
                // 垂直距离限定
                if (Math.abs(neighbor.getY() - seed.getY()) > vLimit) continue;
                if (!maid.isWithinRestriction(neighbor)) continue;
                if (!isValidWaterBlock(world, neighbor)) continue;

                waterBody.add(neighbor);
                queue.add(neighbor);

                if (waterBody.size() >= MAX_BFS_SIZE) {
                    // 安全熔断：水体太大（海洋/大湖），停止扩张
                    TouhouLittleMaid.LOGGER.warn("[RideFish] BFS_SIZE_LIMIT seed={} size={}", seed, waterBody.size());
                    break;
                }

                if (neighbor.getX() < minX) minX = neighbor.getX();
                if (neighbor.getX() > maxX) maxX = neighbor.getX();
                if (neighbor.getY() < minY) minY = neighbor.getY();
                if (neighbor.getY() > maxY) maxY = neighbor.getY();
                if (neighbor.getZ() < minZ) minZ = neighbor.getZ();
                if (neighbor.getZ() > maxZ) maxZ = neighbor.getZ();
            }
            if (waterBody.size() >= MAX_BFS_SIZE) break;
        }

        bboxOut[0] = minX; bboxOut[1] = maxX;
        bboxOut[2] = minY; bboxOut[3] = maxY;
        bboxOut[4] = minZ; bboxOut[5] = maxZ;
        return waterBody;
    }

    /**
     * 在给定水体中寻找最佳开阔水域中心
     * 五阶段管线：Z 过滤 → 2D 前缀和 → 滑动窗口 → 两级匹配（水+空气 / 仅水）→ 质心优选
     *
     * @return 最佳浮漂放置位置，找不到返回 null
     */
    private BlockPos findBestOpenWaterCenter(ServerLevel world, Set<BlockPos> waterBody,
                                              Map<Integer, Integer> layerCount, int[] bbox) {
        int minX = bbox[0], maxX = bbox[1];
        int minY = bbox[2], maxY = bbox[3];
        int minZ = bbox[4], maxZ = bbox[5];

        List<BlockPos> validStrict = new ArrayList<>();
        List<BlockPos> validWaterOnly = new ArrayList<>();

        // Z 过滤 + 滑动窗口扫描
        for (int y = minY; y < maxY; y++) {
            int countLow = layerCount.getOrDefault(y, 0);
            int countHigh = layerCount.getOrDefault(y + 1, 0);
            if (countLow < 25 || countHigh < 25) continue; // Z 过滤：单层不够 5×5 直接跳

            int[][] prefixLow = build2DPrefixSum(waterBody, y, minX, maxX, minZ, maxZ);
            int[][] prefixHigh = build2DPrefixSum(waterBody, y + 1, minX, maxX, minZ, maxZ);

            for (int cx = minX; cx <= maxX - 4; cx++) {
                for (int cz = minZ; cz <= maxZ - 4; cz++) {
                    if (queryWindowSum(prefixLow, cx, cz, minX, minZ) != 25) continue;
                    if (queryWindowSum(prefixHigh, cx, cz, minX, minZ) != 25) continue;

                    int centerX = cx + 2;
                    int centerZ = cz + 2;
                    BlockPos center = new BlockPos(centerX, y + 1, centerZ);

                    // 空气层检查（Y+1 和 Y+2，相对于浮漂位置）
                    boolean airOk = verifyAirLayer5x5(world, centerX, y + 2, centerZ)
                            && verifyAirLayer5x5(world, centerX, y + 3, centerZ);

                    if (airOk) {
                        validStrict.add(center);
                    } else {
                        validWaterOnly.add(center);
                    }
                }
            }
        }

        // 三级优选
        List<BlockPos> candidates;
        if (!validStrict.isEmpty()) {
            candidates = validStrict;
        } else if (!validWaterOnly.isEmpty()) {
            candidates = validWaterOnly;
        } else {
            return null;
        }

        // 质心优选：离水体几何中心最近
        double avgX = waterBody.stream().mapToDouble(BlockPos::getX).average().orElse(0);
        double avgZ = waterBody.stream().mapToDouble(BlockPos::getZ).average().orElse(0);

        return candidates.stream()
                .min((a, b) -> Double.compare(
                        Math.pow(a.getX() - avgX, 2) + Math.pow(a.getZ() - avgZ, 2),
                        Math.pow(b.getX() - avgX, 2) + Math.pow(b.getZ() - avgZ, 2)))
                .orElse(null);
    }

    /**
     * 对指定 Y 层构建 2D 前缀和（积分图）
     * M[i][j] = 1 if (minX+i, Y, minZ+j) ∈ waterBody else 0
     */
    private int[][] build2DPrefixSum(Set<BlockPos> waterBody, int y, int minX, int maxX, int minZ, int maxZ) {
        int w = maxX - minX + 1;
        int d = maxZ - minZ + 1;
        int[][] prefix = new int[w + 1][d + 1];

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < d; j++) {
                int val = waterBody.contains(new BlockPos(minX + i, y, minZ + j)) ? 1 : 0;
                prefix[i + 1][j + 1] = val + prefix[i][j + 1] + prefix[i + 1][j] - prefix[i][j];
            }
        }
        return prefix;
    }

    /**
     * O(1) 查询 5×5 窗口中有效水方块的数量
     */
    private int queryWindowSum(int[][] prefix, int x, int z, int minX, int minZ) {
        int i = x - minX + 1;
        int j = z - minZ + 1;
        return prefix[i + 4][j + 4] - prefix[i - 1][j + 4] - prefix[i + 4][j - 1] + prefix[i - 1][j - 1];
    }

    /**
     * 验证以 (centerX, y, centerZ) 为中心的 5×5 区域是否全为空气或睡莲
     */
    private boolean verifyAirLayer5x5(ServerLevel world, int centerX, int y, int centerZ) {
        BlockPos.MutableBlockPos mp = new BlockPos.MutableBlockPos();
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                mp.set(centerX + dx, y, centerZ + dz);
                BlockState bs = world.getBlockState(mp);
                if (!bs.isAir() && !bs.is(Blocks.LILY_PAD)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 判定单个方块是否为有效水方块
     * 对应 MaidFishingHook.getOpenWaterTypeForBlock 的 INSIDE_WATER 条件：
     * 非空气、非睡莲、是水源（含气泡柱）、无碰撞体积
     */
    private boolean isValidWaterBlock(ServerLevel world, BlockPos pos) {
        BlockState bs = world.getBlockState(pos);
        if (bs.isAir() || bs.is(Blocks.LILY_PAD)) {
            return false;
        }
        FluidState fs = world.getFluidState(pos);
        return fs.is(FluidTags.WATER) && fs.isSource() && bs.getCollisionShape(world, pos).isEmpty();
    }
}