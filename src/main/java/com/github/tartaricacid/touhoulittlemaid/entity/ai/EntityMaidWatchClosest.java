package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidVehicle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author TartaricAcid
 * @date 2020/2/3 17:47
 **/
public class EntityMaidWatchClosest extends EntityAIWatchClosest {
    public EntityMaidWatchClosest(EntityLiving entityIn, Class<? extends Entity> watchTargetClass, float maxDistance) {
        super(entityIn, watchTargetClass, maxDistance);
    }

    public EntityMaidWatchClosest(EntityLiving entityIn, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn) {
        super(entityIn, watchTargetClass, maxDistance, chanceIn);
    }

    @Override
    public boolean shouldExecute() {
        boolean controlNotWatch = !(entity.getControllingPassenger() instanceof EntityMaidVehicle);
        boolean passengerNotWatch = !(entity.getRidingEntity() instanceof EntityMaidVehicle || entity.getRidingEntity() instanceof EntityPlayer);
        return super.shouldExecute() && controlNotWatch && passengerNotWatch;
    }
}
