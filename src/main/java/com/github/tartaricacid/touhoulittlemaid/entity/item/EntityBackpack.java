package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.util.ItemDropUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2020/1/26 0:45
 **/
public class EntityBackpack extends Entity {
    private static final String INV_TAG_NAME = "BackpackInv";
    private static final String LEVEL_TAG_NAME = "BackpackLevel";
    private final ItemStackHandler inv = new ItemStackHandler(30);
    private static final DataParameter<Integer> BACKPACK_LEVEL = EntityDataManager.createKey(EntityBackpack.class, DataSerializers.VARINT);

    public EntityBackpack(World worldIn) {
        super(worldIn);
        setSize(0.8f, 0.8f);
    }

    public EntityBackpack(World worldIn, EntityMaid.EnumBackPackLevel backPackLevel) {
        super(worldIn);
        setBackpackLevel(backPackLevel);
    }

    @Override
    public boolean canBeCollidedWith() {
        return !this.isDead;
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
                return applyHitBackpackLogic((EntityPlayer) source.getTrueSource());
            }
        }
        return false;
    }

    private boolean applyHitBackpackLogic(EntityPlayer player) {
        boolean isPlayerCreativeMode = player.capabilities.isCreativeMode;
        if (player.isSneaking()) {
            this.playSound(SoundEvents.BLOCK_CLOTH_BREAK, 1.0f, 1.0f);
            if (isPlayerCreativeMode) {
                this.setDead();
            } else {
                this.killBackpack();
            }
            ItemDropUtil.dropItemHandlerItems(getInv(), world, this.getPositionVector());
        }
        return true;
    }

    private void killBackpack() {
        this.setDead();
        if (this.world.getGameRules().getBoolean("doEntityDrops")) {
            for (int i = 1; i <= getBackpackLevel().getLevel(); i++) {
                ItemStack itemstack = new ItemStack(MaidItems.MAID_BACKPACK, 1, i);
                this.entityDropItem(itemstack, 0.0F);
            }
        }
    }

    public ItemStackHandler getInv() {
        return inv;
    }

    @Override
    protected void entityInit() {
        this.dataManager.register(BACKPACK_LEVEL, EntityMaid.EnumBackPackLevel.SMALL.getLevel());
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        if (compound.hasKey(INV_TAG_NAME, Constants.NBT.TAG_COMPOUND)) {
            inv.deserializeNBT(compound.getCompoundTag(INV_TAG_NAME));
        }
        if (compound.hasKey(LEVEL_TAG_NAME, Constants.NBT.TAG_INT)) {
            setBackpackLevel(compound.getInteger(LEVEL_TAG_NAME));
        }
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        compound.setTag(INV_TAG_NAME, inv.serializeNBT());
        compound.setInteger(LEVEL_TAG_NAME, getBackpackLevel().getLevel());
    }

    public EntityMaid.EnumBackPackLevel getBackpackLevel() {
        return EntityMaid.EnumBackPackLevel.getEnumLevelByNum(this.dataManager.get(BACKPACK_LEVEL));
    }

    public void setBackpackLevel(EntityMaid.EnumBackPackLevel level) {
        this.setBackpackLevel(level.getLevel());
    }

    private void setBackpackLevel(int level) {
        this.dataManager.set(BACKPACK_LEVEL, level);
    }
}
