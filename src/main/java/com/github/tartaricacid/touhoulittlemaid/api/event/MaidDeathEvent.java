package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class MaidDeathEvent extends LivingEvent {
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
