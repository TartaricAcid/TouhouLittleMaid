package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityDanmaku extends EntityThrowable {
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.FLOAT);

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
        this.getDataManager().set(TYPE, type.getIndex());
        this.getDataManager().set(COLOR, color.getIndex());
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
        this.getDataManager().set(TYPE, type.getIndex());
        this.getDataManager().set(COLOR, color.getIndex());
        this.getDataManager().set(DAMAGE, damage);
        this.getDataManager().set(GRAVITY, gravity);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.getDataManager().register(TYPE, DanmakuType.PELLET.getIndex());
        this.getDataManager().register(COLOR, DanmakuColor.RED.getIndex());
        this.getDataManager().register(DAMAGE, 1.0f);
        this.getDataManager().register(GRAVITY, 0.01f);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
        // 如果碰撞对象为实体，而且投掷者不为空，也不为自己
        if (result.typeOfHit == RayTraceResult.Type.ENTITY && getThrower() != null && !result.entityHit.equals(this.thrower)) {
            result.entityHit.attackEntityFrom(new EntityDamageSource("arrow", getThrower()), this.getDamage());
            this.setDead();
        } else if (result.typeOfHit == RayTraceResult.Type.BLOCK) {
            // 如果碰撞体积为 null
            if (world.getBlockState(result.getBlockPos()).getCollisionBoundingBox(world, result.getBlockPos()) != null) {
                this.setDead();
            }
        }
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted > 200) {
            this.setDead();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (compound.hasKey("DanmakuType")) {
            setType(DanmakuType.getType(compound.getInteger("DanmakuType")));
        }
        if (compound.hasKey("DanmakuColor")) {
            setColor(DanmakuColor.getColor(compound.getInteger("DanmakuColor")));
        }
        if (compound.hasKey("DanmakuDamage")) {
            setDamage(compound.getFloat("DanmakuDamage"));
        }
        if (compound.hasKey("DanmakuGravity")) {
            setGravityVelocity(compound.getFloat("DanmakuGravity"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("DanmakuType", getType().getIndex());
        compound.setInteger("DanmakuColor", getColor().getIndex());
        compound.setFloat("DanmakuDamage", getDamage());
        compound.setFloat("DanmakuGravity", getGravityVelocity());
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
        this.dataManager.set(TYPE, type.getIndex());
    }

    public DanmakuColor getColor() {
        return DanmakuColor.getColor(this.getDataManager().get(COLOR));
    }

    public void setColor(DanmakuColor color) {
        this.dataManager.set(COLOR, color.getIndex());
    }

    public float getDamage() {
        return this.getDataManager().get(DAMAGE);
    }

    public void setDamage(float damage) {
        this.dataManager.set(DAMAGE, damage);
    }
}
