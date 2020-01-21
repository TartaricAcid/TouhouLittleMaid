package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * @author TartaricAcid
 * @date 2020/1/21 17:45
 **/
public final class ItemDropUtil {
    private ItemDropUtil() {
    }

    public static void dropItemHandlerItems(@Nullable IItemHandler itemHandler, World world, Vec3d pos) {
        if (itemHandler != null && !world.isRemote) {
            for (int i = 0; i < itemHandler.getSlots(); ++i) {
                ItemStack itemstack = itemHandler.getStackInSlot(i);
                InventoryHelper.spawnItemStack(world, pos.x, pos.y, pos.z, itemstack);
            }
        }
    }
}
