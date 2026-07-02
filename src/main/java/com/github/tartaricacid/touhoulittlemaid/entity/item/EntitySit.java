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
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class EntitySit extends Entity {
    public static final EntityType<EntitySit> TYPE = EntityType.Builder.<EntitySit>of(EntitySit::new, MobCategory.MISC)
            .sized(0.5f, 0.1f).clientTrackingRange(10).build("sit");
    private static final EntityDataAccessor<String> SIT_TYPE = SynchedEntityData.defineId(EntitySit.class, EntityDataSerializers.STRING);
    private static final UUID REACH_MODIFIER_UUID = UUID.fromString("c9d276a3-33d5-4b7a-a206-d2b18e2da4d5");
    private int passengerTick = 0;
    private BlockPos associatedBlockPos = BlockPos.ZERO;
    private UUID reachModifiedPlayer = null;

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
        if (tag.contains("AssociatedBlockPos", Tag.TAG_COMPOUND)) {
            this.associatedBlockPos = NbtUtils.readBlockPos(tag.getCompound("AssociatedBlockPos"));
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
            this.checkGroundValid();
            this.checkPassengers();
            this.tickReachModifier();
            if (this.getFirstPassenger() instanceof EntityMaid maid) {
                this.tickMaid(maid);
            }
        }
    }

    private void checkGroundValid() {
        if ("fishing".equals(this.getJoyType())) {
            if (!this.level.getBlockState(this.associatedBlockPos.below()).blocksMotion()) {
                this.discard();
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

            manager.apply(joyType);
            if (this.isIdleSchedule(maid)) {
                return;
            }
            if (this.isWorkSchedule(maid) && task.canSitInJoy(maid, joyType)) {
                return;
            }
            maid.stopRiding();
        }
    }

    private void tickReachModifier() {
        Entity passenger = this.getFirstPassenger();
        if (passenger instanceof Player player) {
            if (reachModifiedPlayer == null || !reachModifiedPlayer.equals(player.getUUID())) {
                var attr = player.getAttribute(ForgeMod.BLOCK_REACH.get());
                if (attr != null && attr.getModifier(REACH_MODIFIER_UUID) == null) {
                    attr.addTransientModifier(new AttributeModifier(REACH_MODIFIER_UUID, "gomoku_reach", 3.0, AttributeModifier.Operation.ADDITION));
                }
                reachModifiedPlayer = player.getUUID();
            }
        } else if (reachModifiedPlayer != null) {
            if (this.level instanceof ServerLevel serverLevel) {
                Player oldPlayer = serverLevel.getPlayerByUUID(reachModifiedPlayer);
                if (oldPlayer != null) {
                    var attr = oldPlayer.getAttribute(ForgeMod.BLOCK_REACH.get());
                    if (attr != null) {
                        attr.removeModifier(REACH_MODIFIER_UUID);
                    }
                }
            }
            reachModifiedPlayer = null;
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
