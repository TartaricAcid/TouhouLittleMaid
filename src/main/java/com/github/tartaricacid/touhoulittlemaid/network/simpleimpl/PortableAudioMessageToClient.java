package com.github.tartaricacid.touhoulittlemaid.network.simpleimpl;

import com.github.tartaricacid.touhoulittlemaid.client.audio.PortableAudioSound;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPortableAudio;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.net.URL;
import java.net.URLConnection;

public class PortableAudioMessageToClient implements IMessage {
    private static final String ERROR_404 = "http://music.163.com/404";

    private int entityId;
    private int songId;

    public PortableAudioMessageToClient() {
    }

    public PortableAudioMessageToClient(int entityId, int songId) {
        this.entityId = entityId;
        this.songId = songId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
        songId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(songId);
    }

    public static class Handler implements IMessageHandler<PortableAudioMessageToClient, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(PortableAudioMessageToClient message, MessageContext ctx) {
            if (ctx.side == Side.CLIENT && GeneralConfig.MUSIC_CONFIG.receiveMusic) {
                try {
                    URL songUrl = new URL(String.format("https://music.163.com/song/media/outer/url?id=%d.mp3", message.songId));
                    URLConnection con = songUrl.openConnection();
                    con.setConnectTimeout(3_000);
                    // 默认超时 5 秒
                    con.setReadTimeout(5_000);
                    // 获取得到 302 跳转后的连接地址
                    final URL realSongUrl = new URL(con.getHeaderField("Location"));
                    FMLClientHandler.instance().getClient().addScheduledTask(() -> {
                        Entity entity = FMLClientHandler.instance().getWorldClient().getEntityByID(message.entityId);
                        if (entity instanceof EntityPortableAudio) {
                            EntityPortableAudio audio = (EntityPortableAudio) entity;
                            if (ERROR_404.equals(realSongUrl.toString())) {
                                Minecraft.getMinecraft().ingameGUI.setOverlayMessage(
                                        I18n.format("info.touhou_little_maid.portable_audio.404"),
                                        false);
                            } else {
                                PortableAudioSound sound = new PortableAudioSound(audio, realSongUrl);
                                Minecraft.getMinecraft().getSoundHandler().playSound(sound);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
