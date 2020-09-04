package com.github.tartaricacid.touhoulittlemaid.network.serverpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy.GSON;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class ServerPackManager {
    private static final Path ROOT = Paths.get("touhou-little-maid-server-pack");
    private static final Pattern DOMAIN = Pattern.compile("^assets/([\\w.]+)/$");
    private static final Map<Long, File> CRC32_FILE_MAP = Maps.newHashMap();
    private static final List<CustomModelPack<MaidModelInfo>> MODEL_PACK_LIST = Lists.newArrayList();

    public static void initCrc32Info() {
        CRC32_FILE_MAP.clear();
        MODEL_PACK_LIST.clear();
        File packFolder = ROOT.toFile();
        if (!packFolder.isDirectory()) {
            try {
                Files.createDirectory(packFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        File[] files = packFolder.listFiles((dir, name) -> name.endsWith("zip"));
        if (files == null) {
            return;
        }
        for (File file : files) {
            try {
                long crc32 = FileUtils.checksumCRC32(file);
                CRC32_FILE_MAP.put(crc32, file);
                readZipPack(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void readZipPack(File file) throws IOException {
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> iteration = zipFile.entries();
            while (iteration.hasMoreElements()) {
                Matcher matcher = DOMAIN.matcher(iteration.nextElement().getName());
                if (matcher.find()) {
                    String domain = matcher.group(1);
                    getZipFileModelPackInfo(zipFile, domain);
                }
            }
        }
    }

    private static void getZipFileModelPackInfo(ZipFile zipFile, String domain) throws IOException {
        ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", domain, "maid_model.json"));
        if (entry == null) {
            return;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            CustomModelPack<MaidModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<MaidModelInfo>>() {
                    }.getType());
            pack.decorate();
            // 加入包之前，移除那些彩蛋模型
            pack.getModelList().removeIf(maidModelInfo -> maidModelInfo.getEasterEgg() != null);
            MODEL_PACK_LIST.add(pack);
        }
    }

    public static Map<Long, File> getCrc32FileMap() {
        return CRC32_FILE_MAP;
    }

    public static List<CustomModelPack<MaidModelInfo>> getModelPackList() {
        return MODEL_PACK_LIST;
    }

    @SubscribeEvent
    public static void onEnterServer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP && !ServerPackManager.getCrc32FileMap().isEmpty()) {
            CommonProxy.INSTANCE.sendTo(new SyncClientPackMessage(ServerPackManager.getCrc32FileMap()), (EntityPlayerMP) event.player);
        }
    }
}
