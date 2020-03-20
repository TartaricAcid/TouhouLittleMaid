package com.github.tartaricacid.touhoulittlemaid.entity.ai;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMaidVehicle;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.internal.task.TaskIdle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author TartaricAcid
 * @date 2020/2/3 17:47
 **/
public class EntityMaidWatchClosest extends EntityAIWatchClosest {
    private EntityMaid maid;

    public EntityMaidWatchClosest(EntityMaid maid, Class<? extends Entity> watchTargetClass, float maxDistance) {
        super(maid, watchTargetClass, maxDistance);
        this.maid = maid;
    }

    public EntityMaidWatchClosest(EntityMaid maid, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn) {
        super(maid, watchTargetClass, maxDistance, chanceIn);
        this.maid = maid;
    }

    @Override
    public boolean shouldExecute() {
        boolean taskIsIdle = maid.getTask() instanceof TaskIdle;
        boolean controlNotWatch = !(maid.getControllingPassenger() instanceof EntityMaidVehicle);
        boolean passengerNotWatch = !(maid.getRidingEntity() instanceof EntityMaidVehicle || maid.getRidingEntity() instanceof EntityPlayer);
        return super.shouldExecute() && taskIsIdle && controlNotWatch && passengerNotWatch;
    }
}
