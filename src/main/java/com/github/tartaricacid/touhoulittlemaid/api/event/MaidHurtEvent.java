package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class MaidHurtEvent extends LivingHurtEvent {
    private final EntityMaid maid;

    public MaidHurtEvent(EntityMaid maid, DamageSource source, float amount) {
        super(maid, source, amount);
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
