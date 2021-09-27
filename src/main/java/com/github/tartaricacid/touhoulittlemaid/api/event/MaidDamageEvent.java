package com.github.tartaricacid.touhoulittlemaid.api.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class MaidDamageEvent extends LivingDamageEvent {
    private final EntityMaid maid;

    public MaidDamageEvent(EntityMaid maid, DamageSource source, float amount) {
        super(maid, source, amount);
        this.maid = maid;
    }

    public EntityMaid getMaid() {
        return maid;
    }
}
