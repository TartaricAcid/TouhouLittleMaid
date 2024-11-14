package com.github.tartaricacid.touhoulittlemaid.api.task;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.data.inner.AttackListData;
import com.github.tartaricacid.touhoulittlemaid.entity.item.AbstractEntityFromItem;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.DefaultMonsterType;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTaskData;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.task.AttackTaskConfigContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
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

        // 排除一些盔甲架，还有本模组的实体，以及玩家
        if (target instanceof ArmorStand || target instanceof AbstractEntityFromItem || target instanceof Player) {
            return false;
        }
        // 有主的宠物也不攻击
        if (target instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwnerUUID() != null) {
            return false;
        }

        // 判断配置文件的
        if (MaidConfig.MAID_ATTACK_IGNORE.get().contains(id)) {
            return false;
        }

        MonsterType monsterType;
        AttackListData attackListData = maid.getData(InitTaskData.ATTACK_LIST);
        if (attackListData != null && attackListData.attackGroups().containsKey(location)) {
            // 获取女仆 Task Data 里设置的
            monsterType = attackListData.attackGroups().get(location);
        } else {
            // 那如果没有呢？走默认配置
            monsterType = DefaultMonsterType.getMonsterType(target);
        }
        return DefaultMonsterType.canAttack(maid, target, monsterType);
    }

    @Override
    default MenuProvider getTaskConfigGuiProvider(EntityMaid maid) {
        final int entityId = maid.getId();
        return new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.literal("Maid Attack Config Container");
            }

            @Override
            public AbstractMaidContainer createMenu(int index, Inventory playerInventory, Player player) {
                return new AttackTaskConfigContainer(index, playerInventory, entityId);
            }
        };
    }

    default boolean hasExtraAttack(EntityMaid maid, Entity target) {
        return false;
    }

    default boolean doExtraAttack(EntityMaid maid, Entity target) {
        return false;
    }

    @Override
    default boolean enablePanic(EntityMaid maid) {
        return false;
    }
}
