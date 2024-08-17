package com.github.tartaricacid.touhoulittlemaid.entity.backpack.data;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.MaidFluidUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;

public class TankBackpackData extends SimpleContainer implements IBackpackData {
    public static final int CAPACITY = 10 * FluidType.BUCKET_VOLUME;
    private static final int INPUT_INDEX = 0;
    private static final int OUTPUT_INDEX = 1;
    private final EntityMaid maid;
    private final FluidTank tank = new FluidTank(CAPACITY);
    private final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            if (index == 0) {
                return TankBackpackData.this.tankFluidCount;
            }
            return 0;
        }

        public void set(int index, int value) {
            if (index == 0) {
                TankBackpackData.this.tankFluidCount = value;
            }
        }

        public int getCount() {
            return 1;
        }
    };
    private int tankFluidCount = 0;

    public TankBackpackData(EntityMaid maid) {
        super(2);
        this.maid = maid;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (!this.maid.level.isClientSide) {
            CombinedInvWrapper availableInv = this.maid.getAvailableInv(false);
            if (index == INPUT_INDEX) {
                MaidFluidUtil.bucketToTank(stack, tank, availableInv);
            }
            if (index == OUTPUT_INDEX) {
                MaidFluidUtil.tankToBucket(stack, tank, availableInv);
            }
            this.tankFluidCount = tank.getFluidAmount();
            ResourceLocation key = BuiltInRegistries.FLUID.getKey(tank.getFluid().getFluid());
            maid.setBackpackFluid(key.toString());
        }
        super.setItem(index, stack);
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public ContainerData getDataAccess() {
        return dataAccess;
    }

    @Override
    public void load(CompoundTag tag, EntityMaid maid) {
        this.loadTank(tag.getCompound("Tanks"), maid);
        this.fromTag(tag.getList("Items", Tag.TAG_COMPOUND), this.maid.registryAccess());
    }

    @Override
    public void save(CompoundTag tag, EntityMaid maid) {
        tag.put("Tanks", tank.writeToNBT(maid.registryAccess(), new CompoundTag()));
        tag.put("Items", this.createTag(maid.registryAccess()));
    }

    @Override
    public void serverTick(EntityMaid maid) {
    }

    public FluidTank getTank() {
        return tank;
    }

    public void loadTank(CompoundTag nbt, EntityMaid maid) {
        tank.readFromNBT(this.maid.registryAccess(), nbt);
        this.tankFluidCount = tank.getFluidAmount();
        ResourceLocation key = BuiltInRegistries.FLUID.getKey(tank.getFluid().getFluid());
        maid.setBackpackFluid(key.toString());
    }
}
