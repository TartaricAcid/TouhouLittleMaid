package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * 女仆受到攻击时的事件，此时已经完成了所有的伤害前计算判断，等同于 LivingDamageEvent
 * <p>
 * 各个伤害执行顺序：
 * MaidAttackEvent -> MaidHurtEvent -> MaidDamageEvent -> 最终受到伤害
 * <p>
 * 此事件是可取消的，取消后女仆不会受到此次攻击的伤害
 */
@Cancelable
public class MaidDamageEvent extends LivingEvent {
    private final EntityMaid maid;
    private final DamageSource source;
    private float amount;

    public MaidDamageEvent(EntityMaid maid, DamageSource source, float amount) {
        super(maid);
        this.maid = maid;
        this.source = source;
        this.amount = amount;
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

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
