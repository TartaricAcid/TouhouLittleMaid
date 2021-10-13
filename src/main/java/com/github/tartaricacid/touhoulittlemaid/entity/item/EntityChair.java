package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.ChairConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenChairGuiMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class EntityChair extends AbstractEntityFromItem {
    public static final EntityType<EntityChair> TYPE = EntityType.Builder.<EntityChair>of(EntityChair::new, EntityClassification.MISC)
            .sized(0.875f, 0.5f).clientTrackingRange(10).build("chair");

    private static final DataParameter<String> MODEL_ID = EntityDataManager.defineId(EntityChair.class, DataSerializers.STRING);
    private static final DataParameter<Float> MOUNTED_HEIGHT = EntityDataManager.defineId(EntityChair.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> TAMEABLE_CAN_RIDE = EntityDataManager.defineId(EntityChair.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Optional<UUID>> OWNER_UUID = EntityDataManager.defineId(EntityChair.class, DataSerializers.OPTIONAL_UUID);

    private static final String MODEL_ID_TAG = "ModelId";
    private static final String MOUNTED_HEIGHT_TAG = "MountedHeight";
    private static final String TAMEABLE_CAN_RIDE_TAG = "TameableCanRide";
    private static final String OWNER_UUID_TAG = "OwnerUUID";

    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:cushion";

    protected EntityChair(EntityType<EntityChair> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityChair(World worldIn) {
        this(TYPE, worldIn);
    }

    public EntityChair(World worldIn, double x, double y, double z, float yaw) {
        this(TYPE, worldIn);
        this.setPos(x, y, z);
        this.setRot(yaw, 0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MODEL_ID, DEFAULT_MODEL_ID);
        this.entityData.define(MOUNTED_HEIGHT, 0f);
        this.entityData.define(TAMEABLE_CAN_RIDE, true);
        this.entityData.define(OWNER_UUID, Optional.empty());
    }

    @Override
    protected void pushEntities() {
        List<TameableEntity> list = level.getEntitiesOfClass(TameableEntity.class,
                getBoundingBox().expandTowards(0, 0.5, 0),
                e -> !e.isInSittingPose() && !e.isPassenger() && e.getPassengers().isEmpty());
        for (TameableEntity entity : list) {
            if (!level.isClientSide && isTameableCanRide()) {
                entity.startRiding(this);
            }
        }
    }

    /**
     * 不允许被挤走，所以此处留空
     */
    @Override
    public void push(Entity entityIn) {
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    /**
     * 此参数会影响钓鱼钩和客户端的渲染交互。
     * 所以将其设计为仅修改服务端，避免影响客户端渲染交互，同时不会在服务端被钓鱼钩影响
     */
    @Override
    @OnlyIn(Dist.DEDICATED_SERVER)
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public ActionResultType interact(PlayerEntity player, Hand hand) {
        if (player.isShiftKeyDown()) {
            if (player.getItemInHand(hand).interactLivingEntity(player, this, hand).consumesAction()) {
                return ActionResultType.SUCCESS;
            }
            if (!level.isClientSide) {
                NetworkHandler.sendToClientPlayer(new OpenChairGuiMessage(getId()), player);
            }
        } else {
            if (!level.isClientSide && getPassengers().isEmpty() && !player.isPassenger()) {
                player.startRiding(this);
            }
        }
        return ActionResultType.SUCCESS;
    }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getBoundingBoxForCulling() {
        BedrockModel<EntityChair> model = CustomPackLoader.CHAIR_MODELS.getModel(getModelId()).orElse(null);
        if (model == null) {
            return super.getBoundingBoxForCulling();
        }
        return model.getRenderBoundingBox().move(position());
    }

    @Override
    public double getPassengersRidingOffset() {
        return getMountedHeight();
    }

    @Override
    protected boolean canKillEntity(PlayerEntity player) {
        if (ChairConfig.CHAIR_CAN_DESTROYED_BY_ANYONE.get()) {
            return true;
        }
        return this.getOwnerUUID().map(uuid -> player.getUUID().equals(uuid)).orElse(true);
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.WOOL_BREAK;
    }

    @Override
    protected Item getWithItem() {
        return InitItems.CHAIR.get();
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(MODEL_ID_TAG, Constants.NBT.TAG_STRING)) {
            setModelId(compound.getString(MODEL_ID_TAG));
        }
        if (compound.contains(MOUNTED_HEIGHT_TAG, Constants.NBT.TAG_FLOAT)) {
            setMountedHeight(compound.getFloat(MOUNTED_HEIGHT_TAG));
        }
        if (compound.contains(TAMEABLE_CAN_RIDE_TAG, Constants.NBT.TAG_BYTE)) {
            setTameableCanRide(compound.getBoolean(TAMEABLE_CAN_RIDE_TAG));
        }
        if (compound.contains(OWNER_UUID_TAG)) {
            setOwnerUUID(NBTUtil.loadUUID(Objects.requireNonNull(compound.get(OWNER_UUID_TAG))));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putString(MODEL_ID_TAG, getModelId());
        compound.putFloat(MOUNTED_HEIGHT_TAG, getMountedHeight());
        compound.putBoolean(TAMEABLE_CAN_RIDE_TAG, isTameableCanRide());
        this.getOwnerUUID().ifPresent(uuid -> compound.putUUID(OWNER_UUID_TAG, uuid));
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        List<Entity> list = this.getPassengers();
        return list.isEmpty() ? null : list.get(0);
    }

    public String getModelId() {
        return this.entityData.get(MODEL_ID);
    }

    public void setModelId(String modelId) {
        this.entityData.set(MODEL_ID, modelId);
    }

    public float getMountedHeight() {
        return this.entityData.get(MOUNTED_HEIGHT);
    }

    public void setMountedHeight(float height) {
        height = MathHelper.clamp(height, -0.5f, 2.5f);
        this.entityData.set(MOUNTED_HEIGHT, height);
    }

    public boolean isTameableCanRide() {
        return this.entityData.get(TAMEABLE_CAN_RIDE);
    }

    public void setTameableCanRide(boolean canRide) {
        this.entityData.set(TAMEABLE_CAN_RIDE, canRide);
    }

    public Optional<UUID> getOwnerUUID() {
        return this.entityData.get(OWNER_UUID);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(uuid));
    }

    public void setOwner(@Nullable PlayerEntity player) {
        if (player != null) {
            this.setOwnerUUID(player.getUUID());
        }
    }

    public boolean hasPassenger() {
        return !getPassengers().isEmpty();
    }

    public float getPassengerYaw() {
        if (!getPassengers().isEmpty()) {
            return getPassengers().get(0).yRot;
        }
        return 0;
    }

    public float getYaw() {
        return yRot;
    }

    public float getPassengerPitch() {
        if (!getPassengers().isEmpty()) {
            return getPassengers().get(0).xRot;
        }
        return 0;
    }

    @Deprecated
    public int getDim() {
        RegistryKey<World> dim = this.level.dimension();
        if (dim.equals(World.OVERWORLD)) {
            return 0;
        }
        if (dim.equals(World.NETHER)) {
            return -1;
        }
        if (dim.equals(World.END)) {
            return 1;
        }
        return 0;
    }

    @Override
    protected ItemStack getKilledStack() {
        return ItemChair.setData(InitItems.CHAIR.get().getDefaultInstance(),
                new ItemChair.Data(getModelId(), getMountedHeight(), isTameableCanRide(), isNoGravity()));
    }
}
