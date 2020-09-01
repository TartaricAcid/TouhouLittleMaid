package com.github.tartaricacid.touhoulittlemaid.network.serverpack;

import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.util.DelayedTask;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

public class GetServerPackMessage implements IMessage {
    private List<Long> needList = Lists.newArrayList();

    public GetServerPackMessage() {
    }

    public GetServerPackMessage(List<Long> needList) {
        this.needList = needList;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        needList.clear();
        int length = buf.readInt();
        for (int i = 0; i < length; i++) {
            needList.add(buf.readLong());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(needList.size());
        for (long crc32 : needList) {
            buf.writeLong(crc32);
        }
    }

    public static class Handler implements IMessageHandler<GetServerPackMessage, IMessage> {
        private static void sendPack(GetServerPackMessage message, MessageContext ctx) {
            // 随机间隔，避免并发导致的网络拥塞？
            int delay = (new Random()).nextInt(100) + 50;
            EntityPlayerMP player = ctx.getServerHandler().player;
            for (long crc32 : message.needList) {
                if (ServerPackManager.getCrc32FileMap().containsKey(crc32)) {
                    DelayedTask.add(() -> {
                        if (player != null) {
                            File file = ServerPackManager.getCrc32FileMap().get(crc32);
                            try {
                                byte[] data = FileUtils.readFileToByteArray(file);
                                CommonProxy.INSTANCE.sendTo(new SendClientPackMessage(crc32, data, data.length), player);
                            } catch (IOException ioe) {
                                ioe.printStackTrace();
                            }
                        }
                    }, delay);
                    delay += 100; // 间隔 5 秒发送一次
                }
            }
        }

        @Override
        public IMessage onMessage(GetServerPackMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> sendPack(message, ctx));
            }
            return null;
        }
    }
}
