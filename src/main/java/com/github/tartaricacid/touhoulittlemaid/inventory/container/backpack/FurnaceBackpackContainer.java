package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.FurnaceBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FurnaceBackpackContainer extends MaidMainContainer {
    public static final MenuType<FurnaceBackpackContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new FurnaceBackpackContainer(windowId, inv, data.readInt()));
    private final ContainerData data;

    public FurnaceBackpackContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
        FurnaceBackpackData furnaceData;
        if (this.getMaid().getBackpackData() instanceof FurnaceBackpackData) {
            furnaceData = (FurnaceBackpackData) this.getMaid().getBackpackData();
        } else {
            furnaceData = new FurnaceBackpackData(this.getMaid());
        }
        this.data = furnaceData.getDataAccess();
        this.addSlot(new Slot(furnaceData, 0, 161, 101) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return EntityMaid.canInsertItem(stack);
            }
        });
        this.addSlot(new FurnaceBackpackFuelSlot(this, furnaceData, 1, 161, 142));
        this.addSlot(new FurnaceResultSlot(inventory.player, furnaceData, 2, 221, 121));
        this.addDataSlots(this.data);
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

    private boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) > 0;
    }

    public int getBurnProgress() {
        int cookingProgress = this.data.get(2);
        int cookingTotalTime = this.data.get(3);
        return cookingTotalTime != 0 && cookingProgress != 0 ? cookingProgress * 24 / cookingTotalTime : 0;
    }

    public int getLitProgress() {
        int litDuration = this.data.get(1);
        if (litDuration == 0) {
            litDuration = 200;
        }
        return this.data.get(0) * 13 / litDuration;
    }

    public boolean isLit() {
        return this.data.get(0) > 0;
    }

    public static class FurnaceBackpackFuelSlot extends Slot {
        private final FurnaceBackpackContainer furnaceBackpackContainer;

        public FurnaceBackpackFuelSlot(FurnaceBackpackContainer furnaceBackpackContainer, Container container, int slot, int pX, int pY) {
            super(container, slot, pX, pY);
            this.furnaceBackpackContainer = furnaceBackpackContainer;
        }

        public static boolean isBucket(ItemStack stack) {
            return stack.is(Items.BUCKET);
        }

        public boolean mayPlace(ItemStack stack) {
            return this.furnaceBackpackContainer.isFuel(stack) || isBucket(stack);
        }

        public int getMaxStackSize(ItemStack stack) {
            return isBucket(stack) ? 1 : super.getMaxStackSize(stack);
        }
    }
}
