package com.github.tartaricacid.touhoulittlemaid.entity.monster;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.goal.FairyAttackGoal;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuShoot;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

public class EntityFairy extends MonsterEntity implements IRangedAttackMob, IFlyingAnimal, IHasPowerPoint {
    public static final EntityType<EntityFairy> TYPE = EntityType.Builder.<EntityFairy>of(EntityFairy::new, EntityClassification.MONSTER)
            .sized(0.6f, 1.5f).clientTrackingRange(10).build("fairy");

    private static final String FAIRY_TYPE_TAG_NAME = "FairyType";
    private static final DataParameter<Integer> FAIRY_TYPE = EntityDataManager.defineId(EntityFairy.class, DataSerializers.INT);
    private static final double AIMED_SHOT_PROBABILITY = 0.9;

    protected EntityFairy(EntityType<? extends MonsterEntity> type, World worldIn) {
        super(type, worldIn);
        this.moveControl = new FlyingMovementController(this, 15, true);
    }

    public EntityFairy(World worldIn) {
        this(TYPE, worldIn);
    }

    public static AttributeModifierMap.MutableAttribute createFairyAttributes() {
        return MonsterEntity.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 1.5)
                .add(Attributes.ARMOR, 1.)
                .add(Attributes.FLYING_SPEED, 0.4);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        // TODO：待完成
        // goalSelector.addGoal(0, new AvoidEntityGoal<>(this, EntityScarecrow.class, 10.0F, 1.0D, 1.2D));
        goalSelector.addGoal(1, new FairyAttackGoal(this, 6.0, 1.0));
        goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0));
        goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0));
        goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        goalSelector.addGoal(5, new LookAtGoal(this, EntityMaid.class, 8.0F));
        goalSelector.addGoal(6, new LookRandomlyGoal(this));

        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, EntityMaid.class, 10, true,
                false, e -> !e.isSleeping()));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FAIRY_TYPE, FairyType.BLACK.ordinal());
    }

    @Override
    public int getPowerPoint() {
        return (int) (MiscConfig.MAID_FAIRY_POWER_POINT.get() * 100);
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        dropPowerPoint(this);
    }

    @Override
    public boolean causeFallDamage(float distance, float damageMultiplier) {
        return false;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        if (this.random.nextFloat() <= AIMED_SHOT_PROBABILITY) {
            DanmakuShoot.create().setWorld(level).setThrower(this)
                    .setTarget(target).setRandomColor().setRandomType()
                    .setDamage(distanceFactor + 1).setGravity(0)
                    .setVelocity(0.2f * (distanceFactor + 1))
                    .setInaccuracy(0.2f).aimedShot();
        } else {
            DanmakuShoot.create().setWorld(level).setThrower(this)
                    .setTarget(target).setRandomColor().setRandomType()
                    .setDamage(distanceFactor + 1.5f).setGravity(0)
                    .setVelocity(0.2f * (distanceFactor + 1))
                    .setInaccuracy(0.2f).setFanNum(3).setYawTotal(Math.PI / 6)
                    .fanShapedShot();
        }
    }

    @Override
    protected PathNavigator createNavigation(World worldIn) {
        FlyingPathNavigator navigator = new FlyingPathNavigator(this, worldIn);
        navigator.setCanOpenDoors(false);
        navigator.setCanFloat(true);
        navigator.setCanPassDoors(true);
        return navigator;
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        this.setFairyTypeOrdinal(random.nextInt(FairyType.values().length));
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt(FAIRY_TYPE_TAG_NAME, getFairyTypeOrdinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(FAIRY_TYPE_TAG_NAME, Constants.NBT.TAG_INT)) {
            setFairyTypeOrdinal(compound.getInt(FAIRY_TYPE_TAG_NAME));
        }
    }

    public int getFairyTypeOrdinal() {
        return this.entityData.get(FAIRY_TYPE);
    }

    public void setFairyTypeOrdinal(int ordinal) {
        this.entityData.set(FAIRY_TYPE, ordinal);
    }
}
