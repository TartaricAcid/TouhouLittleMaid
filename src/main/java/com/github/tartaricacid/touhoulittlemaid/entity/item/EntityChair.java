package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import com.github.tartaricacid.touhoulittlemaid.util.ParseI18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/8 11:34
 **/
public class EntityChair extends AbstractEntityFromItem {
    private static final DataParameter<String> MODEL_ID = EntityDataManager.createKey(EntityChair.class, DataSerializers.STRING);
    private static final DataParameter<Float> MOUNTED_HEIGHT = EntityDataManager.createKey(EntityChair.class, DataSerializers.FLOAT);
    private static final DataParameter<Boolean> TAMEABLE_CAN_RIDE = EntityDataManager.createKey(EntityChair.class, DataSerializers.BOOLEAN);

    /**
     * 是否开启 debug 模式下的地面显示，仅在客户端调用
     */
    public boolean isDebugFloorOpen = false;
    /**
     * 是否开启 debug 模式下的人物显示，仅在客户端调用
     */
    public boolean isDebugCharacterOpen = false;

    public EntityChair(World worldIn) {
        super(worldIn);
        setSize(0.875f, 0.5f);
    }

    public EntityChair(World worldIn, double x, double y, double z, float yaw) {
        this(worldIn);
        this.setLocationAndAngles(x, y, z, yaw, 0);
    }

    @Override
    protected boolean canKillEntity(EntityPlayer player) {
        return !GeneralConfig.MISC_CONFIG.chairCannotBeDestroied || player.isCreative();
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_CLOTH_BREAK;
    }

    @Override
    protected Item getWithItem() {
        return MaidItems.CHAIR;
    }

    @Override
    protected ItemStack getKilledStack() {
        ItemStack itemstack = new ItemStack(getWithItem());
        return ItemChair.setAllTagData(itemstack, this.getModelId(), this.getMountedHeight(), this.isTameableCanRide(), this.hasNoGravity());
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(MODEL_ID, "touhou_little_maid:cushion");
        this.dataManager.register(MOUNTED_HEIGHT, 0f);
        this.dataManager.register(TAMEABLE_CAN_RIDE, true);
    }

