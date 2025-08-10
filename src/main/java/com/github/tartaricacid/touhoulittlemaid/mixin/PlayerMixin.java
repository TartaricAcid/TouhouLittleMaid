package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.api.mixin.IPlayerMixin;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin implements IPlayerMixin {
    @Unique
    private long removeVehicleTimestamp = -1L;

    @Inject(method = "removeVehicle()V", at = @At("HEAD"))
    private void tlmRemoveVehicle(CallbackInfo ci) {
        removeVehicleTimestamp = Util.getMillis();
    }

    @Override
    public boolean tlmInRemoveVehicleCooldown() {
        // 三秒冷却时间
        return Util.getMillis() - removeVehicleTimestamp < 3000;
    }

    @Inject(method = "wantsToStopRiding()Z", at = @At("HEAD"), cancellable = true)
    private void tlmWantsToStopRiding(CallbackInfoReturnable<Boolean> cir) {
        Player player = (Player) (Object) this;
        if (player.isShiftKeyDown() && player.getVehicle() instanceof EntityBroom) {
            cir.setReturnValue(false);
        }
    }
}
