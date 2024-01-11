package com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.MaidMainContainer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class TankBackpackContainer extends MaidMainContainer {
    public static final ContainerType<TankBackpackContainer> TYPE = IForgeContainerType.create((windowId, inv, data) -> new TankBackpackContainer(windowId, inv, data.readInt()));
    private static final ResourceLocation INPUT_SLOT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "slot/tank_input_slot");
    private static final ResourceLocation OUTPUT_SLOT = new ResourceLocation(TouhouLittleMaid.MOD_ID, "slot/tank_output_slot");
    private final IIntArray data;

    public TankBackpackContainer(int id, PlayerInventory inventory, int entityId) {
        super(TYPE, id, inventory, entityId);
        TankBackpackData tankData;
        if (this.getMaid().getBackpackData() instanceof TankBackpackData) {
            tankData = (TankBackpackData) this.getMaid().getBackpackData();
        } else {
            tankData = new TankBackpackData(this.getMaid());
        }
        this.data = tankData.getDataAccess();
        this.addSlot(new TankInputSlot(tankData, 0, 161, 101));
        this.addSlot(new TankOutputSlot(tankData, 1, 161, 140));
        this.addDataSlots(this.data);
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

    public int getFluidCount() {
        return this.data.get(0);
    }

    public static class TankInputSlot extends Slot {
        public TankInputSlot(IInventory pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return FluidUtil.getFluidHandler(stack).isPresent();
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return Pair.of(PlayerContainer.BLOCK_ATLAS, INPUT_SLOT);
        }
    }

    public static class TankOutputSlot extends Slot {
        public TankOutputSlot(IInventory pContainer, int pSlot, int pX, int pY) {
            super(pContainer, pSlot, pX, pY);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return FluidUtil.getFluidHandler(stack).isPresent();
        }

        @Override
        @OnlyIn(Dist.CLIENT)
        public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
            return Pair.of(PlayerContainer.BLOCK_ATLAS, OUTPUT_SLOT);
        }
    }
}
