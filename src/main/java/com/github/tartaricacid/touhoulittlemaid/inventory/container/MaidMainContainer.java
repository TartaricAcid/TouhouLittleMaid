package com.github.tartaricacid.touhoulittlemaid.inventory.container;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static net.minecraft.world.inventory.InventoryMenu.*;


public abstract class MaidMainContainer extends AbstractMaidContainer {
    protected static final int PLAYER_INVENTORY_SIZE = 36;
    protected static final ResourceLocation EMPTY_MAINHAND_SLOT = new ResourceLocation("item/empty_slot_sword");
    protected static final ResourceLocation EMPTY_BACK_SHOW_SLOT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "slot/empty_back_show_slot");
    protected static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
    protected static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public MaidMainContainer(MenuType<?> type, int id, Inventory inventory, int entityId) {
        super(type, id, inventory, entityId);
        if (maid != null) {
            this.addMaidArmorInv();
            this.addMaidHandInv();
            this.addMainDefaultInv();
            this.addBackpackInv(inventory);
        }
    }

    protected void addMaidHandInv() {
        LazyOptional<IItemHandler> hand = maid.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN);
        hand.ifPresent((handler) -> addSlot(new SlotItemHandler(handler, 0, 87, 77) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, EMPTY_MAINHAND_SLOT);
            }
        }));
        hand.ifPresent((handler) -> addSlot(new SlotItemHandler(handler, 1, 121, 77) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, EMPTY_ARMOR_SLOT_SHIELD);
            }
        }));
    }

    protected void addMaidArmorInv() {
        LazyOptional<IItemHandler> armor = maid.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.EAST);
        armor.ifPresent((handler -> {
            for (int i = 0; i < 2; ++i) {
                for (int j = 0; j < 2; j++) {
                    final EquipmentSlot equipmentSlot = SLOT_IDS[2 * i + j];
                    addSlot(new SlotItemHandler(handler, 3 - 2 * i - j, 94 + 20 * j, 37 + 20 * i) {
                        @Override
                        public int getMaxStackSize() {
                            return 1;
                        }

                        @Override
                        public boolean mayPlace(@Nonnull ItemStack stack) {
                            return stack.canEquip(equipmentSlot, maid) && stack.getItem().canFitInsideContainerItems();
                        }

                        @Override
                        public boolean mayPickup(Player playerIn) {
                            ItemStack itemstack = this.getItem();
                            boolean curseEnchant = !itemstack.isEmpty() && !playerIn.isCreative() && EnchantmentHelper.hasBindingCurse(itemstack);
                            return !curseEnchant && super.mayPickup(playerIn);
                        }

                        @Override
                        @OnlyIn(Dist.CLIENT)
                        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                            return Pair.of(BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[equipmentSlot.getIndex()]);
                        }
                    });
                }
            }
        }));
    }

    protected void addMainDefaultInv() {
        ItemStackHandler inv = maid.getMaidInv();
        // 默认背包
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(inv, i, 143 + 18 * i, 37));
            // 最后一格给予特殊图标
            if (i == 5) {
                addSlot(new SlotItemHandler(inv, i, 143 + 18 * i, 37) {
                    @Override
                    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                        return Pair.of(BLOCK_ATLAS, EMPTY_BACK_SHOW_SLOT);
                    }
                });
            }
        }
    }

    protected abstract void addBackpackInv(Inventory inventory);

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
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
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (stack2.getCount() == stack1.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack2);

            // 用来修正护甲值不变化的问题
            if (PLAYER_INVENTORY_SIZE <= index && index < PLAYER_INVENTORY_SIZE + 4) {
                EquipmentSlot equipmentSlot = SLOT_IDS[index - PLAYER_INVENTORY_SIZE];
                maid.setLastArmorItem(equipmentSlot, stack1);
            }
            // 还有主副手
            if (PLAYER_INVENTORY_SIZE + 4 <= index && index < PLAYER_INVENTORY_SIZE + 6) {
                int slotIndex = index - PLAYER_INVENTORY_SIZE - 4;
                EquipmentSlot equipmentSlot = slotIndex == 0 ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
                maid.setLastHandItem(equipmentSlot, stack1);
            }
        }
        return stack1;
    }
}
