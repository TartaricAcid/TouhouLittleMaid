package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

import static com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku.NBT.*;

public class EntityDanmaku extends EntityThrowable {
    private static final int MAX_TICKS_EXISTED = 200;
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> TICKS_EXISTED = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> DAMAGES_TERRAIN = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.BOOLEAN);

    public EntityDanmaku(World worldIn) {
        super(worldIn);
    }

    /**
     * @param worldIn   实体所处世界
     * @param throwerIn 发射实体者
     * @param type      弹幕类型
     * @param color     弹幕颜色
     */
    public EntityDanmaku(World worldIn, EntityLivingBase throwerIn, DanmakuType type, DanmakuColor color) {
        super(worldIn, throwerIn);
        this.getDataManager().set(TYPE, type.ordinal());
        this.getDataManager().set(COLOR, color.ordinal());
    }

    /**
     * @param worldIn   实体所处世界
     * @param throwerIn 发射实体者
     * @param damage    弹幕造成的伤害
     * @param gravity   弹幕的重力
     * @param type      弹幕类型
     * @param color     弹幕颜色
     */
    public EntityDanmaku(World worldIn, EntityLivingBase throwerIn, float damage, float gravity, DanmakuType type, DanmakuColor color) {
        super(worldIn, throwerIn);
        this.getDataManager().set(TYPE, type.ordinal());
        this.getDataManager().set(COLOR, color.ordinal());
        this.getDataManager().set(DAMAGE, damage);
        this.getDataManager().set(GRAVITY, gravity);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(TYPE, DanmakuType.PELLET.ordinal());
        this.getDataManager().register(COLOR, DanmakuColor.RED.ordinal());
        this.getDataManager().register(DAMAGE, 1.0f);
        this.getDataManager().register(GRAVITY, 0.01f);
        this.getDataManager().register(TICKS_EXISTED, MAX_TICKS_EXISTED);
        this.getDataManager().register(DAMAGES_TERRAIN, false);
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        if (result.typeOfHit == RayTraceResult.Type.ENTITY) {
            boolean throwerMaidHasSasimono = getThrower() instanceof EntityMaid && ((EntityMaid) getThrower()).hasSasimono();
            boolean hitMaidHasSasimono = result.entityHit instanceof EntityMaid && ((EntityMaid) result.entityHit).hasSasimono();
            if (throwerMaidHasSasimono || hitMaidHasSasimono) {
                applyHasHataSasimonoLogic(result);
            } else {
                applyNormalEntityHitLogic(result);
            }
        } else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            applyBlockHitLogic(result);
        }
    }

    private void applyHasHataSasimonoLogic(@Nonnull RayTraceResult result) {
        EntityLivingBase thrower = getThrower();
        Entity hit = result.entityHit;
        boolean throwerAndHitHasSameOwner = thrower instanceof EntityTameable && hit instanceof EntityTameable &&
                ((EntityTameable) thrower).getOwnerId() != null && ((EntityTameable) thrower).getOwnerId().equals(((EntityTameable) hit).getOwnerId());
        boolean throwerIsPlayerAndHitIsOwnerTameable = thrower instanceof EntityPlayer && hit instanceof EntityTameable
                && thrower.getUniqueID().equals(((EntityTameable) hit).getOwnerId());
        boolean throwerIsTameableAndHitIsPlayerOwner = thrower instanceof EntityTameable && hit instanceof EntityPlayer &&
                ((EntityTameable) thrower).getOwnerId() != null && ((EntityTameable) thrower).getOwnerId().equals(hit.getUniqueID());
        if (throwerAndHitHasSameOwner || throwerIsPlayerAndHitIsOwnerTameable || throwerIsTameableAndHitIsPlayerOwner) {
            this.setDead();
        } else {
            applyNormalEntityHitLogic(result);
        }
    }

    private void applyNormalEntityHitLogic(@Nonnull RayTraceResult result) {
        // 投掷者不为空，也不为自己
        if (getThrower() != null && !result.entityHit.equals(this.thrower)) {
            DamageSource source = new EntityDamageSource("magic", getThrower());
            source.setDamageBypassesArmor();
            source.setMagicDamage();
            source.setProjectile();
            result.entityHit.attackEntityFrom(source, this.getDamage());
            this.setDead();
        }
    }

    private void applyBlockHitLogic(@Nonnull RayTraceResult result) {
        // 如果碰撞体积为 null
        if (world.getBlockState(result.getBlockPos()).getCollisionBoundingBox(world, result.getBlockPos()) != null) {
            this.setDead();
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.getType() == DanmakuType.MASTER_SPARK && ticksExisted > 40) {
            masterSparkLogic();
        }
        if (this.ticksExisted > getDanmakuTicksExisted()) {
            this.setDead();
        }
        // TODO: 2019/12/12 更好的自定义路径的弹幕设计
    }

    private void masterSparkLogic() {
        if (ticksExisted % 8 == 0) {
            for (int i = 0; i < 4; i++) {
                Vec3d v = this.getPositionVector().add(new Vec3d(0, 0, i * 16 + 8)
                        .rotatePitch(this.rotationPitch * 0.01745329251f)
                        .rotateYaw(this.rotationYaw * 0.01745329251f));
                world.createExplosion(this, v.x, v.y, v.z, 3 + rand.nextInt(2), isDamagesTerrain());
            }
        }
        if (ticksExisted % 8 == 4) {
            for (int i = 0; i < 4; i++) {
                Vec3d v = this.getPositionVector().add(new Vec3d(0, 0, i * 16 + 16)
                        .rotatePitch(this.rotationPitch * 0.01745329251f)
                        .rotateYaw(this.rotationYaw * 0.01745329251f));
                world.createExplosion(this, v.x, v.y, v.z, 3 + rand.nextInt(2), isDamagesTerrain());
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey(DANMAKU_TYPE.getName())) {
            setType(DanmakuType.getType(compound.getInteger(DANMAKU_TYPE.getName())));
        }
        if (compound.hasKey(DANMAKU_COLOR.getName())) {
            setColor(DanmakuColor.getColor(compound.getInteger(DANMAKU_COLOR.getName())));
        }
        if (compound.hasKey(DANMAKU_DAMAGE.getName())) {
            setDamage(compound.getFloat(DANMAKU_DAMAGE.getName()));
        }
        if (compound.hasKey(DANMAKU_GRAVITY.getName())) {
            setGravityVelocity(compound.getFloat(DANMAKU_GRAVITY.getName()));
        }
        if (compound.hasKey(DANMAKU_TICKS_EXISTED.getName())) {
            setDanmakuTicksExisted(compound.getInteger(DANMAKU_TICKS_EXISTED.getName()));
        }
        if (compound.hasKey(DANMAKU_DAMAGE.getName())) {
            setDamagesTerrain(compound.getBoolean(DANMAKU_DAMAGE.getName()));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger(DANMAKU_TYPE.getName(), getType().ordinal());
        compound.setInteger(DANMAKU_COLOR.getName(), getColor().ordinal());
        compound.setFloat(DANMAKU_DAMAGE.getName(), getDamage());
        compound.setFloat(DANMAKU_GRAVITY.getName(), getGravityVelocity());
        compound.setInteger(DANMAKU_TICKS_EXISTED.getName(), getDanmakuTicksExisted());
        compound.setBoolean(DANMAKU_DAMAGES_TERRAIN.getName(), isDamagesTerrain());
        return compound;
    }

    //----------------------------------- 相关实体数据的获取与设置 -------------------------------------------

    @Override
    protected float getGravityVelocity() {
        return this.getDataManager().get(GRAVITY);
    }

    public void setGravityVelocity(float gravity) {
        this.dataManager.set(GRAVITY, gravity);
    }

    public DanmakuType getType() {
        return DanmakuType.getType(this.getDataManager().get(TYPE));
    }

    public void setType(DanmakuType type) {
        this.dataManager.set(TYPE, type.ordinal());
    }

    public DanmakuColor getColor() {
        return DanmakuColor.getColor(this.getDataManager().get(COLOR));
    }

    public void setColor(DanmakuColor color) {
        this.dataManager.set(COLOR, color.ordinal());
    }

    public float getDamage() {
        return this.getDataManager().get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.dataManager.set(DAMAGE, damage);
    }

    public int getDanmakuTicksExisted() {
        return this.dataManager.get(TICKS_EXISTED);
    }

    public void setDanmakuTicksExisted(int ticksExisted) {
        this.dataManager.set(TICKS_EXISTED, ticksExisted);
    }

    public boolean isDamagesTerrain() {
        return this.dataManager.get(DAMAGES_TERRAIN);
    }

    public void setDamagesTerrain(boolean isDamagesTerrain) {
        this.dataManager.set(DAMAGES_TERRAIN, isDamagesTerrain);
    }

    enum NBT {
        // 弹幕相关参数
        DANMAKU_TYPE("DanmakuType"),
        DANMAKU_COLOR("DanmakuColor"),
        DANMAKU_DAMAGE("DanmakuDamage"),
        DANMAKU_GRAVITY("DanmakuGravity"),
        DANMAKU_TICKS_EXISTED("DanmakuTicksExisted"),
        DANMAKU_DAMAGES_TERRAIN("DanmakuDamagesTerrain");

        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
