package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.danmaku.CustomSpellCardEntry;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Map;

/**
 * @author TartaricAcid
 * @date 2019/12/1 16:23
 **/
public class SyncCustomSpellCardData implements IMessage {
    private Map<String, CustomSpellCardEntry> map = Maps.newHashMap();
    private int size;

    public SyncCustomSpellCardData() {
        map.clear();
        map.putAll(CommonProxy.CUSTOM_SPELL_CARD_MAP_SERVER);
        this.size = map.size();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        map.clear();
        this.size = buf.readInt();
        for (int i = 0; i < size; i++) {
            map.put(ByteBufUtils.readUTF8String(buf),
                    new CustomSpellCardEntry(
                            ByteBufUtils.readUTF8String(buf),
                            ByteBufUtils.readUTF8String(buf),
                            ByteBufUtils.readUTF8String(buf),
                            ByteBufUtils.readUTF8String(buf),
                            ByteBufUtils.readUTF8String(buf), null, buf.readInt(),
                            new ResourceLocation(ByteBufUtils.readUTF8String(buf)),
                            new ResourceLocation(ByteBufUtils.readUTF8String(buf))
                    )
            );
        }
    }

    @SuppressWarnings("all")
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.size);
        for (String key : map.keySet()) {
            CustomSpellCardEntry entry = map.get(key);
            ByteBufUtils.writeUTF8String(buf, key);
            ByteBufUtils.writeUTF8String(buf, entry.getId());
            ByteBufUtils.writeUTF8String(buf, entry.getNameKey());
            ByteBufUtils.writeUTF8String(buf, entry.getDescriptionKey());
            ByteBufUtils.writeUTF8String(buf, entry.getAuthor());
            ByteBufUtils.writeUTF8String(buf, entry.getVersion());
            buf.writeInt(entry.getCooldown());
            ByteBufUtils.writeUTF8String(buf, entry.getIcon().toString());
            ByteBufUtils.writeUTF8String(buf, entry.getSnapshoot().toString());
        }
    }

    public static class Handler implements IMessageHandler<SyncCustomSpellCardData, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(SyncCustomSpellCardData message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft.getMinecraft().addScheduledTask(() -> TouhouLittleMaid.PROXY.setSpellCard(message.map));
            }
            return null;
        }
    }
}