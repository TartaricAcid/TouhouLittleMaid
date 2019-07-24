package com.github.tartaricacid.touhoulittlemaid.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Snownee
 * @date 2019/7/24 02:31
 */
public class ItemDamageable extends Item {
    public ItemDamageable(int maxDamage) {
        setMaxStackSize(1);
        setMaxDamage(maxDamage);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(I18n.format("tooltips.touhou_little_maid.damageable.desc", (stack.getMaxDamage() + 1 - stack.getItemDamage())));
    }
}
