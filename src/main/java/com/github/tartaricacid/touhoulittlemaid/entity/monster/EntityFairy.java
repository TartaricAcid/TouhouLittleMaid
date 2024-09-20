package com.github.tartaricacid.touhoulittlemaid.entity.monster;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MiscConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.ai.goal.FairyAttackGoal;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.DanmakuShoot;
import com.github.tartaricacid.touhoulittlemaid.init.InitPoi;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class EntityFairy extends Monster implements RangedAttackMob, FlyingAnimal, IHasPowerPoint {
    public static final EntityType<EntityFairy> TYPE = EntityType.Builder.<EntityFairy>of(EntityFairy::new, MobCategory.MONSTER)
            .sized(0.6f, 1.5f).clientTrackingRange(10).build("fairy");

    private static final String FAIRY_TYPE_TAG_NAME = "FairyType";
    private static final EntityDataAccessor<Integer> FAIRY_TYPE = SynchedEntityData.defineId(EntityFairy.class, EntityDataSerializers.INT);
    private static final double AIMED_SHOT_PROBABILITY = 0.9;

    protected EntityFairy(EntityType<? extends Monster> type, Level worldIn) {
        super(type, worldIn);
        this.moveControl = new FlyingMoveControl(this, 15, true);
    }

    public EntityFairy(Level worldIn) {
        this(TYPE, worldIn);
    }

    public static AttributeSupplier.Builder createFairyAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 35.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.ATTACK_DAMAGE, 1.5)
                .add(Attributes.ARMOR, 1.)
                .add(Attributes.FLYING_SPEED, 0.4);
    }

    public static boolean checkFairySpawnRules(EntityType<EntityFairy> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource) {
        if (Monster.checkMonsterSpawnRules(entityType, levelAccessor, spawnType, pos, randomSource) && levelAccessor instanceof ServerLevel level) {
            int scarecrowRange = MiscConfig.SCARECROW_RANGE.get();
            long findCount = level.getPoiManager().getInSquare(type -> type.get().equals(InitPoi.SCARECROW.get()), pos, scarecrowRange, PoiManager.Occupancy.ANY).count();
            return findCount <= 0;
        }
        return false;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new FairyAttackGoal(this, 6.0, 1.0));
        goalSelector.addGoal(2, new MoveTowardsRestrictionGoal(this, 1.0));
        goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0));
        goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        goalSelector.addGoal(5, new LookAtPlayerGoal(this, EntityMaid.class, 8.0F));
        goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
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
    public boolean causeFallDamage(float distance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float distanceFactor) {
        float damageBase = 1;
        Difficulty difficulty = target.level.getDifficulty();
        switch (difficulty) {
            case NORMAL -> damageBase = 1.5f;
            case HARD -> damageBase = 2f;
        }
        if (this.random.nextFloat() <= AIMED_SHOT_PROBABILITY) {
            DanmakuShoot.create().setWorld(level).setThrower(this)
                    .setTarget(target).setRandomColor().setRandomType()
                    .setDamage(distanceFactor + damageBase).setGravity(0)
                    .setVelocity(0.2f * (distanceFactor + 1))
                    .setInaccuracy(0.2f).aimedShot();
        } else {
            DanmakuShoot.create().setWorld(level).setThrower(this)
                    .setTarget(target).setRandomColor().setRandomType()
                    .setDamage(distanceFactor + damageBase + 0.5f).setGravity(0)
                    .setVelocity(0.2f * (distanceFactor + 1))
                    .setInaccuracy(0.2f).setFanNum(3).setYawTotal(Math.PI / 6)
                    .fanShapedShot();
        }
    }

    @Override
    protected PathNavigation createNavigation(Level worldIn) {
        FlyingPathNavigation navigator = new FlyingPathNavigation(this, worldIn);
        navigator.setCanOpenDoors(false);
        navigator.setCanFloat(true);
        navigator.setCanPassDoors(true);
        return navigator;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setFairyTypeOrdinal(random.nextInt(FairyType.values().length));
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt(FAIRY_TYPE_TAG_NAME, getFairyTypeOrdinal());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(FAIRY_TYPE_TAG_NAME, Tag.TAG_INT)) {
            setFairyTypeOrdinal(compound.getInt(FAIRY_TYPE_TAG_NAME));
        }
    }

    public int getFairyTypeOrdinal() {
        return this.entityData.get(FAIRY_TYPE);
    }

    public void setFairyTypeOrdinal(int ordinal) {
        this.entityData.set(FAIRY_TYPE, ordinal);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return InitSounds.FAIRY_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return InitSounds.FAIRY_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return InitSounds.FAIRY_DEATH.get();
    }
}
