package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IEntityRidingMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author TartaricAcid
 * @date 2020/2/3 13:28
 **/
public class EntityMaidVehicle extends AbstractEntityFromItem implements IEntityRidingMaid {
    private static final String MODEL_ID_TAG = "ModelIdTag";
    private static final DataParameter<Integer> MODEL_ID = EntityDataManager.createKey(EntityMaidVehicle.class, DataSerializers.VARINT);

    public EntityMaidVehicle(World worldIn) {
        super(worldIn);
        setSize(0.8f, 0.8f);
    }

    @Override
    protected boolean canKillEntity(EntityPlayer player) {
        return true;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_CLOTH_BREAK;
    }

    @Override
    protected Item getWithItem() {
        return MaidItems.MAID_VEHICLE;
    }

    @Override
    protected ItemStack getKilledStack() {
        return new ItemStack(getWithItem(), 1, getModelId());
    }

    @Override
    public boolean canRiding(AbstractEntityMaid maid) {
        return maid.isCanHoldVehicle();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(MODEL_ID, 0);
    }

    @Override
    public double getMountedYOffset() {
        return Type.values()[getModelId()].height;
    }

    public float[] getMaidHandRotation(EnumHand hand) {
        Type model = Type.values()[getModelId()];
        return hand == EnumHand.MAIN_HAND ? model.rotationRight : model.rotationLeft;
    }

    @Override
    public void updatePassenger(AbstractEntityMaid maid) {
        // 视线也必须同步，因为拉杆箱的朝向受视线限制
        // 只能以视线方向为中心左右各 90 度，不同步就会导致朝向错误
        this.rotationYawHead = maid.rotationYawHead;
        // 旋转方向同步，包括渲染的旋转方向
        this.rotationPitch = maid.rotationPitch;
        this.rotationYaw = maid.rotationYaw;
        this.renderYawOffset = maid.renderYawOffset;
        double[] location = Type.values()[getModelId()].location;
        Vec3d vec3d = maid.getPositionVector().add(new Vec3d(location[0], location[1], location[2]).rotateYaw(maid.renderYawOffset * -0.01745329251f));
        this.setPosition(vec3d.x, vec3d.y, vec3d.z);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (player.getHeldItem(hand).getItem() == Items.NAME_TAG) {
            // 返回 false，交由玩家侧的右击事件进行处理
            return false;
        }
        if (!player.isSneaking() && !this.world.isRemote && !this.isBeingRidden()) {
            player.startRiding(this);
            return true;
        }
        return super.processInitialInteract(player, hand);
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
                boolean isMaidControl = this.getRidingEntity() instanceof AbstractEntityMaid;
                // 因为某些人会利用此功能做一些破坏平衡性的设计（用座椅锁住 xx Boss）
                // 所以限定，只允许 EntityTameable 坐上去（应该不会有人把 Boss 设计为 EntityTameable 的子类吧）
                if (!world.isRemote && notRiddenAndRiding && isMaidControl && entity instanceof EntityTameable) {
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
    public void dismountEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityMaid)) {
            super.dismountEntity(entityIn);
        }
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setInteger(MODEL_ID_TAG, getModelId());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        setModelId(compound.getInteger(MODEL_ID_TAG));
    }

    public int getModelId() {
        return MathHelper.clamp(dataManager.get(MODEL_ID), 0, Type.values().length - 1);
    }

    public void setModelId(int id) {
        dataManager.set(MODEL_ID, MathHelper.clamp(id, 0, Type.values().length - 1));
    }

    public enum Type {
        // 目前硬编码载具
        CAT_CART(new float[]{-1.2f, 0, -0.395f}, new float[]{-1.2f, 0, 0.395f},
                new double[]{0, 0, 1.2},
                0.125),
        WHEEL_CHAIR(new float[]{-1.4f, 0, -0.05f}, new float[]{-1.4f, 0, 0.05f},
                new double[]{0, 0, 1.1},
                0.3125),
        RICKSHAW(new float[]{-1.4f, 0, -0.05f}, new float[]{-1.4f, 0, 0.05f},
                new double[]{0, 0, -1.9},
                0.5625);

        float[] rotationLeft;
        float[] rotationRight;
        double[] location;
        double height;

        Type(float[] rotationLeft, float[] rotationRight, double[] location, double height) {
            this.rotationLeft = rotationLeft;
            this.rotationRight = rotationRight;
            this.location = location;
            this.height = height;
        }

        public static Type getTypeByNum(int num) {
            return values()[num];
        }
    }
}
