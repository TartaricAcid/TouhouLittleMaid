package com.github.tartaricacid.touhoulittlemaid.entity.ai.fishing;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntitySit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.*;

public final class WaterBodyDetector {
    private static final int MAX_SURFACE_BLOCKS = 8000;

    private WaterBodyDetector() {
    }

    @Nullable
    public static Result scan(ServerLevel level, EntityMaid maid) {
        BlockPos center = maid.getBrainSearchPos();
        int range = (int) maid.getRestrictRadius();
        boolean restricted = maid.hasRestriction();

        List<BlockPos> allSurface = new ArrayList<>();
        if (!collectSurface(level, center, range, restricted ? maid : null, allSurface)) {
            return null;
        }

        List<Body> bodies = labelComponents(allSurface);
        for (Body body : bodies) {
            resolveEdges(level, body);
            computeDistances(body);
            body.castTarget = findCastTarget(body);
            findShore(level, maid, body, restricted);
        }

        Body best = selectBest(bodies);
        if (best == null || best.shore == null || best.castTarget == null) {
            return null;
        }

        return new Result(new WaterBody(best.blocks.size(), best.maxDist), best.shore, best.castTarget);
    }

    private static boolean collectSurface(ServerLevel level, BlockPos center, int range,
                                          @Nullable EntityMaid restrictedMaid, List<BlockPos> out) {
        int midY = center.getY();
        int minY = Math.max(level.getMinBuildHeight(), midY - 32);
        int maxY = Math.min(level.getMaxBuildHeight(), midY + 32);

        for (int dx = -range; dx <= range; dx++) {
            for (int dz = -range; dz <= range; dz++) {
                if (out.size() >= MAX_SURFACE_BLOCKS) {
                    return !out.isEmpty();
                }
                BlockPos col = center.offset(dx, 0, dz);
                if (restrictedMaid != null && !restrictedMaid.isWithinRestriction(col)) {
                    continue;
                }
                for (int y = maxY; y >= minY; y--) {
                    BlockPos p = new BlockPos(col.getX(), y, col.getZ());
                    FluidState fluid = level.getFluidState(p);
                    if (fluid.is(FluidTags.WATER) && fluid.isSource()) {
                        if (level.getBlockState(p.above()).isAir()) {
                            out.add(p);
                        }
                        break;
                    }
                }
            }
        }
        return !out.isEmpty();
    }

    private static List<Body> labelComponents(List<BlockPos> allSurface) {
        Map<BlockPos, Body> posToBody = new HashMap<>();
        List<Body> bodies = new ArrayList<>();

        for (BlockPos start : allSurface) {
            if (posToBody.containsKey(start)) {
                continue;
            }
            Body body = new Body();
            bodies.add(body);

            Deque<BlockPos> queue = new ArrayDeque<>();
            queue.add(start);
            posToBody.put(start, body);
            body.blocks.add(start);
            body.blockSet.add(key(start));

            while (!queue.isEmpty()) {
                BlockPos cur = queue.poll();
                for (Direction d : Direction.Plane.HORIZONTAL) {
                    BlockPos nb = cur.relative(d);
                    if (posToBody.containsKey(nb)) {
                        continue;
                    }
                    for (int dy = -1; dy <= 1; dy++) {
                        BlockPos cand = nb.atY(cur.getY() + dy);
                        if (allSurface.contains(cand) && !posToBody.containsKey(cand)) {
                            posToBody.put(cand, body);
                            body.blocks.add(cand);
                            body.blockSet.add(key(cand));
                            queue.add(cand);
                            break;
                        }
                    }
                }
            }

        }
        return bodies;
    }

    private static void resolveEdges(ServerLevel level, Body body) {
        for (BlockPos p : body.blocks) {
            int wy = p.getY();
            for (Direction d : Direction.Plane.HORIZONTAL) {
                BlockPos nb = p.relative(d);
                boolean waterAtNeighbor = false;
                for (int dy = -1; dy <= 1; dy++) {
                    FluidState f = level.getFluidState(nb.atY(wy + dy));
                    if (f.is(FluidTags.WATER) && f.isSource()) {
                        waterAtNeighbor = true;
                        break;
                    }
                }
                if (!waterAtNeighbor) {
                    body.edges.add(p);
                    break;
                }
            }
        }
    }

    private static void computeDistances(Body body) {
        int n = body.blocks.size();
        Map<BlockPos, Integer> idx = new HashMap<>(n);
        for (int i = 0; i < n; i++) {
            idx.put(body.blocks.get(i), i);
        }
        body.distances = new int[n];
        Arrays.fill(body.distances, -1);

        Deque<Integer> queue = new ArrayDeque<>();
        for (BlockPos e : body.edges) {
            Integer i = idx.get(e);
            if (i != null && body.distances[i] == -1) {
                body.distances[i] = 0;
                queue.add(i);
            }
        }

        while (!queue.isEmpty()) {
            int i = queue.poll();
            BlockPos p = body.blocks.get(i);
            int nd = body.distances[i] + 1;
            for (Direction d : Direction.Plane.HORIZONTAL) {
                BlockPos nb = p.relative(d);
                for (int dy = -1; dy <= 1; dy++) {
                    BlockPos cand = nb.atY(p.getY() + dy);
                    Integer j = idx.get(cand);
                    if (j != null && body.distances[j] == -1) {
                        body.distances[j] = nd;
                        if (nd > body.maxDist) {
                            body.maxDist = nd;
                        }
                        queue.add(j);
                    }
                }
            }
        }
    }

