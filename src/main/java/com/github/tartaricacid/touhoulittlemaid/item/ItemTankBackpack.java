package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.TextFormatting;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.network.chat.MutableComponent;
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
        CompoundNBT tags = backpack.getOrCreateTagElement("Tanks");
        data.getTank().writeToNBT(tags);
        return backpack;
    }

    public static void setTankBackpack(EntityMaid maid, TankBackpackData data, ItemStack backpack) {
        CompoundNBT tags = backpack.getTagElement("Tanks");
        if (tags != null) {
            data.loadTank(tags, maid);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> components, TooltipFlag pIsAdvanced) {
        CompoundNBT nbt = stack.getTagElement("Tanks");
        if (nbt != null) {
            MutableComponent fluidInfo;
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(nbt);
            if (fluidStack.getFluid() == Fluids.EMPTY || fluidStack.getAmount() == 0) {
                fluidInfo = new TranslationTextComponent("tooltips.touhou_little_maid.tank_backpack.empty_fluid").withStyle(TextFormatting.GRAY);
            } else {
                fluidInfo = new TranslationTextComponent("tooltips.touhou_little_maid.tank_backpack.fluid",
                        fluidStack.getFluid().getFluidType().getDescription(), fluidStack.getAmount()).withStyle(TextFormatting.GRAY);
            }
            components.add(fluidInfo);
        }
    }
}
