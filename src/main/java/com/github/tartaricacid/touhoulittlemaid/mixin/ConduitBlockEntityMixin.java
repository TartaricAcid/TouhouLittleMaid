package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ConduitBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ConduitBlockEntity.class)
public class ConduitBlockEntityMixin {
    @Inject(method = "applyEffects(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Ljava/util/List;)V", at = @At("HEAD"))
    private static void touhouLittleMaid$ApplyEffects(Level level, BlockPos pos, List<BlockPos> positions, CallbackInfo ci) {
        int distance = positions.size() / 7 * 16;
        AABB aabb = (new AABB(pos)).inflate(distance).expandTowards(0, level.getHeight(), 0);
        List<EntityMaid> list = level.getEntitiesOfClass(EntityMaid.class, aabb);
        for (EntityMaid maid : list) {
            if (pos.closerThan(maid.blockPosition(), distance) && maid.isInWaterOrRain()) {
                maid.addEffect(new MobEffectInstance(MobEffects.CONDUIT_POWER, 260, 0, true, true));
            }
        }
    }
}