    @Override
    protected void collideWithNearbyEntities() {
        super.collideWithNearbyEntities();
        List<Entity> list = this.world.getEntitiesWithinAABB(EntityTameable.class, this.getEntityBoundingBox().expand(0, 0.5, 0));
        if (list.isEmpty()) {
            return;
        }
        // 遍历进行乘坐判定
        for (Entity entity : list) {
            // 如果选择的实体不是已经坐上去的乘客
            if (!entity.isPassenger(this)) {
                // 该实体既没有坐在别的实体上，也没有别的实体坐在它身上
                boolean notRiddenAndRiding = !entity.isBeingRidden() && !entity.isRiding();
                // 因为某些人会利用此功能做一些破坏平衡性的设计（用座椅锁住 xx Boss）
                // 所以限定，只允许 EntityTameable 坐上去（应该不会有人把 Boss 设计为 EntityTameable 的子类吧）
                if (!world.isRemote && notRiddenAndRiding && entity instanceof EntityTameable && isTameableCanRide()) {
                    // 待命状态的生物不执行坐的逻辑
                    if (((EntityTameable) entity).isSitting()) {
                        return;
                    }
                    if (entity instanceof EntityMaid && !((EntityMaid) entity).isCanRiding()) {
                        return;
                    }
                    entity.startRiding(this);
                }
            }
        }
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    /**
     * 不允许被挤走，所以此处留空
     */
    @Override
    protected void collideWithEntity(Entity entityIn) {
    }

    /**
     * 此参数会影响钓鱼钩和客户端的渲染交互。
     * 所以将其设计为仅修改服务端，避免影响客户端渲染交互，同时不会在服务端被钓鱼钩影响
     */
    @Override
    @SideOnly(Side.SERVER)
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            if (player.getHeldItem(hand).interactWithEntity(player, this, hand)) {
                return true;
            }
            if (world.isRemote) {
                player.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.CHAIR.getId(), world, this.getEntityId(), 0, 0);
            }
            return true;
        } else {
            if (!this.world.isRemote && !this.isBeingRidden() && !player.isRiding()) {
                player.startRiding(this);
            }
            return true;
        }
    }

    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        super.updatePassenger(passenger);
        if (this.isPassenger(passenger) && passenger instanceof EntityLivingBase) {
            // renderYawOffset 也必须同步，因为坐上的女仆朝向受 renderYawOffset 限制
            // 不同步就会导致朝向出现小问题
            this.renderYawOffset = ((EntityLivingBase) passenger).renderYawOffset;
        }
    }

    @Nonnull
    @Override
    public String getName() {
        if (this.hasCustomName()) {
            return this.getCustomNameTag();
        }
        if (world.isRemote && CustomResourcesLoader.CHAIR_MODEL.getInfo(getModelId()).isPresent()) {
            String name = CustomResourcesLoader.CHAIR_MODEL.getInfo(getModelId()).get().getName();
            return ParseI18n.parse(name);
        }
        return super.getName();
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        EntityModelJson modelJson = CustomResourcesLoader.CHAIR_MODEL.getModel(getModelId()).orElse(null);
        if (modelJson == null) {
            return super.getRenderBoundingBox();
        }
        return modelJson.renderBoundingBox.offset(getPositionVector());
    }

    @Override
    public double getMountedYOffset() {
        return getMountedHeight();
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey(NBT.MODEL_ID.getName())) {
            setModelId(compound.getString(NBT.MODEL_ID.getName()));
        }
        if (compound.hasKey(NBT.MOUNTED_HEIGHT.getName())) {
            setMountedHeight(compound.getFloat(NBT.MOUNTED_HEIGHT.getName()));
        }
        if (compound.hasKey(NBT.TAMEABLE_CAN_RIDE.getName())) {
            setTameableCanRide(compound.getBoolean(NBT.TAMEABLE_CAN_RIDE.getName()));
        }
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString(NBT.MODEL_ID.getName(), getModelId());
        compound.setFloat(NBT.MOUNTED_HEIGHT.getName(), getMountedHeight());
        compound.setBoolean(NBT.TAMEABLE_CAN_RIDE.getName(), isTameableCanRide());
    }

    public String getModelId() {
        return this.dataManager.get(MODEL_ID);
    }

    public void setModelId(String modelId) {
        this.dataManager.set(MODEL_ID, modelId);
    }

    public float getMountedHeight() {
        return this.dataManager.get(MOUNTED_HEIGHT);
    }

    public void setMountedHeight(float height) {
        // 防止有人恶意利用这一点，强行增加范围限制
        height = MathHelper.clamp(height, -0.5f, 2.5f);
        this.dataManager.set(MOUNTED_HEIGHT, height);
    }

    public boolean isTameableCanRide() {
        return this.dataManager.get(TAMEABLE_CAN_RIDE);
    }

    public void setTameableCanRide(boolean canRide) {
        this.dataManager.set(TAMEABLE_CAN_RIDE, canRide);
    }

    public boolean hasPassenger() {
        return !getPassengers().isEmpty();
    }

    public float getPassengerYaw() {
        if (!getPassengers().isEmpty()) {
            return getPassengers().get(0).rotationYaw;
        }
        return 0;
    }

    public float getYaw() {
        return rotationYaw;
    }

    public float getPassengerPitch() {
        if (!getPassengers().isEmpty()) {
            return getPassengers().get(0).rotationPitch;
        }
        return 0;
    }

    public int getDim() {
        return dimension;
    }

    enum NBT {
        // 模型 ID
        MODEL_ID("ModelId"),
        // 实体坐上去的高度
        MOUNTED_HEIGHT("MountedHeight"),
        // 女仆能坐上去么？
        TAMEABLE_CAN_RIDE("TameableCanRide");

        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
