package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Random;

public final class DanmakuShoot {
    private static final Random RANDOM = new Random();
    private static final double MAX_YAW = 2 * Math.PI;
    private static final int MIN_FAN_NUM = 2;

    private Level world;
    private LivingEntity thrower;
    private DanmakuColor color;
    private DanmakuType type;
    private float gravity;
    private float damage;
    private LivingEntity target;
    private float velocity;
    private float inaccuracy;
    private double yawTotal;
    private int fanNum;

    public static DanmakuShoot create() {
        return new DanmakuShoot();
    }

    public void aimedShot() {
        if (!world.isClientSide) {
            EntityDanmaku danmaku = new EntityDanmaku(world, thrower)
                    .setDamage(damage).setGravityVelocity(gravity)
                    .setDanmakuType(type).setColor(color);
            float offset = 0.3f / target.getBbHeight();
            danmaku.shoot(target.getX() - thrower.getX(), target.getY() - thrower.getY() - offset, target.getZ() - thrower.getZ(), velocity, inaccuracy);
            world.addFreshEntity(danmaku);
        }
        world.playSound(null, thrower.getX(), thrower.getY(), thrower.getZ(), SoundEvents.SNOWBALL_THROW, thrower.getSoundSource(), 1.0f, 0.8f);
    }

    public void fanShapedShot() {
        if (yawTotal < 0 || yawTotal > MAX_YAW || fanNum < MIN_FAN_NUM) {
            return;
        }

        if (!world.isClientSide) {
            float offset = 0.3f / target.getBbHeight();
            Vec3 v = new Vec3(target.getX() - thrower.getX(), target.getY() - thrower.getY() - offset, target.getZ() - thrower.getZ());
            double yaw = -(yawTotal / 2);
            double addYaw = yawTotal / (fanNum - 1);
            for (int i = 1; i <= fanNum; i++) {
                Vec3 v1 = v.yRot((float) yaw);
                yaw = yaw + addYaw;

                EntityDanmaku danmaku = new EntityDanmaku(world, thrower)
                        .setDamage(damage).setGravityVelocity(gravity)
                        .setDanmakuType(type).setColor(color);
                danmaku.shoot(v1.x, v1.y, v1.z, velocity, inaccuracy);
                world.addFreshEntity(danmaku);
            }
        }
        world.playSound(null, thrower.getX(), thrower.getY(), thrower.getZ(), SoundEvents.SNOWBALL_THROW, thrower.getSoundSource(), 1.0f, 0.8f);
    }

    public DanmakuShoot setWorld(Level world) {
        this.world = world;
        return this;
    }

    public DanmakuShoot setThrower(LivingEntity thrower) {
        this.thrower = thrower;
        return this;
    }

    public DanmakuShoot setColor(DanmakuColor color) {
        this.color = color;
        return this;
    }

    public DanmakuShoot setRandomColor() {
        this.color = DanmakuColor.random(RANDOM);
        return this;
    }

    public DanmakuShoot setType(DanmakuType type) {
        this.type = type;
        return this;
    }

    public DanmakuShoot setRandomType() {
        this.type = DanmakuType.random(RANDOM);
        return this;
    }

    public DanmakuShoot setGravity(float gravity) {
        this.gravity = gravity;
        return this;
    }

    public DanmakuShoot setDamage(float damage) {
        this.damage = damage;
        return this;
    }

    public DanmakuShoot setTarget(LivingEntity target) {
        this.target = target;
        return this;
    }

    public DanmakuShoot setVelocity(float velocity) {
        this.velocity = velocity;
        return this;
    }

    public DanmakuShoot setInaccuracy(float inaccuracy) {
        this.inaccuracy = inaccuracy;
        return this;
    }

    public DanmakuShoot setYawTotal(double yawTotal) {
        this.yawTotal = yawTotal;
        return this;
    }

    public DanmakuShoot setFanNum(int fanNum) {
        this.fanNum = fanNum;
        return this;
    }
}
