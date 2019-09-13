package com.github.tartaricacid.touhoulittlemaid.entity.monster;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.EntityFairyAttack;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuShoot;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateFlying;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2019/8/30 14:38
 **/
public class EntityFairy extends AbstractEntityTouhouMob implements IRangedAttackMob, EntityFlying {
    private static final String FAIRY_TYPE_TAG_NAME = "FairyType";
    private static final DataParameter<Integer> FAIRY_TYPE = EntityDataManager.createKey(EntityFairy.class, DataSerializers.VARINT);

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
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(FAIRY_TYPE, TYPE.BLACK_MAID_FAIRY.ordinal());
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

    @Nullable
    @Override
    public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, @Nullable IEntityLivingData livingData) {
        this.setFairyTypeOrdinal(rand.nextInt(3));
        return super.onInitialSpawn(difficulty, livingData);
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger(FAIRY_TYPE_TAG_NAME, getFairyTypeOrdinal());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey(FAIRY_TYPE_TAG_NAME)) {
            setFairyTypeOrdinal(compound.getInteger(FAIRY_TYPE_TAG_NAME));
        }
    }

    @Override
    public void setSwingingArms(boolean swingingArms) {
    }

    public int getFairyTypeOrdinal() {
        return this.dataManager.get(FAIRY_TYPE);
    }

    public void setFairyTypeOrdinal(int ordinal) {
        this.dataManager.set(FAIRY_TYPE, ordinal);
    }

    enum TYPE {
        // 女仆妖精
        BLACK_MAID_FAIRY,
        ORANGE_MAID_FAIRY,
        WHITE_MAID_FAIRY
    }
}
