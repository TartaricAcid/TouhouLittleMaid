package com.github.tartaricacid.touhoulittlemaid.entity.ai.goal;

import com.github.tartaricacid.touhoulittlemaid.datagen.tag.EntityTypeGenerator;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class FairyNearestAttackableTargetGoal<T extends LivingEntity> extends TargetGoal {
    private static final int DEFAULT_RANDOM_INTERVAL = 5;
    private final TargetingConditions targetConditions;
    private @Nullable LivingEntity target;

    public FairyNearestAttackableTargetGoal(EntityFairy fairy) {
        super(fairy, true, false);
        this.setFlags(EnumSet.of(Goal.Flag.TARGET));
        this.targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance());
    }

    @Override
    public boolean canUse() {
        if (this.mob.getRandom().nextInt(DEFAULT_RANDOM_INTERVAL) != 0) {
            return false;
        } else {
            this.findTarget();
            return this.target != null;
        }
    }

    private AABB getTargetSearchArea(double distance) {
        return this.mob.getBoundingBox().inflate(distance, 4, distance);
    }

    private void findTarget() {
        Level level = this.mob.level;
        AABB searchArea = this.getTargetSearchArea(this.getFollowDistance());
        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, searchArea,
                e -> e.getType().is(EntityTypeGenerator.MAID_FAIRY_ATTACK_GOAL));
        this.target = level.getNearestEntity(entities, this.targetConditions, this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
    }

    @Override
    public void start() {
        this.mob.setTarget(this.target);
        super.start();
    }
}
