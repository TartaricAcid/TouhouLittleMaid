package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemChair;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/8/8 11:34
 **/
public class EntityChair extends EntityLivingBase {
    private static final DataParameter<String> MODEL_ID = EntityDataManager.createKey(EntityChair.class, DataSerializers.STRING);
    private static final DataParameter<Float> MOUNTED_HEIGHT = EntityDataManager.createKey(EntityChair.class, DataSerializers.FLOAT);

    public EntityChair(World worldIn) {
        super(worldIn);
        this.rotationYawHead = rotationYaw;
        setSize(0.875f, 0.5f);
    }

    public EntityChair(World worldIn, double x, double y, double z, float yaw) {
        this(worldIn);
        this.setLocationAndAngles(x, y, z, yaw, 0);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(MODEL_ID, "touhou_little_maid:cushion");
        this.dataManager.register(MOUNTED_HEIGHT, -0.002f);
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
                if (!world.isRemote && notRiddenAndRiding && entity instanceof EntityTameable) {
                    // 待命状态的生物不执行坐的逻辑
                    if (((EntityTameable) entity).isSitting()) {
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

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (player.isSneaking()) {
            return false;
        } else {
            if (!this.world.isRemote && !this.isBeingRidden()) {
                player.startRiding(this);
            }
            return true;
        }
    }

    @Override
    public void updatePassenger(@Nonnull Entity passenger) {
        super.updatePassenger(passenger);
        if (passenger instanceof EntityMaid) {
            EntityMaid maid = (EntityMaid) passenger;
            // renderYawOffset 也必须同步，因为坐上的女仆朝向受 renderYawOffset 限制
            // 不同步就会导致朝向出现小问题
            this.renderYawOffset = maid.renderYawOffset;
        }
    }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
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
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (!this.world.isRemote && !this.isDead) {
            // 如果实体是无敌的
            if (this.isEntityInvulnerable(source)) {
                return false;
            }
            // 应用打掉的逻辑
            if (source.getTrueSource() instanceof EntityPlayer) {
                return applyHitChairLogic((EntityPlayer) source.getTrueSource());
            }
        }
        return false;
    }

    /**
     * 击打扫帚逻辑
     */
    private boolean applyHitChairLogic(EntityPlayer player) {
        boolean isPlayerCreativeMode = player.capabilities.isCreativeMode;
        // 潜行状态才会运用击打逻辑
        if (player.isSneaking()) {
            this.removePassengers();
            this.playSound(SoundEvents.BLOCK_CLOTH_BREAK, 1.0f, 1.0f);
            if (isPlayerCreativeMode && !this.hasCustomName()) {
                // 如果是创造模式，而且坐垫没有命名，直接消失
                this.setDead();
            } else {
                // 否则应用实体转物品逻辑
                this.killChair();
            }
        }
        return true;
    }

    /**
     * 将扫帚从实体状态转变为物品状态
     */
    private void killChair() {
        this.setDead();
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = new ItemStack(MaidItems.CHAIR, 1);
            ItemChair.setModelIdAndHeight(itemstack, this.getModelId(), this.getMountedHeight());
            if (this.hasCustomName()) {
                itemstack.setStackDisplayName(this.getCustomNameTag());
            }
            this.entityDropItem(itemstack, 0.0F);
        }
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
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setString(NBT.MODEL_ID.getName(), getModelId());
        compound.setFloat(NBT.MOUNTED_HEIGHT.getName(), getMountedHeight());
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
        this.dataManager.set(MOUNTED_HEIGHT, height);
    }

    // ------------ EntityLivingBase 要求实现的几个抽象方法，因为全用不上，故返回默认值 ----------- //

    @Nonnull
    @Override
    public ItemStack getItemStackFromSlot(@Nonnull EntityEquipmentSlot slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(@Nonnull EntityEquipmentSlot slotIn, @Nonnull ItemStack stack) {
    }

    @Nonnull
    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.LEFT;
    }

    enum NBT {
        // 模型 ID
        MODEL_ID("ModelId"),
        // 实体坐上去的高度
        MOUNTED_HEIGHT("MountedHeight");

        private String name;

        NBT(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