    private static BlockPos findCastTarget(Body body) {
        int bestDist = -1;
        BlockPos best = body.blocks.get(0);
        for (int i = 0; i < body.blocks.size(); i++) {
            if (body.distances[i] > bestDist) {
                bestDist = body.distances[i];
                best = body.blocks.get(i);
            }
        }
        return best;
    }

    private static void findShore(ServerLevel level, EntityMaid maid, Body body, boolean restricted) {
        BlockPos maidPos = maid.blockPosition();
        EntityMaid checkMaid = restricted ? maid : null;

        BlockPos bestShore = null;
        double bestScore = Double.MAX_VALUE;

        for (BlockPos waterEdge : body.edges) {
            int wy = waterEdge.getY();
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                if (hasWaterAtNeighbor(level, waterEdge, wy, dir)) {
                    continue;
                }

                for (int offset = 1; offset <= 3; offset++) {
                    for (int dy : new int[]{2, 1, 0}) {
                        BlockPos shore = waterEdge.relative(dir, offset).above(dy);
                        if (!isValidShore(level, shore, checkMaid)) {
                            continue;
                        }
                        if (isOccupiedByFishingSeat(level, shore, maid)) {
                            continue;
                        }
                        double dist = Math.sqrt(shore.distSqr(maidPos));
                        double score = (dy == 0 ? 200 : dy == 1 ? 100 : 0) + offset * 10 + dist;
                        if (score < bestScore) {
                            bestScore = score;
                            bestShore = shore;
                        }
                    }
                }
            }
        }

        body.shore = bestShore;
    }

    private static boolean hasWaterAtNeighbor(ServerLevel level, BlockPos waterEdge, int wy, Direction dir) {
        BlockPos nb = waterEdge.relative(dir);
        for (int dy = -1; dy <= 1; dy++) {
            FluidState f = level.getFluidState(nb.atY(wy + dy));
            if (f.is(FluidTags.WATER) && f.isSource()) {
                return true;
            }
        }
        return false;
    }

    private static boolean isValidShore(ServerLevel level, BlockPos shore, @Nullable EntityMaid maid) {
        if (maid != null && !maid.isWithinRestriction(shore)) {
            return false;
        }
        BlockState ground = level.getBlockState(shore.below());
        if (!ground.blocksMotion()) {
            return false;
        }
        if (!level.getFluidState(shore).isEmpty()) {
            return false;
        }
        BlockState shoreState = level.getBlockState(shore);
        if (shoreState.blocksMotion() || level.getBlockState(shore.above()).blocksMotion()) {
            return false;
        }
        return true;
    }

    private static boolean isOccupiedByFishingSeat(ServerLevel level, BlockPos shore, EntityMaid self) {
        AABB checkArea = new AABB(shore.offset(-1, -2, -1), shore.offset(2, 2, 2));
        return level.getEntitiesOfClass(EntitySit.class, checkArea)
                .stream().anyMatch(s -> "fishing".equals(s.getJoyType()) && s.getFirstPassenger() != self);
    }

    private static Body selectBest(List<Body> bodies) {
        Body best = null;
        double bestScore = -1;
        for (Body body : bodies) {
            if (body.shore == null || body.castTarget == null) {
                continue;
            }
            double score = (double) body.maxDist * body.maxDist * body.blocks.size();
            if (score > bestScore) {
                bestScore = score;
                best = body;
            }
        }
        return best;
    }

    private static long key(BlockPos p) {
        return ((long) p.getX() & 0x3FFFFFFL) << 38
             | ((long) p.getY() & 0xFFFL) << 26
             | ((long) p.getZ() & 0x3FFFFFFL);
    }

    // ---------- internal class ----------

    private static class Body {
        final List<BlockPos> blocks = new ArrayList<>();
        final Set<Long> blockSet = new HashSet<>();
        final List<BlockPos> edges = new ArrayList<>();
        int[] distances;
        int maxDist;
        BlockPos castTarget;
        BlockPos shore;
    }

    // ---------- public result types ----------

    public static class WaterBody {
        public final int size;
        public final int maxDist;

        WaterBody(int size, int maxDist) {
            this.size = size;
            this.maxDist = maxDist;
        }
    }

    public static class Result {
        public final WaterBody waterBody;
        public final BlockPos shore;
        public final BlockPos castTarget;

        Result(WaterBody waterBody, BlockPos shore, BlockPos castTarget) {
            this.waterBody = waterBody;
            this.shore = shore;
            this.castTarget = castTarget;
        }
    }
}
