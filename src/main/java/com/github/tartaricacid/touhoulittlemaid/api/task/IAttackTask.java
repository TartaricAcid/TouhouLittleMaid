package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.DefaultMonsterType;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMonsterList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

public interface IAttackTask extends IMaidTask {
    /**
     * 寻找合适的第一个攻击目标
     *
     * @param maid 女仆
     * @return 合适的攻击目标
     */
    static Optional<? extends LivingEntity> findFirstValidAttackTarget(EntityMaid maid) {
        return maid.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).flatMap(
                mobs -> mobs.findClosest((e) -> maid.canAttack(e) && maid.isWithinRestriction(e.blockPosition())));
    }

    /**
     * 能否攻击该对象
     *
     * @param maid   女仆
     * @param target 攻击的目标
     * @return 能否攻击？
     */
    default boolean canAttack(EntityMaid maid, LivingEntity target) {
        // 获取实体 ID
        ResourceLocation location = ForgeRegistries.ENTITY_TYPES.getKey(target.getType());
        if (location == null) {
            return false;
        }
        String id = location.toString();

        // 模组自身强制指定的
        if (target instanceof Player) {
            return false;
        }
        if (target instanceof TamableAnimal tamableAnimal) {
            // 有主的宠物也不攻击
            return tamableAnimal.getOwnerUUID() == null;
        }

        // 判断配置文件的
        if (MaidConfig.MAID_ATTACK_IGNORE.get().contains(id)) {
            return false;
        }

        // 获取女仆副手是否有妖怪名单
        ItemStack offhandItem = maid.getOffhandItem();
        if (offhandItem.is(InitItems.MONSTER_LIST.get())) {
            CompoundTag monsterList = ItemMonsterList.getMonsterList(offhandItem);
            if (monsterList.contains(id, Tag.TAG_INT)) {
                int index = monsterList.getInt(id);
                MonsterType monsterType = MonsterType.getTypeByIndex(index);
                return DefaultMonsterType.canAttack(maid, target, monsterType);
            }
        }

        // 那如果没有呢？走默认配置
        MonsterType monsterType = DefaultMonsterType.getMonsterType(target.getType());
        return DefaultMonsterType.canAttack(maid, target, monsterType);
    }

    default boolean hasExtraAttack(EntityMaid maid, Entity target) {
        return false;
    }

    default boolean doExtraAttack(EntityMaid maid, Entity target) {
        return false;
    }
}
