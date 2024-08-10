package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.neoforged.neoforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public class CraftingTableBackpackContainer extends MaidMainContainer {
    public static final MenuType<CraftingTableBackpackContainer> TYPE = IMenuTypeExtension.create((windowId, inv, data) -> new CraftingTableBackpackContainer(windowId, inv, data.readInt()));
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 3, 3);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final ResultSlot resultSlot;
    private final Player player;

    public CraftingTableBackpackContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
        this.player = inventory.player;
        this.access = ContainerLevelAccess.create(this.getMaid().level, this.getMaid().blockPosition());
        this.resultSlot = new ResultSlot(this.player, this.craftSlots, this.resultSlots, 0, 229, 119);
        this.addSlot(this.resultSlot);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * 3, 152 + j * 18, 101 + i * 18));
            }
        }
    }

    @Override
    public void slotsChanged(Container container) {
        this.access.execute((level, blockPos) -> slotChangedCraftingGrid(this, level, this.player, this.craftSlots, this.resultSlots));
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.access.execute((level, blockPos) -> this.clearContainer(player, this.craftSlots));
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
        return slot.container != this.resultSlots && super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
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
                slot.setByPlayer(ItemStack.EMPTY);
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
    protected void addBackpackInv(Inventory inventory) {
        IItemHandler itemHandler = maid.getMaidInv();
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(itemHandler, 6 + i, 143 + 18 * i, 57));
        }
        for (int i = 0; i < 6; i++) {
            addSlot(new SlotItemHandler(itemHandler, 12 + i, 143 + 18 * i, 75));
        }
    }

    private void slotChangedCraftingGrid(AbstractContainerMenu menu, Level level, Player player, CraftingContainer container, ResultContainer result) {
        if (!level.isClientSide && level.getServer() != null) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            ItemStack stack1 = ItemStack.EMPTY;
            Optional<RecipeHolder<CraftingRecipe>> optional = level.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, CraftingInput.of(3,3,this.getItems()), level);
            if (optional.isPresent()) {
                RecipeHolder<CraftingRecipe> recipe = optional.get();
                if (result.setRecipeUsed(level, serverPlayer, recipe)) {
                    ItemStack stack2 = recipe.value().assemble(CraftingInput.of(3,3,this.getItems()), level.registryAccess());
                    if (stack2.isItemEnabled(level.enabledFeatures())) {
                        stack1 = stack2;
                    }
                }
            }
            result.setItem(0, stack1);
            menu.setRemoteSlot(0, stack1);
            serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(menu.containerId, menu.incrementStateId(), resultSlot.index, stack1));
        }
    }
}
