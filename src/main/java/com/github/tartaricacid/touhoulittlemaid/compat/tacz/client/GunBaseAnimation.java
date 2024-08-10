//package com.github.tartaricacid.touhoulittlemaid.compat.tacz.client;
//
//import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
//import com.github.tartaricacid.touhoulittlemaid.client.animation.script.ModelRendererWrapper;
//import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
//import com.tacz.guns.api.item.IGun;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.api.distmarker.OnlyIn;
//
//import javax.annotation.Nullable;
//
//@OnlyIn(Dist.CLIENT)
//public class GunBaseAnimation {
//    public static boolean onSwingGun(IMaid maid, @Nullable ModelRendererWrapper armLeft, @Nullable ModelRendererWrapper armRight) {
//        EntityMaid strictMaid = maid.asStrictMaid();
//        if (strictMaid == null) {
//            return false;
//        }
//        if (!IGun.mainhandHoldGun(strictMaid)) {
//            return false;
//        }
//        if (armLeft != null) {
//            armLeft.setRotateAngleX(-1.75f);
//            armLeft.setRotateAngleY(0.5f);
//        }
//        if (armRight != null) {
//            armRight.setRotateAngleX(-1.65f);
//            armRight.setRotateAngleY(-0.174f);
//        }
//        return true;
//    }
//}
