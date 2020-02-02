package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.client.audio.TrolleyAudioSound;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityTrolleyAudio;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.lang3.StringUtils;

/**
 * @author TartaricAcid
 * @date 2020/2/1 22:34
 **/
public class TrolleyAudioSoundMessage implements IMessage {
    private int entityId;
    private String soundName;
    private String recordName;

    public TrolleyAudioSoundMessage() {
    }

    public TrolleyAudioSoundMessage(String recordName, SoundEvent soundEvent, EntityTrolleyAudio trolleyAudio) {
        this.recordName = recordName;
        this.soundName = soundEvent.getSoundName().toString();
        this.entityId = trolleyAudio.getEntityId();
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.recordName = ByteBufUtils.readUTF8String(buf);
        this.soundName = ByteBufUtils.readUTF8String(buf);
        this.entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, this.recordName);
        ByteBufUtils.writeUTF8String(buf, this.soundName);
        buf.writeInt(this.entityId);
    }

    public String getRecordName() {
        return recordName;
    }

    public SoundEvent getSoundEvent() {
        return new SoundEvent(new ResourceLocation(soundName));
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
                        apply(message.recordName, message.getSoundEvent(), (EntityTrolleyAudio) entity);
                    }
                });
            }
            return null;
        }

        private void apply(String recordName, SoundEvent event, EntityTrolleyAudio trolleyAudio) {
            TrolleyAudioSound sound = new TrolleyAudioSound(event, trolleyAudio);
            Minecraft.getMinecraft().getSoundHandler().playSound(sound);
            if (StringUtils.isNotBlank(recordName)) {
                Minecraft.getMinecraft().ingameGUI.setRecordPlayingMessage(I18n.format(recordName));
            }
        }
    }
}

