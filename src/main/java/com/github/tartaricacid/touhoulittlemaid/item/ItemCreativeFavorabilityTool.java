package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ItemCreativeFavorabilityTool extends Item {
    public ItemCreativeFavorabilityTool() {
        this.setMaxStackSize(1);
        this.setTranslationKey(TouhouLittleMaid.MOD_ID + ".creative_favorability_tool");
        this.setCreativeTab(MaidItems.MAIN_TABS);
        this.setHasSubtypes(true);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> items) {
        if (this.isInCreativeTab(tab)) {
            for (int i = 0; i < 3; i++) {
                items.add(new ItemStack(this, 1, i));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasEffect(ItemStack stack) {
        if (stack.getMetadata() == 0) {
            return true;
        }
        return super.hasEffect(stack);
    }

    @SideOnly(Side.CLIENT)
    @Override
    @ParametersAreNonnullByDefault
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        switch (stack.getMetadata()) {
            case 0:
            default:
                tooltip.add(I18n.format("tooltips.touhou_little_maid.creative_favorability_tool.full"));
                break;
            case 1:
                tooltip.add(I18n.format("tooltips.touhou_little_maid.creative_favorability_tool.add"));
                break;
            case 2:
                tooltip.add(I18n.format("tooltips.touhou_little_maid.creative_favorability_tool.reduce"));
                break;
        }
    }
}
