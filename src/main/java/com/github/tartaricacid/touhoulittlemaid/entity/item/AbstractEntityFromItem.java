package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.util.ItemDropUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Collections;

/**
 * @author TartaricAcid
 * @date 2020/2/2 20:33
 **/
public abstract class AbstractEntityFromItem extends EntityLivingBase {
    private static final String GAME_RULES_DO_ENTITY_DROPS = "doEntityDrops";

    AbstractEntityFromItem(World worldIn) {
        super(worldIn);
    }

    /**
     * 判定此玩家能否击落实体
     *
     * @param player 使用击打的玩家
     * @return 能够击落
     */
    protected abstract boolean canKillEntity(EntityPlayer player);

    /**
     * 击打时的音效
     *
     * @return 音效
     */
    protected abstract SoundEvent getHitSound();

    /**
     * 该实体对应的物品
     *
     * @return 物品
     */
    protected abstract Item getWithItem();

    /**
     * 该实体死亡时掉落的对应物品堆
     *
     * @return 物品堆
     */
    protected abstract ItemStack getKilledStack();

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (!this.world.isRemote && !this.isDead) {
            // 如果实体是无敌的
            if (this.isEntityInvulnerable(source)) {
                return false;
            }
            // 应用打掉的逻辑
            if (source.getTrueSource() instanceof EntityPlayer) {
                return applyHitEntityLogic((EntityPlayer) source.getTrueSource());
            }
        }
        return false;
    }

    private boolean applyHitEntityLogic(EntityPlayer player) {
        boolean isPlayerCreativeMode = player.capabilities.isCreativeMode;
        if (player.isSneaking()) {
            this.removePassengers();
            this.playSound(getHitSound(), 1.0f, 1.0f);
            if (isPlayerCreativeMode && !this.hasCustomName()) {
                this.setDead();
            } else {
                if (canKillEntity(player)) {
                    this.killEntity();
                }
            }
            IItemHandler itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            ItemDropUtil.dropItemHandlerItems(itemHandler, world, this.getPositionVector());
        }
        return true;
    }

    private void killEntity() {
        this.setDead();
        if (this.world.getGameRules().getBoolean(GAME_RULES_DO_ENTITY_DROPS)) {
            ItemStack itemstack = getKilledStack();
            if (this.hasCustomName()) {
                itemstack.setStackDisplayName(this.getCustomNameTag());
            }
            this.entityDropItem(itemstack, 0.0F);
        }
    }

    @Override
    public void knockBack(@Nonnull Entity entityIn, float strength, double xRatio, double zRatio) {
        // 均不允许被击退效果影响
    }

    @Nonnull
    @Override
    public ItemStack getPickedResult(RayTraceResult target) {
        return getKilledStack();
    }

    @Override
    public boolean attackable() {
        return false;
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
