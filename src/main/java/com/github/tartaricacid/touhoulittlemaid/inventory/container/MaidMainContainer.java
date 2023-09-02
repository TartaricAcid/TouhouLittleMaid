package com.github.tartaricacid.touhoulittlemaid.inventory.container;

import com.github.tartaricacid.touhoulittlemaid.client.event.ReloadResourceEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static net.minecraft.world.inventory.InventoryMenu.*;


public class MaidMainContainer extends AbstractContainerMenu {
    public static final MenuType<MaidMainContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new MaidMainContainer(windowId, inv, data.readInt()));
    private static final ResourceLocation[] TEXTURE_EMPTY_SLOTS = new ResourceLocation[]{EMPTY_ARMOR_SLOT_BOOTS, EMPTY_ARMOR_SLOT_LEGGINGS, EMPTY_ARMOR_SLOT_CHESTPLATE, EMPTY_ARMOR_SLOT_HELMET};
    private static final EquipmentSlot[] SLOT_IDS = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    private static final int PLAYER_INVENTORY_SIZE = 36;
    private final EntityMaid maid;

    public MaidMainContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id);
        this.maid = (EntityMaid) inventory.player.level.getEntity(entityId);
        if (maid != null) {
            this.addPlayerInv(inventory);
            this.addMaidArmorInv();
            this.addMaidBauble();
            this.addMaidHandInv();
            this.addMainInv();
            maid.guiOpening = true;
        }
    }

    private void addPlayerInv(Inventory playerInventory) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 88 + col * 18, 174 + row * 18));
            }
        }

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 88 + col * 18, 232));
        }
    }

    private void addMaidHandInv() {
        LazyOptional<IItemHandler> hand = maid.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.DOWN);
        hand.ifPresent((handler) -> addSlot(new SlotItemHandler(handler, 0, 87, 79) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, ReloadResourceEvent.EMPTY_MAINHAND_SLOT);
            }
        }));
        hand.ifPresent((handler) -> addSlot(new SlotItemHandler(handler, 1, 121, 79) {
            @Override
            @OnlyIn(Dist.CLIENT)
            public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                return Pair.of(BLOCK_ATLAS, EMPTY_ARMOR_SLOT_SHIELD);
            }
        }));
    }

    private void addMaidArmorInv() {
        LazyOptional<IItemHandler> armor = maid.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.EAST);
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
                            return stack.canEquip(EquipmentSlot, maid);
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

    private void addMainInv() {
        ItemStackHandler inv = maid.getMaidInv();
        int level = maid.getBackpackLevel();

        // 默认背包
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(inv, i, 143 + 18 * i, 37));
        }

        if (level > BackpackLevel.EMPTY) {
            for (int i = 0; i < 6; i++) {
                addSlot(new SlotItemHandler(inv, 6 + i, 143 + 18 * i, 59));
            }
        }

        if (level > BackpackLevel.SMALL) {
            for (int i = 0; i < 6; i++) {
                addSlot(new SlotItemHandler(inv, 12 + i, 143 + 18 * i, 82));
            }
            for (int i = 0; i < 6; i++) {
                addSlot(new SlotItemHandler(inv, 18 + i, 143 + 18 * i, 100));
            }
        }

        if (level > BackpackLevel.MIDDLE) {
            for (int i = 0; i < 6; i++) {
                addSlot(new SlotItemHandler(inv, 24 + i, 143 + 18 * i, 123));
            }
            for (int i = 0; i < 6; i++) {
                addSlot(new SlotItemHandler(inv, 30 + i, 143 + 18 * i, 141));
            }
        }
    }

    private void addMaidBauble() {
        BaubleItemHandler maidBauble = maid.getMaidBauble();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                addSlot(new SlotItemHandler(maidBauble, i * 3 + j, 86 + 18 * j, 105 + 18 * i) {
                    @Override
                    @OnlyIn(Dist.CLIENT)
                    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
                        return Pair.of(BLOCK_ATLAS, ReloadResourceEvent.EMPTY_BAUBLE_SLOT);
                    }
                });
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

    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        maid.guiOpening = false;
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return maid.isOwnedBy(playerIn) && !maid.isSleeping() && maid.isAlive() && maid.distanceTo(playerIn) < 5.0F;
    }

    @Nullable
    public EntityMaid getMaid() {
        return maid;
    }
}
