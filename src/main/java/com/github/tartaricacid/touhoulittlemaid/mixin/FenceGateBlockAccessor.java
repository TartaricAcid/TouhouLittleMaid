package com.github.tartaricacid.touhoulittlemaid.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.FenceGateBlock;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * 因为某些奇怪的原因，FenceGateBlock 类下的 openSound 和 closeSound 没有对应的 SRG 名；
 * 只能用 mixin 进行访问
 */
@Mixin(FenceGateBlock.class)
public interface FenceGateBlockAccessor {
    @Final
    @Accessor("openSound")
    SoundEvent tlmOpenSound();

    @Final
    @Accessor("closeSound")
    SoundEvent tlmCloseSound();
}