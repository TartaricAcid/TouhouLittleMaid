package com.github.tartaricacid.touhoulittlemaid.entity.monster;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityFairyAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuShoot;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/8/30 14:38
 **/
public class EntityFairy extends AbstractEntityTouhouMob implements IRangedAttackMob, EntityFlying {
    public EntityFairy(World worldIn) {
        super(worldIn);
        setSize(0.6f, 1.5f);
        this.moveHelper = new EntityFlyHelper(this);
    }

    @Override
    public int getPowerPoint() {
        return 32;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityFairyAttack(this, 6.0d, 1.0));
        this.tasks.addTask(2, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(3, new EntityAIWanderAvoidWater(this, 1.0D));
        this.tasks.addTask(4, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(5, new EntityAIWatchClosest(this, EntityMaid.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityMaid.class, true));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.FLYING_SPEED);
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.5);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(1.0);
        this.getEntityAttribute(SharedMonsterAttributes.FLYING_SPEED).setBaseValue(0.4);
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    protected void updateFallState(double y, boolean onGroundIn, @Nonnull IBlockState state, @Nonnull BlockPos pos) {
    }

    @Nonnull
    @Override
    protected PathNavigate createNavigator(@Nonnull World worldIn) {
        PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.setCanOpenDoors(false);
        pathnavigateflying.setCanFloat(true);
        pathnavigateflying.setCanEnterDoors(true);
        return pathnavigateflying;
    }

    @Override
    public void attackEntityWithRangedAttack(@Nonnull EntityLivingBase target, float distanceFactor) {
        if (this.rand.nextFloat() <= 0.9f) {
            DanmakuShoot.aimedShot(world, this, target, distanceFactor + 1, 0,
                    0.2f * (distanceFactor + 1), 0.2f, DanmakuType.getType(rand.nextInt(2)),
                    DanmakuColor.getColor(rand.nextInt(7)));
        } else {
            DanmakuShoot.fanShapedShot(world, this, target, distanceFactor + 1.5f, 0,
                    0.2f * (distanceFactor + 1), 0.2f, DanmakuType.getType(rand.nextInt(2)),
                    DanmakuColor.getColor(rand.nextInt(7)), Math.PI / 6, 3);
        }
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }
}
