package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "positionRider(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity$MoveFunction;)V",
            at = @At("HEAD"),
            cancellable = true
    )
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

    @Inject(
            method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
            at = @At("HEAD")
    )
    private void onCollide(Vec3 pVec, CallbackInfoReturnable<Vec3> cir) {
        if ((Object) this instanceof EntityBroom broom) {
            broom.inPhysicalCheck = true;
        }
    }

    @Inject(
            method = "collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;",
            at = @At("RETURN")
    )
    private void onCollideReturn(Vec3 pVec, CallbackInfoReturnable<Vec3> cir) {
        if ((Object) this instanceof EntityBroom broom) {
            broom.inPhysicalCheck = false;
        }
    }

    @Inject(
            method = "getBoundingBox",
            at = @At("RETURN"),
            cancellable = true
    )
    @SuppressWarnings("all")
    private void onGetBoundingBox(CallbackInfoReturnable<AABB> cir) {
        if ((Object) this instanceof EntityBroom broom && broom.inPhysicalCheck) {
            cir.setReturnValue(broom.getPhysicalBoundingBox());
        }
    }
}
