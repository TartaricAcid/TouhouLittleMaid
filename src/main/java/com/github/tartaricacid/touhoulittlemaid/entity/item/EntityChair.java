package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.client.model.bedrock.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.ChairConfig;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenChairGuiMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.util.thread.EffectiveSide;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class EntityChair extends AbstractEntityFromItem {
    public static final EntityType<EntityChair> TYPE = EntityType.Builder.<EntityChair>of(EntityChair::new, MobCategory.MISC)
            .sized(0.875f, 0.5f).clientTrackingRange(10).build("chair");

    private static final EntityDataAccessor<String> MODEL_ID = SynchedEntityData.defineId(EntityChair.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Float> MOUNTED_HEIGHT = SynchedEntityData.defineId(EntityChair.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> TAMEABLE_CAN_RIDE = SynchedEntityData.defineId(EntityChair.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(EntityChair.class, EntityDataSerializers.OPTIONAL_UUID);

    private static final String MODEL_ID_TAG = "ModelId";
    private static final String MOUNTED_HEIGHT_TAG = "MountedHeight";
    private static final String TAMEABLE_CAN_RIDE_TAG = "TameableCanRide";
    private static final String OWNER_UUID_TAG = "OwnerUUID";

    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:cushion";

    protected EntityChair(EntityType<EntityChair> type, Level worldIn) {
        super(type, worldIn);
    }

    public EntityChair(Level worldIn) {
        this(TYPE, worldIn);
    }

    public EntityChair(Level worldIn, double x, double y, double z, float yaw) {
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
        if (!isTameableCanRide()) {
            return;
        }
        if (!level.isClientSide) {
            List<TamableAnimal> list = level.getEntitiesOfClass(TamableAnimal.class,
                    getBoundingBox().expandTowards(0, 0.5, 0),
                    e -> !e.isInSittingPose() && !e.isPassenger() && e.getPassengers().isEmpty());
            list.stream().findFirst().ifPresent(entity -> entity.startRiding(this));
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
    public boolean isPickable() {
        return !EffectiveSide.get().isServer();
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            if (player.getItemInHand(hand).interactLivingEntity(player, this, hand).consumesAction()) {
                return InteractionResult.SUCCESS;
            }
            if (!level.isClientSide) {
                NetworkHandler.sendToClientPlayer(new OpenChairGuiMessage(getId()), player);
            }
        } else {
            if (!level.isClientSide && getPassengers().isEmpty() && !player.isPassenger()) {
                player.startRiding(this);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Nonnull
    @Override
    @OnlyIn(Dist.CLIENT)
    public AABB getBoundingBoxForCulling() {
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
    protected boolean canKillEntity(Player player) {
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
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains(MODEL_ID_TAG, Tag.TAG_STRING)) {
            setModelId(compound.getString(MODEL_ID_TAG));
        }
        if (compound.contains(MOUNTED_HEIGHT_TAG, Tag.TAG_FLOAT)) {
            setMountedHeight(compound.getFloat(MOUNTED_HEIGHT_TAG));
        }
        if (compound.contains(TAMEABLE_CAN_RIDE_TAG, Tag.TAG_BYTE)) {
            setTameableCanRide(compound.getBoolean(TAMEABLE_CAN_RIDE_TAG));
        }
        if (compound.contains(OWNER_UUID_TAG)) {
            setOwnerUUID(NbtUtils.loadUUID(Objects.requireNonNull(compound.get(OWNER_UUID_TAG))));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
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
        height = Mth.clamp(height, -0.5f, 2.5f);
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

    public void setOwner(@Nullable Player player) {
        if (player != null) {
            this.setOwnerUUID(player.getUUID());
        }
    }

    public boolean hasPassenger() {
        return !getPassengers().isEmpty();
    }

    public float getPassengerYaw() {
        if (!getPassengers().isEmpty()) {
            return getPassengers().get(0).getYRot();
        }
        return 0;
    }

    public float getYaw() {
        return getYRot();
    }

    public float getPassengerPitch() {
        if (!getPassengers().isEmpty()) {
            return getPassengers().get(0).getXRot();
        }
        return 0;
    }

    @Deprecated
    public int getDim() {
        ResourceKey<Level> dim = this.level.dimension();
        if (dim.equals(Level.OVERWORLD)) {
            return 0;
        }
        if (dim.equals(Level.NETHER)) {
            return -1;
        }
        if (dim.equals(Level.END)) {
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
