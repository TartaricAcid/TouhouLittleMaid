package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.FavorabilityManager;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskBoardGames;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.brain.schedule.Activity;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

public class EntitySit extends Entity {
    public static final EntityType<EntitySit> TYPE = EntityType.Builder.<EntitySit>of(EntitySit::new, EntityClassification.MISC)
            .sized(0.5f, 0.1f).clientTrackingRange(10).build("sit");
    private static final DataParameter<String> SIT_TYPE = EntityDataManager.defineId(EntitySit.class, DataSerializers.STRING);
    private int passengerTick = 0;

    public EntitySit(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntitySit(World worldIn, Vector3d pos, String joyType) {
        this(TYPE, worldIn);
        this.setPos(pos.x, pos.y, pos.z);
        this.setJoyType(joyType);
    }

    @Override
    public double getPassengersRidingOffset() {
        return -0.25;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(SIT_TYPE, "");
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT tag) {
        if (tag.contains("SitJoyType", Constants.NBT.TAG_STRING)) {
            this.setJoyType(tag.getString("SitJoyType"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT tag) {
        if (StringUtils.isNotBlank(this.getJoyType())) {
            tag.putString("SitJoyType", this.getJoyType());
        }
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            this.checkBelowWorld();
            this.checkPassengers();
            if (this.getFirstPassenger() instanceof EntityMaid) {
                EntityMaid maid = (EntityMaid) this.getFirstPassenger();
                this.tickMaid(maid);
            }
        }
    }

    private void checkBelowWorld() {
        if (this.getY() < -64.0D) {
            this.outOfWorld();
        }
    }

    @Nullable
    public Entity getFirstPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    private void tickMaid(EntityMaid maid) {
        maid.yRot = this.yRot;
        maid.setYHeadRot(this.yRot);
        if (tickCount % 20 == 0) {
            FavorabilityManager manager = maid.getFavorabilityManager();
            manager.apply(this.getJoyType());
            if (!this.isIdleSchedule(maid) && !isGomokuTask(maid)) {
                maid.stopRiding();
            }
        }
    }

    private void checkPassengers() {
        if (this.getPassengers().isEmpty()) {
            passengerTick++;
        } else {
            passengerTick = 0;
        }
        if (passengerTick > 10) {
            this.remove();
        }
    }

    private boolean isGomokuTask(EntityMaid maid) {
        return Type.GOMOKU.getTypeName().equals(this.getJoyType()) && maid.getTask().getUid().equals(TaskBoardGames.UID);
    }

    private boolean isIdleSchedule(EntityMaid maid) {
        MaidSchedule schedule = maid.getSchedule();
        int time = (int) (maid.level.getDayTime() % 24000L);
        switch (schedule) {
            case ALL:
                return false;
            case NIGHT:
                return InitEntities.MAID_NIGHT_SHIFT_SCHEDULES.get().getActivityAt(time) == Activity.IDLE;
            default:
                return InitEntities.MAID_DAY_SHIFT_SCHEDULES.get().getActivityAt(time) == Activity.IDLE;
        }
    }

    @Override
    public boolean skipAttackInteraction(Entity pEntity) {
        return true;
    }


    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public void move(MoverType pType, Vector3d pPos) {
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public void push(double pX, double pY, double pZ) {
    }

    @Override
    protected boolean repositionEntityAfterLoad() {
        return false;
    }

    @Override
    public void thunderHit(ServerWorld pLevel, LightningBoltEntity pLightning) {
    }

    @Override
    public void refreshDimensions() {
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public String getJoyType() {
        return this.entityData.get(SIT_TYPE);
    }

    public void setJoyType(String type) {
        this.entityData.set(SIT_TYPE, type);
    }
}
