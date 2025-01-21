package com.github.tartaricacid.touhoulittlemaid.mixin;

import com.github.tartaricacid.touhoulittlemaid.api.mixin.IPlayerMixin;
import net.minecraft.Util;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
}
