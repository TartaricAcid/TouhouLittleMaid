package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent.TANK_BACKPACK_TAG;

public class ItemTankBackpack extends ItemMaidBackpack {
    public static ItemStack getTankBackpack(HolderLookup.Provider provider, TankBackpackData data) {
        ItemStack backpack = InitItems.TANK_BACKPACK.get().getDefaultInstance();
        CompoundTag tags = backpack.get(TANK_BACKPACK_TAG);
        if (tags == null) {
            tags = new CompoundTag();
            backpack.set(InitDataComponent.TANK_BACKPACK_TAG, tags);
        }
        data.getTank().writeToNBT(provider, tags);
        return backpack;
    }

    public static void setTankBackpack(EntityMaid maid, TankBackpackData data, ItemStack backpack) {
        CompoundTag tags = backpack.get(TANK_BACKPACK_TAG);
        if (tags == null) {
            tags = new CompoundTag();
            backpack.set(InitDataComponent.TANK_BACKPACK_TAG, tags);
        }
        data.loadTank(tags, maid);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag nbt = stack.get(TANK_BACKPACK_TAG);
        if (nbt != null) {
            CompoundTag compound = nbt.getCompound("Fluid");
            if (compound.isEmpty()) {
                return;
            }

            MutableComponent fluidInfo;
            Optional<FluidStack> fluid = FluidStack.parse(worldIn.registries(), compound);
            if (fluid.isEmpty()) {
                return;
            }
            FluidStack fluidStack = fluid.get();
            if (fluidStack.getFluid() == Fluids.EMPTY || fluidStack.getAmount() == 0) {
                fluidInfo = Component.translatable("tooltips.touhou_little_maid.tank_backpack.empty_fluid").withStyle(ChatFormatting.GRAY);
            } else {
                fluidInfo = Component.translatable("tooltips.touhou_little_maid.tank_backpack.fluid",
                        fluidStack.getFluid().getFluidType().getDescription(), fluidStack.getAmount()).withStyle(ChatFormatting.GRAY);
            }
            tooltip.add(fluidInfo);
        }
    }
}
