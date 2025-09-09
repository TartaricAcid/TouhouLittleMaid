package com.github.tartaricacid.touhoulittlemaid.entity.monster;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;

public interface IHasPowerPoint {
    /**
     * 获取 Power 点数值
     *
     * @return Power 点数值，与玩家 Power 数是 100 倍的关系
     */
    int getPowerPoint();

    /**
     * 掉落 P 点的方法
     *
     * @param entity 掉落该 P 点的实体
     */
    default void dropPowerPoint(LivingEntity entity) {
        // 需要考虑 doMobLoot 规则
        if (!entity.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            return;
        }
        int dropTime = 20;
        if (entity.deathTime == dropTime && !entity.level.isClientSide) {
            int totalPowerPoint = getPowerPoint();
            while (totalPowerPoint > 0) {
                int powerSplit = EntityPowerPoint.getPowerValue(totalPowerPoint);
                totalPowerPoint -= powerSplit;
                EntityPowerPoint powerPoint = new EntityPowerPoint(entity.level, entity.getX(), entity.getY(), entity.getZ(), powerSplit);
                entity.level.addFreshEntity(powerPoint);
            }
        }
    }
}
