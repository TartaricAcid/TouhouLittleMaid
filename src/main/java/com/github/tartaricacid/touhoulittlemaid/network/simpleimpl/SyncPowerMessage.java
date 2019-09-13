package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.capability.CapabilityPowerHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 * @date 2019/8/28 17:27
 **/
public class SyncPowerMessage implements IMessage {
    private float power;

    public SyncPowerMessage() {
    }

    public SyncPowerMessage(float power) {
        this.power = power;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.power = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeFloat(power);
    }

    public float getPower() {
        return power;
    }

    public static class Handler implements IMessageHandler<SyncPowerMessage, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(SyncPowerMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    EntityPlayer player = Minecraft.getMinecraft().player;
                    if (player == null) {
                        return;
                    }
                    PowerHandler power = player.getCapability(CapabilityPowerHandler.POWER_CAP, null);
                    if (power != null) {
                        power.set(message.getPower());
                    }
                });
            }
            return null;
        }
    }
}
