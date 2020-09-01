package com.github.tartaricacid.touhoulittlemaid.network.serverpack;

import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SyncClientPackMessage implements IMessage {
    private final List<Long> data = Lists.newArrayList();

    public SyncClientPackMessage() {
    }

    public SyncClientPackMessage(Map<Long, File> rawData) {
        data.addAll(rawData.keySet());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data.clear();
        int length = buf.readInt();
        for (int i = 0; i < length; i++) {
            data.add(buf.readLong());
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(data.size());
        for (long crc32 : data) {
            buf.writeLong(crc32);
        }
    }

    public static class Handler implements IMessageHandler<SyncClientPackMessage, IMessage> {
        @Override
        public IMessage onMessage(SyncClientPackMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    ClientPackManager.initCrc32Info();
                    ClientPackManager.CRC32_LIST_FROM_SERVER.clear();
                    CustomResourcesLoader.reloadResources();

                    Set<Long> localFileCrc = ClientPackManager.getCrc32FileMap().keySet();
                    List<Long> need = Lists.newArrayList();
                    for (long crc32 : message.data) {
                        ClientPackManager.CRC32_LIST_FROM_SERVER.add(crc32);
                        if (!localFileCrc.contains(crc32)) {
                            need.add(crc32);
                        } else {
                            ClientPackManager.readModelFromZipFile(ClientPackManager.getCrc32FileMap().get(crc32));
                        }
                    }
                    if (need.size() > 0) {
                        CommonProxy.INSTANCE.sendToServer(new GetServerPackMessage(need));
                        ITextComponent component = new TextComponentTranslation("message.touhou_little_maid.server.pack_sync");
                        Minecraft.getMinecraft().player.sendStatusMessage(component, false);
                    }
                });
            }
            return null;
        }
    }
}
