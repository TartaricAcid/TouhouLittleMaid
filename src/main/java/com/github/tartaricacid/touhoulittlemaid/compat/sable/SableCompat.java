package com.github.tartaricacid.touhoulittlemaid.compat.sable;

import dev.ryanhcode.sable.Sable;
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
}
