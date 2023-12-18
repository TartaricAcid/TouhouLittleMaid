package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import java.util.Optional;

public class CraftingTableBackpackContainer extends MaidMainContainer {
    public static final ContainerType<CraftingTableBackpackContainer> TYPE = IForgeContainerType.create((windowId, inv, data) -> new CraftingTableBackpackContainer(windowId, inv, data.readInt()));
    private final CraftingInventory craftSlots = new CraftingInventory(this, 3, 3);
    private final CraftResultInventory resultSlots = new CraftResultInventory();
    private final IWorldPosCallable access;
    private final CraftingResultSlot resultSlot;
    private final PlayerEntity player;

    public CraftingTableBackpackContainer(int id, PlayerInventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
        this.player = inventory.player;
        this.access = IWorldPosCallable.create(this.getMaid().level, this.getMaid().blockPosition());
        this.resultSlot = new CraftingResultSlot(this.player, this.craftSlots, this.resultSlots, 0, 229, 119);
        this.addSlot(this.resultSlot);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * 3, 152 + j * 18, 101 + i * 18));
            }
        }
    }

    @Override
    public void slotsChanged(IInventory container) {
        this.access.execute((level, blockPos) -> slotChangedCraftingGrid(this.containerId, level, this.player, this.craftSlots, this.resultSlots));
    }

    @Override
    public void removed(PlayerEntity player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> this.clearContainer(player, level, this.craftSlots));
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int index) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack2 = slot.getItem();
            stack1 = stack2.copy();
            if (index == resultSlot.index) {
                this.access.execute((level, blockPos) -> stack2.getItem().onCraftedBy(stack2, level, player));
                if (!this.moveItemStackTo(stack2, 0, PLAYER_INVENTORY_SIZE, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(stack2, stack1);
            } else if (index < resultSlot.index) {
                if (index < PLAYER_INVENTORY_SIZE) {
                    if (!this.moveItemStackTo(stack2, PLAYER_INVENTORY_SIZE, resultSlot.index, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(stack2, 0, PLAYER_INVENTORY_SIZE, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack2, 0, PLAYER_INVENTORY_SIZE, false)) {
                return ItemStack.EMPTY;
            }
            if (stack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (stack2.getCount() == stack1.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, stack2);
            if (index == resultSlot.index) {
                player.drop(stack2, false);
            }
        }
        return stack1;
    }

    @Override
    protected void addBackpackInv(PlayerInventory inventory) {
        IItemHandler itemHandler = maid.getMaidInv();
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(itemHandler, 6 + i, 143 + 18 * i, 57));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(itemHandler, 12 + i, 143 + 18 * i, 75));
        }
    }

    protected void slotChangedCraftingGrid(int menuId, World level, PlayerEntity player, CraftingInventory container, CraftResultInventory result) {
        if (!level.isClientSide) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            ItemStack itemStack = ItemStack.EMPTY;
            Optional<ICraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, container, level);
            if (optional.isPresent()) {
                ICraftingRecipe icraftingrecipe = optional.get();
                if (result.setRecipeUsed(level, serverPlayer, icraftingrecipe)) {
                    itemStack = icraftingrecipe.assemble(container);
                }
            }
            result.setItem(0, itemStack);
            serverPlayer.connection.send(new SSetSlotPacket(menuId, resultSlot.index, itemStack));
        }
    }
}
