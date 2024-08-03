package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BeaconAbsorbMessage {
    private final float x;
    private final float y;
    private final float z;

    public BeaconAbsorbMessage(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public static void encode(BeaconAbsorbMessage message, FriendlyByteBuf buf) {
        buf.writeFloat(message.x);
        buf.writeFloat(message.y);
        buf.writeFloat(message.z);
    }

    public static BeaconAbsorbMessage decode(FriendlyByteBuf buf) {
        return new BeaconAbsorbMessage(buf.readFloat(), buf.readFloat(), buf.readFloat());
    }

    public static void handle(BeaconAbsorbMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> spawnParticle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void spawnParticle(BeaconAbsorbMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            EntityPowerPoint.spawnExplosionParticle(mc.level, message.x, message.y, message.z, mc.level.random);
        }
    }
}
