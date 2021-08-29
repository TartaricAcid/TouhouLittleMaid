package com.github.tartaricacid.touhoulittlemaid.entity.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.HandSide;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Collections;

public abstract class AbstractEntityFromItem extends LivingEntity {
    public AbstractEntityFromItem(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    /**
     * 判定此玩家能否击落实体
     *
     * @param player 使用击打的玩家
     * @return 能够击落
     */
    protected abstract boolean canKillEntity(PlayerEntity player);

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
    public void kill() {
        this.remove();
    }

    @Override
    public boolean hurt(@Nonnull DamageSource source, float amount) {
        if (!this.level.isClientSide && !this.dead && this.isAlive()) {
            // 如果实体是无敌的
            if (this.isInvulnerableTo(source)) {
                return false;
            }
            // 应用打掉的逻辑
            if (source.getDirectEntity() instanceof PlayerEntity) {
                return applyHitEntityLogic((PlayerEntity) source.getDirectEntity());
            }
        }
        return false;
    }

    private boolean applyHitEntityLogic(PlayerEntity player) {
        if (player.isShiftKeyDown()) {
            this.ejectPassengers();
            this.playSound(getHitSound(), 1.0f, 1.0f);
            if (player.isCreative() && !this.hasCustomName()) {
                this.remove();
            } else {
                if (canKillEntity(player)) {
                    this.killEntity();
                }
            }
            LazyOptional<IItemHandler> itemHandler = this.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (!level.isClientSide) {
                itemHandler.ifPresent((handler) -> {
                    for (int i = 0; i < handler.getSlots(); ++i) {
                        ItemStack itemstack = handler.getStackInSlot(i);
                        InventoryHelper.dropItemStack(level, getX(), getY(), getZ(), itemstack);
                    }
                });
            }
        }
        return true;
    }

    private void killEntity() {
        this.remove();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            ItemStack itemstack = getKilledStack();
            if (this.hasCustomName()) {
                itemstack.setHoverName(this.getCustomName());
            }
            this.spawnAtLocation(itemstack, 0.0F);
        }
    }

    @Override
    public void knockback(float strength, double ratioX, double ratioZ) {
        // 不允许被击退效果影响
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

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {
    }

    @Override
    public HandSide getMainArm() {
        return HandSide.LEFT;
    }
}
