package com.github.tartaricacid.touhoulittlemaid.client.audio.music;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.neteasemusic.NetEaseMusic;
import com.github.tartaricacid.touhoulittlemaid.api.neteasemusic.WebApi;
import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy.GSON;

/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public final class MusicManger {
    private static final ResourceLocation MUSIC_JSON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "music.json");
    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(1, 1, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    public static CopyOnWriteArrayList<NetEaseMusicList> MUSIC_LIST_GROUP = Lists.newCopyOnWriteArrayList();

    public static void loadMusicList() {
        MUSIC_LIST_GROUP.clear();
        WebApi netEaseWebApi = new NetEaseMusic().getApi();
        try {
            List<IResource> resourceList = Minecraft.getMinecraft().getResourceManager().getAllResources(MUSIC_JSON);
            for (IResource resource : resourceList) {
                readSingleJsonFile(netEaseWebApi, resource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readSingleJsonFile(WebApi netEaseWebApi, IResource resource) {
        try (InputStream stream = resource.getInputStream()) {
            List<MusicJsonInfo> infos = GSON.fromJson(IOUtils.toString(stream, StandardCharsets.UTF_8),
                    new TypeToken<List<MusicJsonInfo>>() {
                    }.getType());
            for (MusicJsonInfo info : infos) {
                for (long id : info.getContent().getPlayList()) {
                    for (NetEaseMusicList list : MUSIC_LIST_GROUP) {
                        if (id == list.getListId()) {
                            break;
                        }
                    }
                    insertMusicListInfo(netEaseWebApi, id, info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertMusicListInfo(WebApi netEaseWebApi, long id, MusicJsonInfo info) {
        POOL.submit(() -> {
            try {
                TouhouLittleMaid.LOGGER.info("Obtaining {} NetEase cloud music playlist information", id);
                String rep = netEaseWebApi.list(id);
                NetEaseMusicList neteaseMusicList = GSON.fromJson(rep, NetEaseMusicList.class);
                if (neteaseMusicList.getCode() != 200) {
                    TouhouLittleMaid.LOGGER.error(rep);
                } else {
                    neteaseMusicList.setMusicJsonInfo(info);
                    neteaseMusicList.getPlayList().deco();
                    neteaseMusicList.setListId(id);
                    MUSIC_LIST_GROUP.add(neteaseMusicList);
                    TouhouLittleMaid.LOGGER.info("{} NetEase cloud music information obtained successfully", id);
                }
                // 防止高频访问，导致被网易云音乐屏蔽
                // 随机 sleep 15 ~ 30 秒
                Thread.sleep((15 + Math.round(Math.random() * 15)) * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static long getTaskCount() {
        return POOL.getTaskCount();
    }

    public static long getCompletedTaskCount() {
        return POOL.getCompletedTaskCount();
    }
}
