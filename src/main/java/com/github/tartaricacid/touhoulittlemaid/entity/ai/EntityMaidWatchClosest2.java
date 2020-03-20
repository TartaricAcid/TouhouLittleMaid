package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;

/**
 * @author TartaricAcid
 * @date 2020/2/3 17:58
 **/
public class EntityMaidWatchClosest2 extends EntityMaidWatchClosest {
    public EntityMaidWatchClosest2(EntityMaid maid, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn) {
        super(maid, watchTargetClass, maxDistance, chanceIn);
        this.setMutexBits(3);
    }
}
