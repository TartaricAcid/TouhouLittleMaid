package com.github.tartaricacid.touhoulittlemaid.api.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.task.IRangedAttackTask;
import com.github.tartaricacid.touhoulittlemaid.api.task.meal.MaidMealType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.jetbrains.annotations.ApiStatus;

import java.util.Random;

public interface IMaidBauble {
    Random RANDOM = new Random();

    /**
     * 当女仆装备饰品时，每 tick 都会执行此方法
     *
     * @param maid       EntityMaid
     * @param baubleItem ItemStack
     */
    default void onTick(EntityMaid maid, ItemStack baubleItem) {
    }

    /**
     * 当女仆受到伤害时，在伤害计算前调用此方法，可以通过返回值决定是否取消此次伤害
     *
     * @param maid       女仆
     * @param baubleItem 饰品物品
     * @param source     伤害类型
     * @param damage     伤害值，可修改此值来改变最终伤害
     * @return 是否取消此次伤害，返回 true 则取消伤害，返回 false 则不取消伤害，取消伤害后，后续饰品不再触发此方法
     */
    @ApiStatus.AvailableSince("1.4.2")
    default boolean onInjured(EntityMaid maid, ItemStack baubleItem, DamageSource source, MutableFloat damage) {
        return false;
    }

    /**
     * 当女仆即将死亡时调用此方法，可以通过返回值决定是否取消此次死亡
     *
     * @param maid       女仆
     * @param baubleItem 饰品物品
     * @param source     伤害类型
     * @return 是否取消此次死亡，返回 true 则取消死亡，返回 false 则不取消死亡，取消死亡后，后续饰品不再触发此方法
     */
    @ApiStatus.AvailableSince("1.4.2")
    default boolean onDeath(EntityMaid maid, ItemStack baubleItem, DamageSource source) {
        return false;
    }

    /**
     * 当女仆装备饰品时调用此方法，可以在此时修改女仆的 attribute
     *
     * @param maid       女仆
     * @param baubleItem 饰品物品
     */
    @ApiStatus.AvailableSince("1.4.2")
    default void onPutOn(EntityMaid maid, ItemStack baubleItem) {
    }

    /**
     * 当女仆卸下饰品时调用此方法，可以在此时修改女仆的 attribute
     *
     * @param maid       女仆
     * @param baubleItem 饰品物品
     */
    @ApiStatus.AvailableSince("1.4.2")
    default void onTakeOff(EntityMaid maid, ItemStack baubleItem) {
    }

    /**
     * 当女仆进行近战攻击时调用此方法
     *
     * @param maid       女仆
     * @param baubleItem 饰品物品
     * @param target     近战攻击目标
     */
    @ApiStatus.AvailableSince("1.4.2")
    default void onMeleeAttack(EntityMaid maid, ItemStack baubleItem, Entity target) {
    }

    /**
     * 当女仆进行远程攻击时调用此方法
     *
     * @param maid       女仆
     * @param baubleItem 饰品物品
     * @param task       远程攻击任务，一定是 IRangedAttackTask 的子类，可能是弓兵、弹幕，三叉戟、枪械，或者其他附属模组使用的远程攻击方法
     */
    @ApiStatus.AvailableSince("1.4.2")
    default void onRangedAttack(EntityMaid maid, ItemStack baubleItem, IRangedAttackTask task) {
    }

    /**
     * 当女仆吃东西时调用此方法
     *
     * @param maid       女仆
     * @param baubleItem 饰品物品
     * @param foodItem   食物物品
     * @param mealType   进食类型，是工作餐还是回血餐还是家庭餐
     */
    @ApiStatus.AvailableSince("1.4.2")
    default void onMaidEat(EntityMaid maid, ItemStack baubleItem, ItemStack foodItem, MaidMealType mealType) {
    }

    /**
     * 当女仆好感度等级发生改变时，调用此方法
     * <p>
     * 虽然女仆设定上是只能升级不会降级，但是仍然有创造模式道具可以强制降级。<br>
     * 所以也需要考虑上 oldLevel 和 newLevel 的大小
     *
     * @param maid       女仆
     * @param baubleItem 饰品物品
     * @param oldLevel   旧等级
     * @param newLevel   新等级
     */
    @ApiStatus.AvailableSince("1.4.2")
    default void onFavorabilityLevelChange(EntityMaid maid, ItemStack baubleItem, int oldLevel, int newLevel) {
    }

    /**
     * 是否将当前佩戴的饰品同步到客户端。
     * <p>
     * 默认情况下不进行同步。若饰品需要在客户端渲染特殊佩戴效果（例如自定义模型），
     * 应返回 true，服务器会把饰品物品数据发送到客户端以供渲染使用。
     *
     * @param maid       女仆实体
     * @param baubleItem 饰品物品堆
     * @return 若需同步到客户端返回 true，否则返回 false
     */
    @ApiStatus.AvailableSince("1.4.7")
    default boolean syncClient(EntityMaid maid, ItemStack baubleItem) {
        return false;
    }

    /**
     * Get a chat bubble when maid has the bauble
     * <p>
     * 已废弃，不再使用
     *
     * @return chat bubble id
     */
    @Deprecated
    default String getChatBubbleId() {
        return StringUtils.EMPTY;
    }
}
