package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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
    private static final DataParameter<BlockPos> ORIGIN_POS = EntityDataManager.createKey(EntityDanmaku.class, DataSerializers.BLOCK_POS);
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
        this.getDataManager().register(X_FUNCTION, "");
        this.getDataManager().register(Y_FUNCTION, "");
        this.getDataManager().register(Z_FUNCTION, "");
        this.getDataManager().register(ORIGIN_POS, BlockPos.ORIGIN);

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

        try {
            bindings.put("x", this.getOriginPos().getX());
            bindings.put("y", this.getOriginPos().getY());
            bindings.put("z", this.getOriginPos().getZ());
            bindings.put("t", this.ticksExisted);

            if (!getXFunction().isEmpty()) {
                this.posX = Double.parseDouble(NASHORN.eval(getXFunction(), bindings).toString());
            }
            if (!getYFunction().isEmpty()) {
                this.posY = Double.parseDouble(NASHORN.eval(getYFunction(), bindings).toString());
            }
            if (!getZFunction().isEmpty()) {
                this.posZ = Double.parseDouble(NASHORN.eval(getZFunction(), bindings).toString());
            }
        } catch (ScriptException e) {
            this.setDead();
        }

        if (this.ticksExisted > MAX_TICKS_EXISTED) {
            this.setDead();
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
        if (compound.hasKey(DANMAKU_X_FUNCTION.getName())) {
            setXFunction(compound.getString(DANMAKU_X_FUNCTION.getName()));
        }
        if (compound.hasKey(DANMAKU_Y_FUNCTION.getName())) {
            setYFunction(compound.getString(DANMAKU_Y_FUNCTION.getName()));
        }
        if (compound.hasKey(DANMAKU_Z_FUNCTION.getName())) {
            setZFunction(compound.getString(DANMAKU_Z_FUNCTION.getName()));
        }
        if (compound.hasKey(DANMAKU_ORIGIN_POS.getName())) {
            setOriginPos(NBTUtil.getPosFromTag(compound.getCompoundTag(DANMAKU_ORIGIN_POS.getName())));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger(DANMAKU_TYPE.getName(), getType().getIndex());
        compound.setInteger(DANMAKU_COLOR.getName(), getColor().getIndex());
        compound.setFloat(DANMAKU_DAMAGE.getName(), getDamage());
        compound.setFloat(DANMAKU_GRAVITY.getName(), getGravityVelocity());
        compound.setString(DANMAKU_X_FUNCTION.getName(), getXFunction());
        compound.setString(DANMAKU_Y_FUNCTION.getName(), getYFunction());
        compound.setString(DANMAKU_Z_FUNCTION.getName(), getZFunction());
        compound.setTag(DANMAKU_ORIGIN_POS.getName(), NBTUtil.createPosTag(getOriginPos()));
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

    public BlockPos getOriginPos() {
        return this.dataManager.get(ORIGIN_POS);
    }

    public void setOriginPos(BlockPos pos) {
        this.dataManager.set(ORIGIN_POS, pos);
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
        DANMAKU_ORIGIN_POS("DanmakuOriginPos");


        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
