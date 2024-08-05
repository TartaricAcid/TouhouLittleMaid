package com.github.tartaricacid.touhoulittlemaid.inventory.container;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static net.minecraft.world.inventory.InventoryMenu.*;


public abstract class MaidMainContainer extends AbstractMaidContainer {
    protected static final int PLAYER_INVENTORY_SIZE = 36;
    private static final ResourceLocation EMPTY_MAINHAND_SLOT = new ResourceLocation("item/empty_slot_sword");
    private static final ResourceLocation EMPTY_BACK_SHOW_SLOT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "slot/empty_back_show_slot");
    private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

    public MaidMainContainer(MenuType<?> type, int id, Inventory inventory, int entityId) {
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

    private void addMaidArmorInv() {
        LazyOptional<IItemHandler> armor = maid.getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.EAST);
        armor.ifPresent((handler -> {
            for (int i = 0; i < 2; ++i) {
                for (int j = 0; j < 2; j++) {
                    final EquipmentSlot EquipmentSlot = SLOT_IDS[2 * i + j];
                    addSlot(new SlotItemHandler(handler, 3 - 2 * i - j, 94 + 20 * j, 37 + 20 * i) {
                        @Override
                        public int getMaxStackSize() {
                            return 1;
                        }

                        @Override
                        public boolean mayPlace(@Nonnull ItemStack stack) {
                            return stack.canEquip(EquipmentSlot, maid) && stack.getItem().canFitInsideContainerItems();
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
                            return Pair.of(BLOCK_ATLAS, TEXTURE_EMPTY_SLOTS[EquipmentSlot.getIndex()]);
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
                        return Pair.of(BLOCK_ATLAS, EMPTY_BACK_SHOW_SLOT);
                    }
                });
            }
        }
    }

    protected abstract void addBackpackInv(Inventory inventory);

    private void addMaidBauble() {
        BaubleItemHandler maidBauble = maid.getMaidBauble();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotItemHandler(maidBauble, i * 3 + j, 86 + 18 * j, 99 + 18 * i));
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
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
