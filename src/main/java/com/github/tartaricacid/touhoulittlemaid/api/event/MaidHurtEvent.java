package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

/**
 * 女仆受到攻击时的事件，此时已经进行了敌我类型判断，但是还没有进行护甲、药水效果吸收后的伤害计算，等同于 LivingHurtEvent
 * <p>
 * 各个伤害执行顺序：
 * MaidAttackEvent -> MaidHurtEvent -> MaidDamageEvent -> 最终受到伤害
 * <p>
 * 此事件是可取消的，取消后女仆不会受到此次攻击的伤害
 */
public class MaidHurtEvent extends LivingEvent implements ICancellableEvent {
    private final EntityMaid maid;
    private final DamageSource source;
    private float amount;

    public MaidHurtEvent(EntityMaid maid, DamageSource source, float amount) {
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
