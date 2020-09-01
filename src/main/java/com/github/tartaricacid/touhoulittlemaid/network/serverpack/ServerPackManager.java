package com.github.tartaricacid.touhoulittlemaid.network.serverpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.SERVER)
public final class ServerPackManager {
    private static final Path ROOT = Paths.get("touhou-little-maid-server-pack");
    private static final Map<Long, File> CRC32_FILE_MAP = Maps.newHashMap();

    public static void initCrc32Info() {
        CRC32_FILE_MAP.clear();
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<Long, File> getCrc32FileMap() {
        return CRC32_FILE_MAP;
    }

    @SubscribeEvent
    public static void onEnterServer(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            CommonProxy.INSTANCE.sendTo(new SyncClientPackMessage(ServerPackManager.getCrc32FileMap()), (EntityPlayerMP) event.player);
        }
    }
}
