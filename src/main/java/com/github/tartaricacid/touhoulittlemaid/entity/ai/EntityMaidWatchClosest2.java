package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;

/**
 * @author TartaricAcid
 * @date 2020/2/3 17:58
 **/
public class EntityMaidWatchClosest2 extends EntityMaidWatchClosest {
    public EntityMaidWatchClosest2(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn) {
        super(entitylivingIn, watchTargetClass, maxDistance, chanceIn);
        this.setMutexBits(3);
    }
}
