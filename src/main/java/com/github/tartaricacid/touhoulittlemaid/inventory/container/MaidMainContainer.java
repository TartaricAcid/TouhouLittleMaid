package com.github.tartaricacid.touhoulittlemaid.inventory.container;

import com.github.tartaricacid.touhoulittlemaid.client.event.ReloadResourceEvent;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.mojang.datafixers.util.Pair;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static net.minecraft.inventory.container.PlayerContainer.*;

public abstract class MaidMainContainer extends AbstractMaidContainer {
    protected static final int PLAYER_INVENTORY_SIZE = 36;
    private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlotType[] SLOT_IDS = new EquipmentSlotType[]{EquipmentSlotType.HEAD, EquipmentSlotType.CHEST, EquipmentSlotType.LEGS, EquipmentSlotType.FEET};

    public MaidMainContainer(ContainerType<?> type, int id, PlayerInventory inventory, int entityId) {
        super(type, id, inventory, entityId);
        if (maid != null) {
            this.addMaidArmorInv();
            this.addMaidBauble();
            this.addMaidHandInv();
            this.addMainDefaultInv();
            this.addBackpackInv(inventory);
        }
    }

    private void addMaidHandInv() {
        LazyOptional<IItemHandler> hand = maid.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN);
        hand.ifPresent((handler) -> addSlot(new SlotItemHandler(handler, 0, 87, 77) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, ReloadResourceEvent.EMPTY_MAINHAND_SLOT);
            }
        }));
        hand.ifPresent((handler) -> addSlot(new SlotItemHandler(handler, 1, 121, 77) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(PlayerContainer.BLOCK_ATLAS, PlayerContainer.EMPTY_ARMOR_SLOT_SHIELD);
            }
        }));
    }

    private void addMaidArmorInv() {
        LazyOptional<IItemHandler> armor = maid.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.EAST);
        armor.ifPresent((handler -> {
            for (int i = 0; i < 2; ++i) {
                for (int j = 0; j < 2; j++) {
                    final EquipmentSlotType equipmentSlotType = SLOT_IDS[2 * i + j];
                    addSlot(new SlotItemHandler(handler, 3 - 2 * i - j, 94 + 20 * j, 37 + 20 * i) {
                        @Override
                        public int getMaxStackSize() {
                            return 1;
                        }

                        @Override
                        public boolean mayPlace(@Nonnull ItemStack stack) {
                            return stack.canEquip(equipmentSlotType, maid);
                        }

                        @Override
                        public boolean mayPickup(PlayerEntity playerIn) {
                            ItemStack itemstack = this.getItem();
                            boolean curseEnchant = !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack);
                            return !curseEnchant && super.mayPickup(playerIn);
                        }

                        @Override
                        @OnlyIn(Dist.CLIENT)
                        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                            return Pair.of(PlayerContainer.BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentSlotType.getIndex()]);
                        }
                    });
                }
            }
        }));
    }

    private void addMainDefaultInv() {
        ItemStackHandler inv = maid.getMaidInv();

        // 默认背包
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(inv, i, 143 + 18 * i, 37));
            // 最后一格给予特殊图标
            if (i == 5) {
                addSlot(new SlotItemHandler(inv, i, 143 + 18 * i, 37) {
                    @Override
                    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                        return Pair.of(PlayerContainer.BLOCK_ATLAS, ReloadResourceEvent.EMPTY_BACK_SHOW_SLOT);
                    }
                });
            }
        }
    }

    protected abstract void addBackpackInv(PlayerInventory inventory);

    private void addMaidBauble() {
        BaubleItemHandler maidBauble = maid.getMaidBauble();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotItemHandler(maidBauble, i * 3 + j, 86 + 18 * j, 99 + 18 * i));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack2 = slot.getItem();
            stack1 = stack2.copy();
            if (index < PLAYER_INVENTORY_SIZE) {
                if (!this.moveItemStackTo(stack2, PLAYER_INVENTORY_SIZE, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack2, 0, PLAYER_INVENTORY_SIZE, true)) {
                return ItemStack.EMPTY;
            }
            if (stack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack1;
    }
}
