package com.github.tartaricacid.touhoulittlemaid.inventory;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;

public class MaidMainContainer extends Container {
    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = new EntityEquipmentSlot[]{EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};
    public EntityMaid entityMaid;

    public MaidMainContainer(IInventory playerInventory, EntityMaid entityMaid) {
        addEntityArmorAndHandSlots(entityMaid);
        addPlayerSlots(playerInventory);
        this.entityMaid = entityMaid;
        entityMaid.guiOpening = true;
    }

    @Override
    public void detectAndSendChanges()
    {
        entityMaid.guiOpening = true;
        super.detectAndSendChanges();
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn)
    {
        entityMaid.guiOpening = false;
        super.onContainerClosed(playerIn);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.entityMaid.isTamed() && this.entityMaid.getOwnerId().equals(playerIn.getUniqueID())
                && this.entityMaid.isEntityAlive() && this.entityMaid.getDistance(playerIn) < 5.0F;
    }

    private void addPlayerSlots(IInventory playerInventory) {
        // 玩家物品栏
        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(playerInventory, j1 + (l + 1) * 9, 8 + j1 * 18, 84 + l * 18));
            }
        }

        // 玩家快捷栏
        for (int i1 = 0; i1 < 9; ++i1) {
            this.addSlotToContainer(new Slot(playerInventory, i1, 8 + i1 * 18, 142));
        }
    }

    private void addEntityArmorAndHandSlots(EntityMaid entityMaid) {
        IItemHandler itemHandler = entityMaid.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        // 占用 0-3
        addArmorSlot(entityMaid, itemHandler);
        // 占用 4-5
        addHandSlot(entityMaid, itemHandler);
    }

    private void addArmorSlot(EntityMaid entityMaid, IItemHandler itemHandler) {
        for (int k = 0; k < 4; ++k) {
            final EntityEquipmentSlot entityequipmentslot = VALID_EQUIPMENT_SLOTS[k];
            this.addSlotToContainer(new SlotItemHandler(itemHandler, 3 - k, 8, 8 + k * 18) {
                /**
                 * 以防万一，设置最大堆叠数为 1
                 */
                @Override
                public int getSlotStackLimit() {
                    return 1;
                }

                /**
                 * 检查格子是否放置的是指定物品
                 */
                @Override
                public boolean isItemValid(ItemStack stack) {
                    return stack.getItem().isValidArmor(stack, entityequipmentslot, entityMaid);
                }

                /**
                 * 检查能否拿出这个物品
                 * 如果物品为空，或者玩家为创造模式，物品没有绑定诅咒附魔才可以拿出
                 */
                @Override
                public boolean canTakeStack(EntityPlayer playerIn) {
                    ItemStack itemstack = this.getStack();
                    return (itemstack.isEmpty() || playerIn.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(playerIn);
                }

                @Override
                @Nullable
                @SideOnly(Side.CLIENT)
                public String getSlotTexture() {
                    return ItemArmor.EMPTY_SLOT_NAMES[entityequipmentslot.getIndex()];
                }
            });
        }
    }

    private void addHandSlot(EntityMaid entityMaid, IItemHandler itemHandler) {
        this.addSlotToContainer(new SlotItemHandler(itemHandler, 4, 80, 62));
        this.addSlotToContainer(new SlotItemHandler(itemHandler, 5, 98, 62) {
            @Override
            @Nullable
            @SideOnly(Side.CLIENT)
            public String getSlotTexture() {
                return "minecraft:items/empty_armor_slot_shield";
            }
        });
    }

    /**
     * 处理 Shift 点击情况下的物品逻辑
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 6) {
                if (!this.mergeItemStack(itemstack1, 6, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, 6, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
