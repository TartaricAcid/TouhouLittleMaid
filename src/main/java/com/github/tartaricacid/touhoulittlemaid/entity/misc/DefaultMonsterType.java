package com.github.tartaricacid.touhoulittlemaid.entity.misc;

import com.github.tartaricacid.touhoulittlemaid.entity.item.AbstractEntityFromItem;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMonsterList;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.Npc;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Map;

public final class DefaultMonsterType {
    private final static Map<EntityType<?>, MonsterType> DEFAULT = Maps.newHashMap();

    public static void initDefault(Level level) {
        // 只允许初始化一次
        if (!DEFAULT.isEmpty()) {
            return;
        }

        BuiltInRegistries.ENTITY_TYPE.forEach(type -> {
            Entity entity = type.create(level);
            if (!(entity instanceof LivingEntity livingEntity)) {
                return;
            }

            // 排除一些盔甲架，还有本模组的实体，以及玩家
            if (livingEntity instanceof ArmorStand || livingEntity instanceof AbstractEntityFromItem || livingEntity instanceof Player) {
                return;
            }

            // 如果继承了 Enemy 接口，那就是敌对生物
            if (livingEntity instanceof Enemy) {
                DEFAULT.putIfAbsent(type, MonsterType.HOSTILE);
                return;
            }

            // 如果是玩家、宠物、NPC、归为友好
            if (livingEntity instanceof TamableAnimal || livingEntity instanceof Npc) {
                DEFAULT.putIfAbsent(type, MonsterType.FRIENDLY);
                return;
            }

            // 否则归为中立
            DEFAULT.putIfAbsent(type, MonsterType.NEUTRAL);
        });
    }

    public static Map<EntityType<?>, MonsterType> getMonsterList(ItemStack stack, @Nullable Level level) {
        Map<EntityType<?>, MonsterType> output = Maps.newHashMap();

        if (level == null || stack.getItem() != InitItems.MONSTER_LIST.get()) {
            return output;
        }

        // 先从物品里读取数据
        Map<ResourceLocation, MonsterType> monsterList = ItemMonsterList.getMonsterList(stack);
        monsterList.forEach((key, monsterType) -> {
            EntityType<?> entityType = BuiltInRegistries.ENTITY_TYPE.get(key);
            output.put(entityType, monsterType);
        });

        // 最后补齐默认数据
        DEFAULT.forEach(output::putIfAbsent);

        return output;
    }

    public static MonsterType getMonsterType(EntityType<?> entityType) {
        return DEFAULT.getOrDefault(entityType, MonsterType.NEUTRAL);
    }

    public static boolean canAttack(EntityMaid maid, LivingEntity target, MonsterType monsterType) {
        // 友好生物，无论什么情况不攻击
        if (monsterType == MonsterType.FRIENDLY) {
            return true;
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
