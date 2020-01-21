package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ICanRidingMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.network.MaidGuiHandler;
import com.github.tartaricacid.touhoulittlemaid.util.ItemDropUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;

/**
 * @author TartaricAcid
 * @date 2020/1/20 14:58
 **/
public class EntitySuitcase extends EntityLivingBase implements ICanRidingMaid {
    private static final String MAIN_INV_TAG = "MainInventory";
    private final ItemStackHandler mainInv = new ItemStackHandler(27);

    public EntitySuitcase(World worldIn) {
        super(worldIn);
        this.preventEntitySpawning = true;
        this.setSize(0.8f, 0.8f);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (player.getHeldItem(hand).interactWithEntity(player, this, hand)) {
            return true;
        }
        player.openGui(TouhouLittleMaid.INSTANCE, MaidGuiHandler.OTHER_GUI.SUITCASE.getId(), world, this.getEntityId(), 0, 0);
        return true;
    }

    @Override
    public void updatePassenger(AbstractEntityMaid maid) {
        if (maid.isPassenger(this)) {
            // 视线也必须同步，因为拉杆箱的朝向受视线限制
            // 只能以视线方向为中心左右各 90 度，不同步就会导致朝向错误
            this.rotationYawHead = maid.rotationYawHead;
            // 旋转方向同步，包括渲染的旋转方向
            this.rotationPitch = maid.rotationPitch;
            this.rotationYaw = maid.rotationYaw;
            this.renderYawOffset = maid.renderYawOffset;
            Vec3d vec3d = maid.getPositionVector().add(new Vec3d(0.4, 0, -1).rotateYaw(maid.renderYawOffset * -0.01745329251f));
            this.setPosition(vec3d.x, vec3d.y, vec3d.z);
        }
    }

    @Override
    public void dismountEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityMaid)) {
            super.dismountEntity(entityIn);
        }
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
                return applyHitSuitcaseLogic((EntityPlayer) source.getTrueSource());
            }
        }
        return false;
    }

    private boolean applyHitSuitcaseLogic(EntityPlayer player) {
        boolean isPlayerCreativeMode = player.capabilities.isCreativeMode;
        if (player.isSneaking()) {
            this.removePassengers();
            this.playSound(SoundEvents.BLOCK_CLOTH_BREAK, 1.0f, 1.0f);
            if (isPlayerCreativeMode && !this.hasCustomName()) {
                this.setDead();
            } else {
                this.killSuitcase();
            }
            IItemHandler itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            ItemDropUtil.dropItemHandlerItems(itemHandler, world, this.getPositionVector());
        }
        return true;
    }

    private void killSuitcase() {
        this.setDead();
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            ItemStack itemstack = new ItemStack(MaidItems.SUITCASE, 1);
            if (this.hasCustomName()) {
                itemstack.setStackDisplayName(this.getCustomNameTag());
            }
            this.entityDropItem(itemstack, 0.0F);
        }
    }

    @Nonnull
    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return new ItemStack(MaidItems.SUITCASE);
    }

    @Override
    public boolean attackable() {
        return false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setTag(MAIN_INV_TAG, mainInv.serializeNBT());
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey(MAIN_INV_TAG, Constants.NBT.TAG_COMPOUND)) {
            mainInv.deserializeNBT(compound.getCompoundTag(MAIN_INV_TAG));
        }
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(mainInv);
        }
        return super.getCapability(capability, facing);
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
}
