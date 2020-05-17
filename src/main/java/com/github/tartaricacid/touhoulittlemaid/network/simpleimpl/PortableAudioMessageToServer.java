package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPortableAudio;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.github.tartaricacid.touhoulittlemaid.util.DelayedTask;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;
import java.util.regex.Pattern;

public class PortableAudioMessageToServer implements IMessage {
    private UUID uuid;
    private int songId;

    public PortableAudioMessageToServer() {
    }

    public PortableAudioMessageToServer(UUID uuid, int songId) {
        this.uuid = uuid;
        this.songId = songId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        uuid = new UUID(buf.readLong(), buf.readLong());
        songId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(uuid.getMostSignificantBits());
        buf.writeLong(uuid.getLeastSignificantBits());
        buf.writeInt(songId);
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
                    // 特殊 id，直接停止播放
                    if (message.songId == -1) {
                        return;
                    }
                    // 一秒钟后执行新歌曲装载发包
                    DelayedTask.add(() -> {
                        audio.setPlaying(true);
                        audio.setSongId(message.songId);
                        PortableAudioMessageToClient msg = new PortableAudioMessageToClient(audio.getEntityId(), message.songId);
                        NetworkRegistry.TargetPoint point = new NetworkRegistry.TargetPoint(
                                audio.world.provider.getDimension(),
                                audio.posX, audio.posY, audio.posZ, 128);
                        CommonProxy.INSTANCE.sendToAllAround(msg, point);
                    }, 20);
                }
            });
        }
    }
}
