package com.github.tartaricacid.touhoulittlemaid.draw;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.DrawConfigGui;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.draw.DrawManger.sortModelDrawInfo;

public class SendToClientDrawMessage implements IMessage {
    private List<DrawManger.ModelDrawInfo> modelDrawInfoList = Lists.newArrayList();

    public SendToClientDrawMessage() {
    }

    public SendToClientDrawMessage(HashMap<String, Integer> modelToWeight, HashMap<Integer, List<String>> levelToModel,
                                   List<CustomModelPack<MaidModelInfo>> maidModelPackInfoList) {
        for (int index : levelToModel.keySet()) {
            DrawManger.Level level = DrawManger.Level.getLevelByIndex(index);
            for (String modelId : levelToModel.get(index)) {
                int weight = modelToWeight.get(modelId);
                DrawManger.ModelDrawInfo modelDrawInfo = new DrawManger.ModelDrawInfo(modelId, level, weight);
                modelDrawInfoList.add(modelDrawInfo);
            }
            for (CustomModelPack<MaidModelInfo> info : maidModelPackInfoList) {
                for (MaidModelInfo modelInfo : info.getModelList()) {
                    String modelId = modelInfo.getModelId().toString();
                    if (!modelToWeight.containsKey(modelId)) {
                        DrawManger.ModelDrawInfo modelDrawInfo = new DrawManger.ModelDrawInfo(modelId, DrawManger.Level.N, 0);
                        modelDrawInfoList.add(modelDrawInfo);
                    }
                }
            }
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        modelDrawInfoList.clear();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            String modelId = ByteBufUtils.readUTF8String(buf);
            byte level = buf.readByte();
            int weight = buf.readInt();
            modelDrawInfoList.add(new DrawManger.ModelDrawInfo(modelId, DrawManger.Level.getLevelByIndex(level), weight));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(modelDrawInfoList.size());
        for (DrawManger.ModelDrawInfo info : modelDrawInfoList) {
            ByteBufUtils.writeUTF8String(buf, info.getModelId());
            buf.writeByte(info.getLevel().getIndex());
            buf.writeInt(info.getWeight());
        }
    }

    public static class Handler implements IMessageHandler<SendToClientDrawMessage, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(SendToClientDrawMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> {
                    // 检查客户端列表与服务端列表是否同步
                    for (DrawManger.ModelDrawInfo info : message.modelDrawInfoList) {
                        if (!CustomResourcesLoader.MAID_MODEL.containsInfo(info.getModelId())) {
                            ITextComponent component = new TextComponentTranslation("message.touhou_little_maid.draw.pack_not_sync");
                            Minecraft.getMinecraft().player.sendMessage(component);
                            return;
                        }
                    }
                    Minecraft.getMinecraft().displayGuiScreen(new DrawConfigGui(sortModelDrawInfo(message.modelDrawInfoList)));
                });
            }
            return null;
        }
    }
}
