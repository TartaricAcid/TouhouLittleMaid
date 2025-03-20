package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.api.mixin.INavigationMixin;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PathNavigation.class)
public class NavigationMixin implements INavigationMixin {
    @Shadow protected double speedModifier;

    @Unique
    public double touhouLittleMaid$GetSpeedModifier(){
        return this.speedModifier;
    }
}
