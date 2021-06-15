package com.github.tartaricacid.touhoulittlemaid.util;

import com.sun.javafx.geom.Vec3d;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

public final class ItemsUtil {
    private ItemsUtil() {
    }

    public static void dropItemHandlerItems(@Nullable IItemHandler itemHandler, World world, Vec3d pos) {
        if (itemHandler != null && !world.isClientSide) {
            for (int i = 0; i < itemHandler.getSlots(); ++i) {
                ItemStack itemstack = itemHandler.getStackInSlot(i);
                InventoryHelper.dropItemStack(world, pos.x, pos.y, pos.z, itemstack);
            }
        }
    }

    public static void dropEntityItems(Entity entity, IItemHandler itemHandler, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; i++) {
            InventoryHelper.dropItemStack(entity.level, entity.getX(), entity.getY(), entity.getZ(), itemHandler.getStackInSlot(i));
        }
    }

    public static void dropEntityItems(Entity entity, IItemHandler itemHandler) {
        dropEntityItems(entity, itemHandler, 0, itemHandler.getSlots());
    }

    public static void giveItemToPlayer(PlayerEntity player, ItemStack stack) {
        if (player.addItem(stack)) {
            player.level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
            player.inventoryMenu.broadcastChanges();
        } else {
            ItemEntity itemEntity = player.drop(stack, false);
            if (itemEntity != null) {
                itemEntity.setNoPickUpDelay();
                itemEntity.setOwner(player.getUUID());
            }
        }
    }
}
