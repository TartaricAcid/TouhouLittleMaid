package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class MaidAttackEvent extends LivingEvent {
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
