package com.github.tartaricacid.touhoulittlemaid.entity.ai.goal;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;

import java.util.EnumSet;
import java.util.Objects;

public class MaidTemptGoal extends Goal {
    private static final EntityPredicate TEMP_TARGETING = (new EntityPredicate()).range(10.0D).allowInvulnerable().allowSameTeam().allowNonAttackable().allowUnseeable();
    protected final CreatureEntity mob;
    private final double speedModifier;
    private final Ingredient items;
    private final boolean canScare;
    protected EntityMaid maid;
    private double px;
    private double py;
    private double pz;
    private double pRotX;
    private double pRotY;
    private int calmDown;
    private boolean isRunning;

    public MaidTemptGoal(CreatureEntity creature, double speedModifier, Ingredient ingredient, boolean canScare) {
        this(creature, speedModifier, canScare, ingredient);
    }

    public MaidTemptGoal(CreatureEntity creature, double speedModifier, boolean canScare, Ingredient ingredient) {
        this.mob = creature;
        this.speedModifier = speedModifier;
        this.items = ingredient;
        this.canScare = canScare;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if (!(creature.getNavigation() instanceof GroundPathNavigator) && !(creature.getNavigation() instanceof FlyingPathNavigator)) {
            throw new IllegalArgumentException("Unsupported mob type for TemptGoal: " + Objects.requireNonNull(creature.getType().getRegistryName()));
        }
    }

    public static boolean checkNavigation(CreatureEntity creature) {
        return creature.getNavigation() instanceof GroundPathNavigator || creature.getNavigation() instanceof FlyingPathNavigator;
    }

    public boolean canUse() {
        if (this.calmDown > 0) {
            --this.calmDown;
            return false;
        } else {
            this.maid = this.mob.level.getNearestEntity(EntityMaid.class, TEMP_TARGETING, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(10));
            if (this.maid == null) {
                return false;
            } else {
                return this.shouldFollowItem(this.maid.getMainHandItem()) || this.shouldFollowItem(this.maid.getOffhandItem());
            }
        }
    }

    protected boolean shouldFollowItem(ItemStack itemStack) {
        return this.items.test(itemStack);
    }

    public boolean canContinueToUse() {
        if (this.canScare()) {
            if (this.mob.distanceToSqr(this.maid) < 36) {
                if (this.maid.distanceToSqr(this.px, this.py, this.pz) > 0.01) {
                    return false;
                }
                if (Math.abs((double) this.maid.xRot - this.pRotX) > 5.0D || Math.abs((double) this.maid.yRot - this.pRotY) > 5) {
                    return false;
                }
            } else {
                this.px = this.maid.getX();
                this.py = this.maid.getY();
                this.pz = this.maid.getZ();
            }
            this.pRotX = this.maid.xRot;
            this.pRotY = this.maid.yRot;
        }
        return this.canUse();
    }

    protected boolean canScare() {
        return this.canScare;
    }

    public void start() {
        this.px = this.maid.getX();
        this.py = this.maid.getY();
        this.pz = this.maid.getZ();
        this.isRunning = true;
    }

    public void stop() {
        this.maid = null;
        this.mob.getNavigation().stop();
        this.calmDown = 100;
        this.isRunning = false;
    }

    public void tick() {
        this.mob.getLookControl().setLookAt(this.maid, (float) (this.mob.getMaxHeadYRot() + 20), (float) this.mob.getMaxHeadXRot());
        if (this.mob.distanceToSqr(this.maid) < 6.25D) {
            this.mob.getNavigation().stop();
        } else {
            this.mob.getNavigation().moveTo(this.maid, this.speedModifier);
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }
}
