package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityDanmaku extends ThrowableProjectile {
    public static final EntityType<EntityDanmaku> TYPE = EntityType.Builder.<EntityDanmaku>of(EntityDanmaku::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(10).build("danmaku");

    private static final int MAX_TICKS_EXISTED = 200;
    private static final EntityDataAccessor<Integer> DANMAKU_TYPE = SynchedEntityData.defineId(EntityDanmaku.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntityDanmaku.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(EntityDanmaku.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> GRAVITY = SynchedEntityData.defineId(EntityDanmaku.class, EntityDataSerializers.FLOAT);

    private static final String DANMAKU_TYPE_TAG = "DanmakuType";
    private static final String DANMAKU_COLOR_TAG = "DanmakuColor";
    private static final String DANMAKU_DAMAGE_TAG = "DanmakuDamage";
    private static final String DANMAKU_GRAVITY_TAG = "DanmakuGravity";

    public EntityDanmaku(EntityType<? extends ThrowableProjectile> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityDanmaku(Level worldIn, LivingEntity throwerIn) {
        super(TYPE, throwerIn, worldIn);
    }

    public EntityDanmaku(Level worldIn, double x, double y, double z) {
        super(TYPE, x, y, z, worldIn);
    }

    private static boolean hasSameOwner(TamableAnimal tameableA, TamableAnimal tameableB) {
        if (tameableA.getOwnerUUID() == null) {
            return false;
        }
        return tameableA.getOwnerUUID().equals(tameableB.getOwnerUUID());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DANMAKU_TYPE, DanmakuType.PELLET.ordinal());
        this.entityData.define(COLOR, DanmakuColor.RED.ordinal());
        this.entityData.define(DAMAGE, 1.0f);
        this.entityData.define(GRAVITY, 0.01f);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        // 如果碰撞体积为 null
        BlockPos pos = result.getBlockPos();
        BlockState blockState = level.getBlockState(pos);
        if (!blockState.getCollisionShape(level, pos).isEmpty()) {
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity thrower = getOwner();
        Entity hit = result.getEntity();
        if (thrower instanceof TamableAnimal) {
            TamableAnimal tameable = (TamableAnimal) thrower;
            if (hit instanceof TamableAnimal && hasSameOwner(tameable, (TamableAnimal) hit)) {
                this.discard();
                return;
            }
            if (hit instanceof LivingEntity && tameable.isOwnedBy((LivingEntity) hit)) {
                this.discard();
                return;
            }
            ResourceLocation registryName = ForgeRegistries.ENTITY_TYPES.getKey(hit.getType());
            if (registryName != null && MaidConfig.MAID_RANGED_ATTACK_IGNORE.get().contains(registryName.toString())) {
                this.discard();
                return;
            }
        }

        if (thrower != null && !hit.is(thrower)) {
            DamageSource source = this.damageSources().thrown(this, thrower);
            hit.hurt(source, this.getDamage());
            this.discard();
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.tickCount > MAX_TICKS_EXISTED) {
            this.discard();
        }
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(DANMAKU_TYPE_TAG, Tag.TAG_INT)) {
            setDanmakuType(DanmakuType.getType(compound.getInt(DANMAKU_TYPE_TAG)));
        }
        if (compound.contains(DANMAKU_COLOR_TAG, Tag.TAG_INT)) {
            setColor(DanmakuColor.getColor(compound.getInt(DANMAKU_COLOR_TAG)));
        }
        if (compound.contains(DANMAKU_DAMAGE_TAG, Tag.TAG_FLOAT)) {
            setDamage(compound.getFloat(DANMAKU_DAMAGE_TAG));
        }
        if (compound.contains(DANMAKU_GRAVITY_TAG, Tag.TAG_FLOAT)) {
            setGravityVelocity(compound.getFloat(DANMAKU_GRAVITY_TAG));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
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
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
