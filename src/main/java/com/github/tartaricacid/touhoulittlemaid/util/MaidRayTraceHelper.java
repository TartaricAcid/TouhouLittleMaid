package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import java.util.List;
import java.util.Optional;

public final class MaidRayTraceHelper {
    private MaidRayTraceHelper() {
    }

    /**
     * 来自于原版的获取实体指向对象的方法，
     * 很好奇为何原版将其设定为仅客户端方法
     *
     * @param player   玩家
     * @param distance 搜索距离
     * @return RayTraceResult 枚举
     */
    public static Optional<EntityMaid> rayTraceMaid(PlayerEntity player, double distance) {
        // 搜索玩家附近驯服的自己的实体
        List<Entity> maidList = player.level.getEntities(player, player.getBoundingBox()
                .inflate(distance, distance, distance), entity -> entity instanceof EntityMaid && ((EntityMaid) entity).isOwnedBy(player));
        // 遍历附近实体，查看能否玩家看见
        for (Entity maid : maidList) {
            // 二次判定，以防万一
            if (maid.isAlive() && maid instanceof EntityMaid && isEntityBeLooked(player, maid) && ((EntityMaid) maid).isOwnedBy(player)) {
                return Optional.of((EntityMaid) maid);
            }
        }
        return Optional.empty();
    }

    private static boolean isEntityBeLooked(PlayerEntity player, Entity lookEntity) {
        // 玩家朝向向量（标准化）
        Vector3d lookVec = player.getLookAngle().normalize();
        // 玩家和实体间构成的向量
        Vector3d playerAndEntityVec = new Vector3d(lookEntity.getX() - player.getX(),
                lookEntity.getBoundingBox().minY + (double) lookEntity.getEyeHeight() * 2 / 3 - (player.getY() + (double) player.getEyeHeight()),
                lookEntity.getZ() - player.getZ());
        // 玩家和实体间的距离
        double playerAndEntityDistance = playerAndEntityVec.length();
        // 玩家和实体间构成的向量（标准化）
        playerAndEntityVec = playerAndEntityVec.normalize();
        // 标准化的向量，点乘得到的直接就是 cos 夹角值
        double cosAngle = lookVec.dot(playerAndEntityVec);
        // 最大偏差，与距离有关系，距离越远，偏差应该越小
        double maxDistanceDeviation = 0.075D / playerAndEntityDistance;
        // 最后再检查一次能否看见
        return cosAngle > 1.0D - maxDistanceDeviation && player.canSee(lookEntity);
    }
}
