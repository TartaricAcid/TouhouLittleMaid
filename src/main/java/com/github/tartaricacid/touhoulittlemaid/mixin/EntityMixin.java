package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V", at = @At("HEAD"), cancellable = true)
    protected void positionRider(Entity passenger, Entity.MoveFunction callback, CallbackInfo ci) {
        if (passenger instanceof EntityMaid maid && maid.getVehicle() instanceof Player player) {
            Vec3 position = player.position();
            float radians = (float) -Math.toRadians(player.yBodyRot);
            Vec3 offset = position.add(new Vec3(0, 0, 0.75).yRot(radians));
            double yOffset = 0.15;
            if (player.isDescending()) {
                yOffset = yOffset - 0.3;
            }
            callback.accept(passenger, offset.x(), offset.y() + yOffset, offset.z());
            ci.cancel();
        }
    }
}
