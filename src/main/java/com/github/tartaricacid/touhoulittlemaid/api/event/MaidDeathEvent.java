package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class MaidDeathEvent extends LivingDeathEvent {
    private final EntityMaid maid;

    public MaidDeathEvent(EntityMaid maid, DamageSource source) {
        super(maid, source);
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
