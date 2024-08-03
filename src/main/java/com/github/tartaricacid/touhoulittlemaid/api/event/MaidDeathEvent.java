package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class MaidDeathEvent extends LivingEvent implements ICancellableEvent {
    private final EntityMaid maid;
    private final DamageSource source;

    public MaidDeathEvent(EntityMaid maid, DamageSource source) {
        super(maid);
        this.maid = maid;
        this.source = source;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public DamageSource getSource() {
        return source;
    }
}
