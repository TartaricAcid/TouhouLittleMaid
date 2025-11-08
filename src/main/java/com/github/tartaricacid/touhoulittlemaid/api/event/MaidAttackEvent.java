package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * 女仆受到攻击时的事件，此时还没有进行任何伤害类型、敌友类型判断，是最早触发的事件，等同于 LivingAttackEvent
 * <p>
 * 各个伤害执行顺序：
 * MaidAttackEvent -> MaidHurtEvent -> MaidDamageEvent -> 最终受到伤害
 * <p>
 * 此事件是可取消的，取消后女仆不会受到此次攻击的伤害
 * <p>
 * 额外说明：此事件不是女仆攻击其他生物的事件，如果你想监听女仆攻击其他生物，请使用 MaidHurtTarget 事件
 */
public class MaidAttackEvent extends LivingEvent implements ICancellableEvent {
    private final EntityMaid maid;
    private final DamageSource source;
    private final float amount;

    public MaidAttackEvent(EntityMaid maid, DamageSource source, float amount) {
        super(maid);
        this.source = source;
        this.amount = amount;
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public DamageSource getSource() {
        return source;
    }

    public float getAmount() {
        return amount;
    }
}
