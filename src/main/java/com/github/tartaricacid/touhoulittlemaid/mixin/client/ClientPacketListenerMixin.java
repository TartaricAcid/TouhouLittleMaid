package com.github.tartaricacid.touhoulittlemaid.mixin.client;

import com.github.tartaricacid.touhoulittlemaid.client.input.DismountBroomKey;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityBroom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundSetPassengersPacket;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;
    @Shadow
    private ClientLevel level;

    @Inject(method = "handleSetEntityPassengersPacket",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void tlmHandleSetEntityPassengersPacket(ClientboundSetPassengersPacket packet, CallbackInfo ci) {
        ClientPacketListener listener = (ClientPacketListener) (Object) this;
        PacketUtils.ensureRunningOnSameThread(packet, listener, this.minecraft);
        Entity vehicleEntity = this.level.getEntity(packet.getVehicle());
        LocalPlayer player = this.minecraft.player;
        if (!(vehicleEntity instanceof EntityBroom) || player == null) {
            return;
        }
        boolean playerWasPassenger = vehicleEntity.hasIndirectPassenger(player);
        vehicleEntity.ejectPassengers();
        for (int passengerId : packet.getPassengers()) {
            Entity passengerEntity = this.level.getEntity(passengerId);
            if (passengerEntity == null) {
                continue;
            }
            passengerEntity.startRiding(vehicleEntity, true);
            if (passengerEntity != player || playerWasPassenger) {
                continue;
            }
            Component mountMessage = Component.translatable("mount.onboard", DismountBroomKey.DISMOUNT_KEY.getTranslatedKeyMessage());
            this.minecraft.gui.setOverlayMessage(mountMessage, false);
            this.minecraft.getNarrator().sayNow(mountMessage);
        }
        ci.cancel();
    }
}
