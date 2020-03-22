package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.client.audio.TrolleyAudioSound;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTrolleyAudio;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

/**
 * @author TartaricAcid
 * @date 2020/2/1 22:34
 **/
public class TrolleyAudioSoundMessage implements IMessage {
    private int entityId;
    private String recordId;
    private String recordDisplayName;

    public TrolleyAudioSoundMessage() {
    }

    public TrolleyAudioSoundMessage(String recordDisplayName, ResourceLocation registerName, EntityTrolleyAudio trolleyAudio) {
        this.recordDisplayName = recordDisplayName;
        this.recordId = registerName.toString();
        this.entityId = trolleyAudio.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.recordDisplayName = ByteBufUtils.readUTF8String(buf);
        this.recordId = ByteBufUtils.readUTF8String(buf);
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.recordDisplayName);
        ByteBufUtils.writeUTF8String(buf, this.recordId);
        buf.writeInt(this.entityId);
    }

    public String getRecordDisplayName() {
        return recordDisplayName;
    }

    public String getRecordId() {
        return recordId;
    }

    public int getEntityId() {
        return entityId;
    }

    public static class Handler implements IMessageHandler<TrolleyAudioSoundMessage, IMessage> {
        @Override
        public IMessage onMessage(TrolleyAudioSoundMessage message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                Minecraft mc = Minecraft.getMinecraft();
                mc.addScheduledTask(() -> {
                    Entity entity = mc.world.getEntityByID(message.getEntityId());
                    if (entity instanceof EntityTrolleyAudio) {
                        Item item = Item.getByNameOrId(message.getRecordId());
                        if (item instanceof ItemRecord) {
                            apply(message.recordDisplayName, ((ItemRecord) item).getSound(), (EntityTrolleyAudio) entity);
                        }
                    }
                });
            }
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void apply(String recordName, SoundEvent event, EntityTrolleyAudio trolleyAudio) {
            TrolleyAudioSound sound = new TrolleyAudioSound(event, trolleyAudio);
            Minecraft.getMinecraft().getSoundHandler().playSound(sound);
            if (StringUtils.isNotBlank(recordName)) {
                Minecraft.getMinecraft().ingameGUI.setRecordPlayingMessage(I18n.format(recordName));
            }
        }
    }
}

