package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import com.github.tartaricacid.touhoulittlemaid.network.entity.SSpawnDanmakuPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class EntityDanmaku extends ThrowableEntity {
    public static final EntityType<EntityDanmaku> TYPE = EntityType.Builder.<EntityDanmaku>of(EntityDanmaku::new, EntityClassification.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(10).build("danmaku");

    private static final int MAX_TICKS_EXISTED = 200;
    private static final DataParameter<Integer> DANMAKU_TYPE = EntityDataManager.defineId(EntityDanmaku.class, DataSerializers.INT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.defineId(EntityDanmaku.class, DataSerializers.INT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.defineId(EntityDanmaku.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> GRAVITY = EntityDataManager.defineId(EntityDanmaku.class, DataSerializers.FLOAT);

    private static final String DANMAKU_TYPE_TAG = "DanmakuType";
    private static final String DANMAKU_COLOR_TAG = "DanmakuColor";
    private static final String DANMAKU_DAMAGE_TAG = "DanmakuDamage";
    private static final String DANMAKU_GRAVITY_TAG = "DanmakuGravity";

    public EntityDanmaku(EntityType<? extends ThrowableEntity> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityDanmaku(World worldIn, LivingEntity throwerIn) {
        super(TYPE, throwerIn, worldIn);
    }

    public EntityDanmaku(World worldIn, double x, double y, double z) {
        super(TYPE, x, y, z, worldIn);
    }

    private static boolean hasSameOwner(TameableEntity tameableA, TameableEntity tameableB) {
        if (tameableA.getOwnerUUID() == null) {
            return false;
        }
        if (tameableB.getOwnerUUID() == null) {
            return false;
        }
        return tameableA.getOwnerUUID() == tameableB.getOwnerUUID();
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DANMAKU_TYPE, DanmakuType.PELLET.ordinal());
        this.entityData.define(COLOR, DanmakuColor.RED.ordinal());
        this.entityData.define(DAMAGE, 1.0f);
        this.entityData.define(GRAVITY, 0.01f);
    }

    @Override
    protected void onHitBlock(BlockRayTraceResult result) {
        // 如果碰撞体积为 null
        if (level.getBlockState(result.getBlockPos()).getCollisionShape(level, result.getBlockPos()).isEmpty()) {
            this.remove();
        }
    }

    @Override
    protected void onHitEntity(EntityRayTraceResult result) {
        Entity thrower = getOwner();
        Entity hit = result.getEntity();
        if (thrower instanceof TameableEntity) {
            TameableEntity tameable = (TameableEntity) thrower;
            if (hit instanceof TameableEntity && hasSameOwner(tameable, (TameableEntity) hit)) {
                this.remove();
                return;
            }
            if (hit instanceof LivingEntity && tameable.isOwnedBy((LivingEntity) hit)) {
                this.remove();
                return;
            }
        }

        if (thrower != null && !hit.is(thrower)) {
            DamageSource source = new EntityDamageSourceDanmaku(this, thrower);
            hit.hurt(source, this.getDamage());
            this.remove();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > MAX_TICKS_EXISTED) {
            this.remove();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(DANMAKU_TYPE_TAG, Constants.NBT.TAG_INT)) {
            setDanmakuType(DanmakuType.getType(compound.getInt(DANMAKU_TYPE_TAG)));
        }
        if (compound.contains(DANMAKU_COLOR_TAG, Constants.NBT.TAG_INT)) {
            setColor(DanmakuColor.getColor(compound.getInt(DANMAKU_COLOR_TAG)));
        }
        if (compound.contains(DANMAKU_DAMAGE_TAG, Constants.NBT.TAG_FLOAT)) {
            setDamage(compound.getFloat(DANMAKU_DAMAGE_TAG));
        }
        if (compound.contains(DANMAKU_GRAVITY_TAG, Constants.NBT.TAG_FLOAT)) {
            setGravityVelocity(compound.getFloat(DANMAKU_GRAVITY_TAG));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt(DANMAKU_TYPE_TAG, getDanmakuType().ordinal());
        compound.putInt(DANMAKU_COLOR_TAG, getColor().ordinal());
        compound.putFloat(DANMAKU_DAMAGE_TAG, getDamage());
        compound.putFloat(DANMAKU_GRAVITY_TAG, getGravity());
    }

    @Override
    public boolean isInWater() {
        return super.isInWater();
    }

    @Override
    protected float getGravity() {
        return this.entityData.get(GRAVITY);
    }

    public EntityDanmaku setGravityVelocity(float gravity) {
        this.entityData.set(GRAVITY, gravity);
        return this;
    }

    public DanmakuType getDanmakuType() {
        return DanmakuType.getType(this.entityData.get(DANMAKU_TYPE));
    }

    public EntityDanmaku setDanmakuType(DanmakuType type) {
        this.entityData.set(DANMAKU_TYPE, type.ordinal());
        return this;
    }

    public DanmakuColor getColor() {
        return DanmakuColor.getColor(this.entityData.get(COLOR));
    }

    public EntityDanmaku setColor(DanmakuColor color) {
        this.entityData.set(COLOR, color.ordinal());
        return this;
    }

    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }

    public EntityDanmaku setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
        return this;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return new SSpawnDanmakuPacket(this);
    }
}
