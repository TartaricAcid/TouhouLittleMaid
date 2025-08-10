package com.github.tartaricacid.touhoulittlemaid.mixin.client.compat;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import invtweaks.events.ClientEvents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.client.event.ScreenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientEvents.class)
public class InvTweaksMixin {
    @Inject(
            method = "onScreenEventInit",
            at = @At("HEAD"),
            remap = false,
            cancellable = true
    )
    private static void onScreenEventInit(ScreenEvent.Init.Post event, CallbackInfo ci) {
        Screen screen = event.getScreen();
        if (screen instanceof AbstractMaidContainerGui<?>) {
            ci.cancel();
        }
    }
}
