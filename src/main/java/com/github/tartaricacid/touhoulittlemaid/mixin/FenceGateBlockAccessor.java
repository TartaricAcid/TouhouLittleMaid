package com.github.tartaricacid.touhoulittlemaid.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.FenceGateBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FenceGateBlock.class)
public interface FenceGateBlockAccessor {
    @Final
    @Accessor("openSound")
    SoundEvent tlmOpenSound();

    @Final
    @Accessor("closeSound")
    SoundEvent tlmCloseSound();
}
