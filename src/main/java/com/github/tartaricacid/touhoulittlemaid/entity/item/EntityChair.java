package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.OpenChairGuiMessage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class EntityChair extends AbstractEntityFromItem {
    public static final EntityType<EntityChair> TYPE = EntityType.Builder.<EntityChair>of(EntityChair::new, EntityClassification.MISC)
            .sized(0.875f, 0.5f).clientTrackingRange(10).build("chair");
    private static final DataParameter<String> MODEL_ID = EntityDataManager.defineId(EntityChair.class, DataSerializers.STRING);
    private static final String MODEL_ID_TAG = "ModelId";
    private static final String MOUNTED_HEIGHT_TAG = "MountedHeight";
    private static final String TAMEABLE_CAN_RIDE_TAG = "TameableCanRide";
    private static final String DEFAULT_MODEL_ID = "touhou_little_maid:cushion";
    private float mountedHeight = 0.0f;
    private boolean tameableCanRide = true;

    public EntityChair(EntityType<EntityChair> type, World worldIn) {
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

    @Override
    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (this.hasPassenger(passenger) && passenger instanceof LivingEntity) {
            // renderYawOffset 也必须同步，因为坐上的女仆朝向受 renderYawOffset 限制
            // 不同步就会导致朝向出现小问题
            // Fixme: 有问题，需要修改
            this.yHeadRot = ((LivingEntity) passenger).yHeadRot;
        }
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
        // TODO: 需要配置文件修改
        return true;
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
        if (compound.contains(MODEL_ID_TAG)) {
            setModelId(compound.getString(MODEL_ID_TAG));
        }
        if (compound.contains(MOUNTED_HEIGHT_TAG)) {
            setMountedHeight(compound.getFloat(MOUNTED_HEIGHT_TAG));
        }
        if (compound.contains(TAMEABLE_CAN_RIDE_TAG)) {
            setTameableCanRide(compound.getBoolean(TAMEABLE_CAN_RIDE_TAG));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putString(MODEL_ID_TAG, getModelId());
        compound.putFloat(MOUNTED_HEIGHT_TAG, getMountedHeight());
        compound.putBoolean(TAMEABLE_CAN_RIDE_TAG, isTameableCanRide());
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
        return mountedHeight;
    }

    public void setMountedHeight(float mountedHeight) {
        // 防止有人恶意利用这一点，强行增加范围限制
        this.mountedHeight = MathHelper.clamp(mountedHeight, -0.5f, 2.5f);
    }

    public boolean isTameableCanRide() {
        return tameableCanRide;
    }

    public void setTameableCanRide(boolean tameableCanRide) {
        this.tameableCanRide = tameableCanRide;
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
