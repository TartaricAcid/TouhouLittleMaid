package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
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

    /**
     * 修改为在 Entity.move() 中 collide() 调用的前后设置 inPhysicalCheck 标志
     * 以避免其它 mod 对 collide() 的修改导致 inPhysicalCheck 永久卡在 true 的问题
     */
    @Inject(
            method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;")
    )
    private void beforeCollide(MoverType type, Vec3 movement, CallbackInfo ci) {
        if ((Object) this instanceof EntityBroom broom) {
            broom.inPhysicalCheck = true;
        }
    }

    @Inject(
            method = "move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", shift = At.Shift.AFTER)
    )
    private void afterCollide(MoverType type, Vec3 movement, CallbackInfo ci) {
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
