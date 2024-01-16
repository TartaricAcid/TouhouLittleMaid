package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemTankBackpack extends ItemMaidBackpack {
    public static ItemStack getTankBackpack(TankBackpackData data) {
        ItemStack backpack = InitItems.TANK_BACKPACK.get().getDefaultInstance();
        CompoundTag tags = backpack.getOrCreateTagElement("Tanks");
        data.getTank().writeToNBT(tags);
        return backpack;
    }

    public static void setTankBackpack(EntityMaid maid, TankBackpackData data, ItemStack backpack) {
        CompoundTag tags = backpack.getTagElement("Tanks");
        if (tags != null) {
            data.loadTank(tags, maid);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> components, TooltipFlag pIsAdvanced) {
        CompoundTag nbt = stack.getTagElement("Tanks");
        if (nbt != null) {
            MutableComponent fluidInfo;
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(nbt);
            if (fluidStack.getFluid() == Fluids.EMPTY || fluidStack.getAmount() == 0) {
                fluidInfo = new TranslatableComponent("tooltips.touhou_little_maid.tank_backpack.empty_fluid").withStyle(ChatFormatting.GRAY);
            } else {
                fluidInfo = new TranslatableComponent("tooltips.touhou_little_maid.tank_backpack.fluid",
                        fluidStack.getDisplayName(), fluidStack.getAmount()).withStyle(ChatFormatting.GRAY);
            }
            components.add(fluidInfo);
        }
    }
}
