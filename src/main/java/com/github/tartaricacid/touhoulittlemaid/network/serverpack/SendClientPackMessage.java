package com.github.tartaricacid.touhoulittlemaid.network.serverpack;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class SendClientPackMessage implements IMessage {
    private long crc32;
    private byte[] data;
    private int length;

    public SendClientPackMessage() {
    }

    public SendClientPackMessage(long crc32, byte[] data, int length) {
        this.crc32 = crc32;
        this.data = data;
        this.length = length;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        crc32 = buf.readLong();
        length = buf.readInt();
        data = new byte[length];
        buf.readBytes(data);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(crc32);
        buf.writeInt(data.length);
        buf.writeBytes(data);
    }

    public static class Handler implements IMessageHandler<SendClientPackMessage, IMessage> {
        @Override
        public IMessage onMessage(SendClientPackMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    try {
                        File file = ClientPackManager.storePackFile(message.data, message.crc32);
                        if (FileUtils.checksumCRC32(file) == message.crc32) {
                            ClientPackManager.readModelFromZipFile(file);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
            return null;
        }
    }
}
