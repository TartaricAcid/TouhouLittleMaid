package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.client.audio.music.NetEaseMusicList;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPortableAudio;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

public class PortableAudioMessageToServer implements IMessage {
    private UUID uuid;
    private List<EntityPortableAudio.MusicListData> musicListData = Lists.newArrayList();
    private int listSize;
    private int index;

    public PortableAudioMessageToServer() {
    }

    public PortableAudioMessageToServer(UUID uuid, NetEaseMusicList.PlayList playList, int index) {
        this.uuid = uuid;
        this.listSize = playList.getTracks().size();
        for (NetEaseMusicList.Track track : playList.getTracks()) {
            this.musicListData.add(new EntityPortableAudio.MusicListData(track.getId(), track.getDurationInTicks()));
        }
        this.index = index;
    }

    private PortableAudioMessageToServer(UUID uuid) {
        this.uuid = uuid;
        this.listSize = 0;
        this.index = -1;
    }

    public static PortableAudioMessageToServer getStopMessage(UUID uuid) {
        return new PortableAudioMessageToServer(uuid);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = new UUID(buf.readLong(), buf.readLong());
        listSize = buf.readInt();
        for (int i = 0; i < listSize; i++) {
            long musicId = buf.readLong();
            int nextTime = buf.readInt();
            musicListData.add(new EntityPortableAudio.MusicListData(musicId, nextTime));
        }
        index = Math.min(buf.readInt(), musicListData.size());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        buf.writeInt(listSize);
        for (EntityPortableAudio.MusicListData data : musicListData) {
            buf.writeLong(data.getMusicId());
            buf.writeInt(data.getNextTime());
        }
        buf.writeInt(Math.min(index, musicListData.size()));
    }

    public static class Handler implements IMessageHandler<PortableAudioMessageToServer, IMessage> {
        @Override
        public IMessage onMessage(PortableAudioMessageToServer message, MessageContext ctx) {
            if (ctx.side == Side.SERVER) {
                String playerName = ctx.getServerHandler().player.getDisplayNameString();
                if (Pattern.matches(GeneralConfig.MUSIC_CONFIG.playerNameReg, playerName)) {
                    playMusic(message, ctx);
                }
            }
            return null;
        }

        private void playMusic(PortableAudioMessageToServer message, MessageContext ctx) {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> {
                Entity entity = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityFromUuid(message.uuid);
                if (entity instanceof EntityPortableAudio) {
                    EntityPortableAudio audio = (EntityPortableAudio) entity;
                    audio.setPlaying(false);
                    audio.clearAllMusicListData();
                    // 特殊 id，直接停止播放
                    if (message.index == -1) {
                        return;
                    }
                    audio.setMusicListData(message.musicListData);
                    audio.setMusicIndex(message.index);
                }
            });
        }
    }
}
