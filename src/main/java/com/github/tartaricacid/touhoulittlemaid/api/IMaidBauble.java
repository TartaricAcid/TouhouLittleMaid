package com.github.tartaricacid.touhoulittlemaid.api;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import javax.annotation.Nullable;
import java.util.List;

public interface IMaidBauble {
    /**
     * * 在女仆死亡时应用的方法
     *
     * @param entityMaid 女仆对象
     * @param baubleItem 所调用的饰品
     * @param cause      伤害源
     * @return 是否取消后续死亡事件
     */
    default boolean onMaidDeath(EntityMaid entityMaid, ItemStack baubleItem, DamageSource cause) {
        return false;
    }

    /**
     * 在女仆受到攻击时触发的方法
     *
     * @param entityMaid 女仆对象
     * @param baubleItem 所调用的饰品
     * @param source     伤害源
     * @param amount     伤害值
     * @return 是否取消后续伤害事件
     */
    default boolean onMaidAttacked(EntityMaid entityMaid, ItemStack baubleItem, DamageSource source, float amount) {
        return false;
    }

    /**
     * 在女仆近战攻击时触发的方法
     *
     * @param entityMaid   女仆对象
     * @param entityTarget 女仆攻击的对象
     * @param baubleItem   所调用的饰品
     * @param damage       会造成的伤害
     * @param knockBack    会造成的击退效果
     * @param fireAspect   会造成的火焰附加等级
     * @return 包含伤害、击退、火焰附加数值的一个对象
     */
    default AttackValue onMaidAttack(EntityMaid entityMaid, Entity entityTarget, ItemStack baubleItem, float damage, int knockBack, int fireAspect) {
        return new AttackValue(damage, knockBack, fireAspect);
    }

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
    default boolean onRangedAttack(EntityMaid entityMaid, EntityLivingBase target, ItemStack baubleItem, float distanceFactor, @Nullable EntityArrow entityArrow) {
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
    default boolean onDanmakuAttack(EntityMaid entityMaid, EntityLivingBase target, ItemStack baubleItem, float distanceFactor, List<Entity> entityList) {
        return false;
    }

    default void onTick(AbstractEntityMaid maid, ItemStack bauble)
    {
    }
}
