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
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private static final Path ROOT_FOLDER = Paths.get(System.getProperty("user.home")).resolve("touhou_little_maid");
    private static final Path LOCAL_MUSIC_LIST_FILE = ROOT_FOLDER.resolve("music_list");
    private static final ResourceLocation MUSIC_JSON = new ResourceLocation(TouhouLittleMaid.MOD_ID, "music.json");
    private static final ThreadPoolExecutor POOL = new ThreadPoolExecutor(1, 1, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
    private static final MusicJsonInfo DEFAULT_INFO = MusicJsonInfo.getDefaultInstance();
    public static final WebApi NET_EASE_WEB_API = new NetEaseMusic().getApi();
    public static CopyOnWriteArrayList<NetEaseMusicList> MUSIC_LIST_GROUP = Lists.newCopyOnWriteArrayList();

    public static void loadMusicList() {
        MUSIC_LIST_GROUP.clear();
        checkLocalFile();
        try {
            List<IResource> resourceList = Minecraft.getMinecraft().getResourceManager().getAllResources(MUSIC_JSON);
            for (IResource resource : resourceList) {
                readSingleJsonFile(resource);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkLocalFile() {
        // 判定文件夹是否存在
        if (!ROOT_FOLDER.toFile().isDirectory()) {
            try {
                Files.createDirectories(ROOT_FOLDER);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                return;
            }
        }

        // 检查文件是否存在
        if (!LOCAL_MUSIC_LIST_FILE.toFile().isFile()) {
            try {
                LOCAL_MUSIC_LIST_FILE.toFile().createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            try {
                List<String> lists = FileUtils.readLines(LOCAL_MUSIC_LIST_FILE.toFile(), StandardCharsets.UTF_8);
                for (String id : lists) {
                    long i = Long.parseUnsignedLong(id);
                    insertMusicListInfo(i, DEFAULT_INFO);
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    private static void readSingleJsonFile(IResource resource) {
        try (InputStream stream = resource.getInputStream()) {
            List<MusicJsonInfo> infos = GSON.fromJson(IOUtils.toString(stream, StandardCharsets.UTF_8),
                    new TypeToken<List<MusicJsonInfo>>() {
                    }.getType());
            for (MusicJsonInfo info : infos) {
                for (long id : info.getContent().getPlayList()) {
                    insertMusicListInfo(id, info);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean insertMusicListInfo(long id, MusicJsonInfo info) {
        for (NetEaseMusicList list : MUSIC_LIST_GROUP) {
            if (id == list.getListId()) {
                return false;
            }
        }
        POOL.submit(() -> {
            try {
                TouhouLittleMaid.LOGGER.info("Obtaining {} NetEase cloud music playlist information", id);
                String rep = NET_EASE_WEB_API.list(id);
                NetEaseMusicList neteaseMusicList = GSON.fromJson(rep, NetEaseMusicList.class);
                if (neteaseMusicList.getCode() != 200) {
                    TouhouLittleMaid.LOGGER.error(rep);
                } else {
                    neteaseMusicList.setMusicJsonInfo(info);
                    neteaseMusicList.getPlayList().deco();
                    neteaseMusicList.setListId(id);
                    if (neteaseMusicList.getPlayList().getTrackCount() > 0) {
                        MUSIC_LIST_GROUP.add(neteaseMusicList);
                        TouhouLittleMaid.LOGGER.info("{} NetEase cloud music information obtained successfully", id);
                    } else {
                        TouhouLittleMaid.LOGGER.info("{} Music list track number is 0", id);
                    }
                }
                // 防止高频访问，导致被网易云音乐屏蔽
                // 随机 sleep 15 ~ 30 秒
                Thread.sleep((15 + Math.round(Math.random() * 15)) * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    public static boolean addSingleList(long id) {
        if (insertMusicListInfo(id, DEFAULT_INFO)) {
            try {
                List<String> lines = FileUtils.readLines(LOCAL_MUSIC_LIST_FILE.toFile(), StandardCharsets.UTF_8);
                lines.add(String.valueOf(id));
                FileUtils.writeLines(LOCAL_MUSIC_LIST_FILE.toFile(), StandardCharsets.UTF_8.toString(), lines);
                return true;
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        return false;
    }

    public static boolean removeSingleList(long id) {
        try {
            List<String> lines = FileUtils.readLines(LOCAL_MUSIC_LIST_FILE.toFile(), StandardCharsets.UTF_8);
            if (lines.remove(String.valueOf(id))) {
                MUSIC_LIST_GROUP.removeIf(netEaseMusicList -> netEaseMusicList.getListId() == id);
                FileUtils.writeLines(LOCAL_MUSIC_LIST_FILE.toFile(), StandardCharsets.UTF_8.toString(), lines);
                return true;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static long getTaskCount() {
        return POOL.getTaskCount();
    }

    public static long getCompletedTaskCount() {
        return POOL.getCompletedTaskCount();
    }
}
