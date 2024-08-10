//package com.github.tartaricacid.touhoulittlemaid.compat.tacz;
//
//import com.tacz.guns.api.item.IGun;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.world.item.ItemStack;
//
//import javax.annotation.Nullable;
//
//public class TacInnerCompat {
//    @Nullable
//    public static ResourceLocation getGunId(ItemStack stack) {
//        IGun iGun = IGun.getIGunOrNull(stack);
//        if (iGun != null) {
//            return iGun.getGunId(stack);
//        }
//        return null;
//    }
//
//    public static boolean isGun(ItemStack itemStack) {
//        return itemStack.getItem() instanceof IGun;
//    }
//}
