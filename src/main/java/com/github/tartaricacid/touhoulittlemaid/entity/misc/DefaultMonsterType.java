package com.github.tartaricacid.touhoulittlemaid.entity.misc;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;

public final class DefaultMonsterType {
    public static MonsterType getMonsterType(LivingEntity target) {
        // 如果继承了 Enemy 接口，那就是敌对生物
        if (target instanceof Enemy) {
            return MonsterType.HOSTILE;
        }

        // 如果是玩家、宠物、NPC、归为友好
        if (target instanceof TamableAnimal || target instanceof Npc) {
            return MonsterType.FRIENDLY;
        }

        // 否则归为中立
        return MonsterType.NEUTRAL;
    }

    public static boolean canAttack(EntityMaid maid, LivingEntity target, MonsterType monsterType) {
        // 友好生物，无论什么情况不攻击
        if (monsterType == MonsterType.FRIENDLY) {
            return false;
        }

        // 中立生物，只有玩家攻击的，或者攻击过玩家的才会攻击
        if (monsterType == MonsterType.NEUTRAL) {
            return checkNeutral(maid, target);
        }

        // 其他的，那只有敌对了，攻击
        return true;
    }

    private static boolean checkNeutral(EntityMaid maid, LivingEntity target) {
        // 先判断主人
        if (maid.getOwner() instanceof Player player) {
            // 获取攻击主人的生物
            LivingEntity lastHurtByMob = player.getLastHurtByMob();
            if (target.equals(lastHurtByMob)) {
                return true;
            }
            // 获取主人攻击过的生物
            LivingEntity lastHurtMob = player.getLastHurtMob();
            if (target.equals(lastHurtMob)) {
                return true;
            }
        }

        // 再判断女仆自身被攻击的
        LivingEntity maidLastHurtByMob = maid.getLastHurtByMob();
        return target.equals(maidLastHurtByMob);
    }
}
