package com.github.tartaricacid.touhoulittlemaid.geckolib3.util;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.IAnimatable;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.AnimationFactory;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.InstancedAnimationFactory;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.manager.SingletonAnimationFactory;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;

public class GeckoLibUtil {
    public static AnimationFactory createFactory(IAnimatable animatable) {
        return createFactory(animatable, !(animatable instanceof Entity) && !(animatable instanceof TileEntity));
    }

    public static AnimationFactory createFactory(IAnimatable animatable, boolean singletonObject) {
        return singletonObject ? new SingletonAnimationFactory(animatable) : new InstancedAnimationFactory(animatable);
    }
}
