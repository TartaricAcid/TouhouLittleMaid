package com.github.tartaricacid.touhoulittlemaid.api;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;
import java.util.List;

public interface IMaidBauble {

    /**
     * 在普通远程攻击模式下触发
     *
     * @param entityMaid     女仆对象
     * @param target         女仆瞄准的目标
     * @param baubleItem     所调用的饰品
     * @param distanceFactor 距离因子，其实也就是蓄力时长
     * @param entityArrow    通过女仆背包内物品获取得到的实体箭
     * @return 是否取消后续普通远程攻击事件
     */
    default boolean onRangedAttack(AbstractEntityMaid entityMaid, EntityLivingBase target, ItemStack baubleItem, float distanceFactor, @Nullable EntityArrow entityArrow) {
        return false;
    }

    /**
     * 在弹幕攻击模式下触发
     *
     * @param entityMaid     女仆对象
     * @param target         女仆瞄准的目标
     * @param baubleItem     所调用的饰品
     * @param distanceFactor 距离因子，其实也就是蓄力时长
     * @param entityList     女仆附近的 Mob 列表，用于判定弹幕射击的档数
     * @return 是否取消后续弹幕攻击事件
     */
    default boolean onDanmakuAttack(AbstractEntityMaid entityMaid, EntityLivingBase target, ItemStack baubleItem, float distanceFactor, List<Entity> entityList) {
        return false;
    }

    default void onTick(AbstractEntityMaid maid, ItemStack bauble)
    {
    }
}
