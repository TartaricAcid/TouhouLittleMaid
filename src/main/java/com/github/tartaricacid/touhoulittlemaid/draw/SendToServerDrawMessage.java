package com.github.tartaricacid.touhoulittlemaid.draw;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.draw.DrawManger.readDrawCsvFile;
import static com.github.tartaricacid.touhoulittlemaid.draw.DrawManger.writeDrawCsvFile;

public class SendToServerDrawMessage implements IMessage {
    private List<DrawManger.ModelDrawInfo> modelDrawInfoList = Lists.newArrayList();
    private boolean isReload;

    public SendToServerDrawMessage() {
    }

    public SendToServerDrawMessage(List<DrawManger.ModelDrawInfo> modelDrawInfoList, boolean isReload) {
        this.modelDrawInfoList = modelDrawInfoList;
        this.isReload = isReload;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        modelDrawInfoList.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            String modelId = ByteBufUtils.readUTF8String(buf);
            byte level = buf.readByte();
            int weight = buf.readInt();
            DrawManger.ModelDrawInfo modelDrawInfo = new DrawManger.ModelDrawInfo(modelId,
                    DrawManger.Level.getLevelByIndex(level), weight);
            modelDrawInfoList.add(modelDrawInfo);
        }
        isReload = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(modelDrawInfoList.size());
        for (DrawManger.ModelDrawInfo info : modelDrawInfoList) {
            ByteBufUtils.writeUTF8String(buf, info.getModelId());
            buf.writeByte(info.getLevel().getIndex());
            buf.writeInt(info.getWeight());
        }
        buf.writeBoolean(isReload);
    }

    public static class Handler implements IMessageHandler<SendToServerDrawMessage, IMessage> {
        @Override
        public IMessage onMessage(SendToServerDrawMessage message, MessageContext ctx) {
            if (ctx.side == Side.SERVER && ctx.getServerHandler().player.canUseCommand(2, "")) {
                FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                    writeDrawCsvFile(message.modelDrawInfoList);
                    if (message.isReload) {
                        readDrawCsvFile(TouhouLittleMaid.class);
                    }
                });
            }
            return null;
        }
    }
}
