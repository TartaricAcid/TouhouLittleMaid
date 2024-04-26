package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.HoneyBottleItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HoneyBottleItem.class)
public class HoneyBottleItemMixin {
    @Inject(method = "finishUsingItem", at = @At(value = "HEAD"))

    public void finishUsingItem(ItemStack stack, Level pLevel, LivingEntity pEntityLiving, CallbackInfoReturnable<ItemStack> cir) {
        if (pEntityLiving instanceof EntityMaid maid && stack.getCount() != 1) {
            if (!ItemHandlerHelper.insertItemStacked(maid.getAvailableInv(false), new ItemStack(Items.GLASS_BOTTLE), false).isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), new ItemStack(Items.GLASS_BOTTLE));
                maid.level.addFreshEntity(itemEntity);
            }
        }
    }
}
