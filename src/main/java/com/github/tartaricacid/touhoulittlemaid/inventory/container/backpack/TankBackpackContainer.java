package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TankBackpackContainer extends MaidMainContainer {
    public static final MenuType<TankBackpackContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new TankBackpackContainer(windowId, inv, data.readInt()));
    private final ContainerData data;

    public TankBackpackContainer(int id, Inventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
        TankBackpackData tankData;
        if (this.getMaid().getBackpackData() instanceof TankBackpackData) {
            tankData = (TankBackpackData) this.getMaid().getBackpackData();
        } else {
            tankData = new TankBackpackData(this.getMaid());
        }
        this.data = tankData.getDataAccess();
        this.addSlot(new TankInputSlot(tankData, 0, 161, 101));
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

    public int getTankPercent() {
        return this.data.get(0) / TankBackpackData.CAPACITY;
    }

    public int getFluidCount() {
        return this.data.get(0);
    }

    public static class TankInputSlot extends Slot {
        public TankInputSlot(Container pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return FluidUtil.getFluidHandler(stack).isPresent();
        }
    }
}
