package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BeaconAbsorbMessage implements IMessage {
    private float x;
    private float y;
    private float z;

    public BeaconAbsorbMessage() {}

    public BeaconAbsorbMessage(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readFloat();
        this.y = buf.readFloat();
        this.z = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(x);
        buf.writeFloat(y);
        buf.writeFloat(z);
    }

    public static class Handler implements IMessageHandler<BeaconAbsorbMessage, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(BeaconAbsorbMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    EntityPlayer player = Minecraft.getMinecraft().player;
                    if (player == null || player.world == null) {
                        return;
                    }
                    EntityPowerPoint.spawnExplosionParticle(player.world, message.x, message.y, message.z, player.world.rand);
                });
            }
            return null;
        }
    }
}
