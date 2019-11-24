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
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.script.Bindings;
import javax.script.ScriptException;

import static com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku.NBT.*;
import static com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy.NASHORN;

public class EntityDanmaku extends EntityThrowable {
    private static final int MAX_TICKS_EXISTED = 200;
    private static final DataParameter<Integer> TYPE = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> COLOR = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.VARINT);
    private static final DataParameter<Float> DAMAGE = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> GRAVITY = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.FLOAT);
    private static final DataParameter<String> X_FUNCTION = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.STRING);
    private static final DataParameter<String> Y_FUNCTION = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.STRING);
    private static final DataParameter<String> Z_FUNCTION = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.STRING);
    private static final DataParameter<Float> ORIGIN_POS_X = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> ORIGIN_POS_Y = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> ORIGIN_POS_Z = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> DANMAKU_YAW = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> DANMAKU_TICKS_EXISTED = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.VARINT);
    private Bindings bindings = NASHORN.createBindings();

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
        this.getDataManager().register(X_FUNCTION, "");
        this.getDataManager().register(Y_FUNCTION, "");
        this.getDataManager().register(Z_FUNCTION, "");
        this.getDataManager().register(ORIGIN_POS_X, 0.0f);
        this.getDataManager().register(ORIGIN_POS_Y, 0.0f);
        this.getDataManager().register(ORIGIN_POS_Z, 0.0f);
        this.getDataManager().register(DANMAKU_YAW, 0.0f);
        this.getDataManager().register(DANMAKU_TICKS_EXISTED, MAX_TICKS_EXISTED);
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
            result.entityHit.attackEntityFrom(new EntityDamageSource("arrow", getThrower()), this.getDamage());
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
        boolean xyzFunctionIsEmpty = getXFunction().isEmpty() && getYFunction().isEmpty() && getZFunction().isEmpty();
        if (!xyzFunctionIsEmpty) {
            try {
                applyCustomSpellCardLogic();
            } catch (ScriptException e) {
                this.setDead();
            }
        }

        if (this.ticksExisted > getDanmakuTicksExisted()) {
            this.setDead();
        }
    }

    private void applyCustomSpellCardLogic() throws ScriptException {
        bindings.put("t", this.ticksExisted);
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        if (!getXFunction().isEmpty()) {
            x = Double.parseDouble(NASHORN.eval(getXFunction(), bindings).toString());
        }
        if (!getYFunction().isEmpty()) {
            y = Double.parseDouble(NASHORN.eval(getYFunction(), bindings).toString());
        }
        if (!getZFunction().isEmpty()) {
            z = Double.parseDouble(NASHORN.eval(getZFunction(), bindings).toString());
        }
        Vec3d vec3d = (new Vec3d(x, y, z))
                .rotateYaw(this.getDanmakuYaw() * -0.01745329251f)
                .add(getOriginPos().x, getOriginPos().y, getOriginPos().z);
        this.setPosition(vec3d.x, vec3d.y, vec3d.z);
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
        if (compound.hasKey(DANMAKU_X_FUNCTION.getName())) {
            setXFunction(compound.getString(DANMAKU_X_FUNCTION.getName()));
        }
        if (compound.hasKey(DANMAKU_Y_FUNCTION.getName())) {
            setYFunction(compound.getString(DANMAKU_Y_FUNCTION.getName()));
        }
        if (compound.hasKey(DANMAKU_Z_FUNCTION.getName())) {
            setZFunction(compound.getString(DANMAKU_Z_FUNCTION.getName()));
        }
        if (compound.hasKey(DANMAKU_ORIGIN_POS_X.getName())
                && compound.hasKey(DANMAKU_ORIGIN_POS_Y.getName())
                && compound.hasKey(DANMAKU_ORIGIN_POS_Z.getName())) {
            setOriginPos(new Vec3d(compound.getFloat(DANMAKU_ORIGIN_POS_X.getName()),
                    compound.getFloat(DANMAKU_ORIGIN_POS_Y.getName()),
                    compound.getFloat(DANMAKU_ORIGIN_POS_Z.getName())));

        }
        if (compound.hasKey(NBT.DANMAKU_YAW.getName())) {
            setDanmakuYaw(compound.getFloat(NBT.DANMAKU_YAW.getName()));
        }
        if (compound.hasKey(NBT.DANMAKU_TICKS_EXISTED.getName())) {
            setDanmakuTicksExisted(compound.getInteger(NBT.DANMAKU_TICKS_EXISTED.getName()));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger(DANMAKU_TYPE.getName(), getType().ordinal());
        compound.setInteger(DANMAKU_COLOR.getName(), getColor().ordinal());
        compound.setFloat(DANMAKU_DAMAGE.getName(), getDamage());
        compound.setFloat(DANMAKU_GRAVITY.getName(), getGravityVelocity());
        compound.setString(DANMAKU_X_FUNCTION.getName(), getXFunction());
        compound.setString(DANMAKU_Y_FUNCTION.getName(), getYFunction());
        compound.setString(DANMAKU_Z_FUNCTION.getName(), getZFunction());
        compound.setFloat(DANMAKU_ORIGIN_POS_X.getName(), (float) getOriginPos().x);
        compound.setFloat(DANMAKU_ORIGIN_POS_Y.getName(), (float) getOriginPos().y);
        compound.setFloat(DANMAKU_ORIGIN_POS_Z.getName(), (float) getOriginPos().z);
        compound.setFloat(NBT.DANMAKU_YAW.getName(), getDanmakuYaw());
        compound.setInteger(NBT.DANMAKU_TICKS_EXISTED.getName(), getDanmakuTicksExisted());
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

    public String getXFunction() {
        return this.getDataManager().get(X_FUNCTION);
    }

    public void setXFunction(String xFunction) {
        this.dataManager.set(X_FUNCTION, xFunction);
    }


    public String getYFunction() {
        return this.getDataManager().get(Y_FUNCTION);
    }

    public void setYFunction(String yFunction) {
        this.dataManager.set(Y_FUNCTION, yFunction);
    }


    public String getZFunction() {
        return this.getDataManager().get(Z_FUNCTION);
    }

    public void setZFunction(String zFunction) {
        this.dataManager.set(Z_FUNCTION, zFunction);
    }

    public Vec3d getOriginPos() {
        return new Vec3d(this.dataManager.get(ORIGIN_POS_X), this.dataManager.get(ORIGIN_POS_Y), this.dataManager.get(ORIGIN_POS_Z));
    }

    public void setOriginPos(Vec3d pos) {
        this.dataManager.set(ORIGIN_POS_X, (float) pos.x);
        this.dataManager.set(ORIGIN_POS_Y, (float) pos.y);
        this.dataManager.set(ORIGIN_POS_Z, (float) pos.z);
    }

    public float getDanmakuYaw() {
        return this.dataManager.get(DANMAKU_YAW);
    }

    public void setDanmakuYaw(float yaw) {
        this.dataManager.set(DANMAKU_YAW, yaw);
    }

    public int getDanmakuTicksExisted() {
        return this.dataManager.get(DANMAKU_TICKS_EXISTED);
    }

    public void setDanmakuTicksExisted(int ticksExisted) {
        this.dataManager.set(DANMAKU_TICKS_EXISTED, ticksExisted);
    }

    enum NBT {
        // 弹幕相关参数
        DANMAKU_TYPE("DanmakuType"),
        DANMAKU_COLOR("DanmakuColor"),
        DANMAKU_DAMAGE("DanmakuDamage"),
        DANMAKU_GRAVITY("DanmakuGravity"),
        DANMAKU_X_FUNCTION("DanmakuXFunction"),
        DANMAKU_Y_FUNCTION("DanmakuYFunction"),
        DANMAKU_Z_FUNCTION("DanmakuZFunction"),
        DANMAKU_ORIGIN_POS_X("DanmakuOriginPosX"),
        DANMAKU_ORIGIN_POS_Y("DanmakuOriginPosY"),
        DANMAKU_ORIGIN_POS_Z("DanmakuOriginPosZ"),
        DANMAKU_YAW("DanmakuYaw"),
        DANMAKU_TICKS_EXISTED("DanmakuTicksExisted");


        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
