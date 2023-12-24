package com.github.tartaricacid.touhoulittlemaid.entity.backpack.data;

import com.github.tartaricacid.touhoulittlemaid.api.backpack.IBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.MaidFluidUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.registries.ForgeRegistries;

public class TankBackpackData extends SimpleContainer implements IBackpackData {
    public static final int CAPACITY = 10 * FluidType.BUCKET_VOLUME;
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
        super(1);
        this.maid = maid;
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        if (!this.maid.level.isClientSide) {
            CombinedInvWrapper availableInv = this.maid.getAvailableInv(false);
            FluidActionResult fluidActionResult = MaidFluidUtil.tankToBucket(stack, tank, availableInv);
            if (!fluidActionResult.isSuccess()) {
                MaidFluidUtil.bucketToTank(stack, tank, availableInv);
            }
            this.tankFluidCount = tank.getFluidAmount();
            ResourceLocation key = ForgeRegistries.FLUIDS.getKey(tank.getFluid().getFluid());
            if (key != null) {
                maid.setBackpackFluid(key.toString());
            }
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
        tank.readFromNBT(tag.getCompound("Tanks"));
        this.fromTag(tag.getList("Items", Tag.TAG_COMPOUND));
        this.tankFluidCount = tank.getFluidAmount();
        ResourceLocation key = ForgeRegistries.FLUIDS.getKey(tank.getFluid().getFluid());
        if (key != null) {
            maid.setBackpackFluid(key.toString());
        }
    }

    @Override
    public void save(CompoundTag tag, EntityMaid maid) {
        tag.put("Tanks", tank.writeToNBT(new CompoundTag()));
        tag.put("Items", this.createTag());
    }

    @Override
    public void serverTick(EntityMaid maid) {
    }
}
