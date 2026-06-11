package com.github.tartaricacid.touhoulittlemaid.compat.sable;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SableCompat {
    private static boolean IS_LOADED = false;

    public static void init() {
        IS_LOADED = true;
    }

    public static boolean isSableLoaded() {
        return IS_LOADED;
    }

    public static BlockPos getBlockPosWithSublevel(Level level, Entity entity, BlockPos pos) {
        if (isSableLoaded() && level == entity.level()) {
            final var sublevel = Sable.HELPER.getTrackingSubLevel(entity);
            if (sublevel != null) {
                var localPos = sublevel.logicalPose().transformPositionInverse(Vec3.atBottomCenterOf(pos));

                return BlockPos.containing(localPos);
            }
        }
        return pos;
    }

    public static Vec3 transformToGlobalPosition(Entity entity, Vec3 pos) {
        if (!isSableLoaded()) return pos;

        final var sublevel = Sable.HELPER.getTrackingSubLevel(entity);
        if (sublevel == null) {
            return pos;
        }
        return sublevel.logicalPose().transformPosition(pos);
    }

    public static BlockPos getEntityBlockPosition(Entity entity) {
        if (isSableLoaded()) {
            final var sublevel = Sable.HELPER.getTrackingSubLevel(entity);
            if (sublevel != null) {
                return BlockPos.containing(sublevel.logicalPose().transformPositionInverse(entity.position()));
            }
        }
        return entity.blockPosition();
    }

    public static boolean isInSublevel(Entity entity) {
        return isSableLoaded() && Sable.HELPER.getTrackingSubLevel(entity) != null;
    }

    public static double distToCenterSqr(Level level, BlockPos pos1, Vec3 pos2) {
        if (!isSableLoaded()) {
            return pos1.distToCenterSqr(pos2);
        }

        return Sable.HELPER.distanceSquaredWithSubLevels(level, Vec3.atCenterOf(pos1), pos2);
    }

    // 修正实体掉出子区域后没有传送到正确位置
    public static void fixSubLevelEntityLeaving(Entity entity) {
        if (!isSableLoaded()) return;

        SubLevel sublevel = Sable.HELPER.getContaining(entity);
        if (sublevel == null) return;

        if (!entity.getBoundingBox().intersects(sublevel.getPlot().getBoundingBox().toAABB().inflate(1.0))) {
            var position = entity.position();
            position = sublevel.logicalPose().transformPosition(position);

            entity.moveTo(position);
        }
    }
}
