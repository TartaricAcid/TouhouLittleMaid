package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.entity.backpack.data.TankBackpackData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
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
    public void appendHoverText(ItemStack stack, @Nullable World pLevel, List<ITextComponent> components, ITooltipFlag pIsAdvanced) {
        CompoundNBT nbt = stack.getTagElement("Tanks");
        if (nbt != null) {
            IFormattableTextComponent fluidInfo;
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(nbt);
            if (fluidStack.getFluid() == Fluids.EMPTY || fluidStack.getAmount() == 0) {
                fluidInfo = new TranslationTextComponent("tooltips.touhou_little_maid.tank_backpack.empty_fluid").withStyle(TextFormatting.GRAY);
            } else {
                fluidInfo = new TranslationTextComponent("tooltips.touhou_little_maid.tank_backpack.fluid",
                        fluidStack.getDisplayName(), fluidStack.getAmount()).withStyle(TextFormatting.GRAY);
            }
            components.add(fluidInfo);
        }
    }
}
