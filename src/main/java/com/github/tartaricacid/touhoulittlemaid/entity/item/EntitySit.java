package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.FavorabilityManager;
import com.github.tartaricacid.touhoulittlemaid.entity.favorability.Type;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskBoardGames;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class EntitySit extends Entity {
    public static final EntityType<EntitySit> TYPE = EntityType.Builder.<EntitySit>of(EntitySit::new, MobCategory.MISC)
            .sized(0.5f, 0.1f)
            .clientTrackingRange(10)
            .ridingOffset(-0.25F)
            .build("sit");
    private static final EntityDataAccessor<String> SIT_TYPE = SynchedEntityData.defineId(EntitySit.class, EntityDataSerializers.STRING);
    private int passengerTick = 0;
    private BlockPos associatedBlockPos = BlockPos.ZERO;

    public EntitySit(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntitySit(Level worldIn, Vec3 pos, String joyType, BlockPos associatedBlockPos) {
        this(TYPE, worldIn);
        this.setPos(pos);
        this.setJoyType(joyType);
        this.associatedBlockPos = associatedBlockPos;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SIT_TYPE, "");
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("SitJoyType", Tag.TAG_STRING)) {
            this.setJoyType(tag.getString("SitJoyType"));
        }
        if (tag.contains("AssociatedBlockPos", Tag.TAG_COMPOUND)) {
            Optional<BlockPos> blockPosOptional = NbtUtils.readBlockPos(tag, "AssociatedBlockPos");
            blockPosOptional.ifPresent(blockPos -> this.associatedBlockPos = blockPos);
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (StringUtils.isNotBlank(this.getJoyType())) {
            tag.putString("SitJoyType", this.getJoyType());
        }
        tag.put("AssociatedBlockPos", NbtUtils.writeBlockPos(this.associatedBlockPos));
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
        maid.setYRot(this.getYRot());
        maid.setYHeadRot(this.getYRot());
        if (tickCount % 20 == 0) {
            FavorabilityManager manager = maid.getFavorabilityManager();
            String joyType = this.getJoyType();
            IMaidTask task = maid.getTask();

            // 给予好感度提升
            manager.apply(joyType);
            // 如果是空闲状态，那么娱乐方块可以随便坐
            if (this.isIdleSchedule(maid)) {
                return;
            }
            // 如果是工作状态，看看这个工作是否允许你坐在上面
            if (this.isWorkSchedule(maid) && task.canSitInJoy(maid, joyType)) {
                return;
            }
            // 否则，不允许在上面待着
            maid.stopRiding();
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
        return Type.GOMOKU.getTypeName().equals(this.getJoyType()) && maid.getTask().getUid().equals(TaskBoardGames.UID) && isWorkSchedule(maid);
    }

    private boolean isIdleSchedule(EntityMaid maid) {
        return maid.getScheduleDetail() == Activity.IDLE;
    }

    private boolean isWorkSchedule(EntityMaid maid) {
        return maid.getScheduleDetail() == Activity.WORK;
    }

    public BlockPos getAssociatedBlockPos() {
        return associatedBlockPos;
    }

    @Override
    protected Vec3 getPassengerAttachmentPoint(Entity entity, EntityDimensions dimensions, float partialTick) {
        return new Vec3(0, -0.125, 0);
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

    public String getJoyType() {
        return this.entityData.get(SIT_TYPE);
    }

    public void setJoyType(String type) {
        this.entityData.set(SIT_TYPE, type);
    }
}
