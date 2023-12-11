package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.entity.ai.brain.MaidSchedule;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.FavorabilityManager;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskBoardGames;
import com.github.tartaricacid.touhoulittlemaid.init.InitEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.StringUtils;

public class EntitySit extends Entity {
    public static final EntityType<EntitySit> TYPE = EntityType.Builder.<EntitySit>of(EntitySit::new, MobCategory.MISC)
            .sized(0.5f, 0.1f).clientTrackingRange(10).build("sit");
    private static final EntityDataAccessor<String> SIT_TYPE = SynchedEntityData.defineId(EntitySit.class, EntityDataSerializers.STRING);
    private int passengerTick = 0;

    public EntitySit(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntitySit(Level worldIn, Vec3 pos, String joyType) {
        this(TYPE, worldIn);
        this.setPos(pos);
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
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("SitJoyType", Tag.TAG_STRING)) {
            this.setJoyType(tag.getString("SitJoyType"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (StringUtils.isNotBlank(this.getJoyType())) {
            tag.putString("SitJoyType", this.getJoyType());
        }
    }

    @Override
    public void tick() {
        if (!this.level.isClientSide) {
            this.checkBelowWorld();
            this.checkPassengers();
            if (this.getFirstPassenger() instanceof EntityMaid maid) {
                this.tickMaid(maid);
            }
        }
    }

    private void tickMaid(EntityMaid maid) {
        if (tickCount % 2 == 0) {
            maid.setYRot(this.getYRot());
            maid.setYHeadRot(this.getYRot());
        }
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
            this.discard();
        }
    }

    private boolean isGomokuTask(EntityMaid maid) {
        return Type.GOMOKU.getTypeName().equals(this.getJoyType()) && maid.getTask().getUid().equals(TaskBoardGames.UID);
    }

    private boolean isIdleSchedule(EntityMaid maid) {
        MaidSchedule schedule = maid.getSchedule();
        int time = (int) (maid.level.getDayTime() % 24000L);
        switch (schedule) {
            case ALL -> {
                return false;
            }
            case NIGHT -> {
                return InitEntities.MAID_NIGHT_SHIFT_SCHEDULES.get().getActivityAt(time) == Activity.IDLE;
            }
            default -> {
                return InitEntities.MAID_DAY_SHIFT_SCHEDULES.get().getActivityAt(time) == Activity.IDLE;
            }
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
    public void move(MoverType pType, Vec3 pPos) {
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
    public void thunderHit(ServerLevel pLevel, LightningBolt pLightning) {
    }

    @Override
    public void refreshDimensions() {
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public String getJoyType() {
        return this.entityData.get(SIT_TYPE);
    }

    public void setJoyType(String type) {
        this.entityData.set(SIT_TYPE, type);
    }
}
