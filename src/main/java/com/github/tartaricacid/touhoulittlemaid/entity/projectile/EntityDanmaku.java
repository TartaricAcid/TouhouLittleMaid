package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class EntityDanmaku extends ThrowableProjectile {
    public static final EntityType<EntityDanmaku> TYPE = EntityType.Builder.<EntityDanmaku>of(EntityDanmaku::new, MobCategory.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(6).updateInterval(10).noSave().build("danmaku");

    private static final int MAX_TICKS_EXISTED = 200;
    private static final EntityDataAccessor<Integer> DANMAKU_TYPE = SynchedEntityData.defineId(EntityDanmaku.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(EntityDanmaku.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(EntityDanmaku.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> GRAVITY = SynchedEntityData.defineId(EntityDanmaku.class, EntityDataSerializers.FLOAT);

    private int impedingLevel = 0;
    private boolean hurtEnderman = false;

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
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(DANMAKU_TYPE, DanmakuType.PELLET.ordinal());
        builder.define(COLOR, DanmakuColor.RED.ordinal());
        builder.define(DAMAGE, 1.0f);
        builder.define(GRAVITY, 0.01f);
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
            ResourceLocation registryName = BuiltInRegistries.ENTITY_TYPE.getKey(hit.getType());
            if (registryName != null && MaidConfig.MAID_RANGED_ATTACK_IGNORE.get().contains(registryName.toString())) {
                this.discard();
                return;
            }
        }

        if (thrower != null && !hit.is(thrower)) {
            DamageSource source;
            if (this.hurtEnderman) {
                source = this.damageSources().indirectMagic(this, thrower);
            } else {
                source = this.damageSources().thrown(this, thrower);
            }
            hit.hurt(source, this.getDamage());
            if (this.impedingLevel > 0 && hit instanceof LivingEntity livingEntity) {
                int duration = (20 + this.impedingLevel * 10) * 20;
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, duration, this.impedingLevel));
            }
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
    public boolean isInWater() {
        return super.isInWater();
    }

    @Override
    public double getGravity() {
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

    public EntityDanmaku setImpedingLevel(int impedingLevel) {
        this.impedingLevel = impedingLevel;
        return this;
    }

    public EntityDanmaku setHurtEnderman(boolean hurtEnderman) {
        this.hurtEnderman = hurtEnderman;
        return this;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity pEntity) {
        return new ClientboundAddEntityPacket(this, pEntity);
    }
}
