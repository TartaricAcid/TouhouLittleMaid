package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.HataTextureManager;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.github.tartaricacid.touhoulittlemaid.util.GetJarResources;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author TartaricAcid
 * @date 2019/8/17 16:15
 **/
@SideOnly(Side.CLIENT)
public class CustomHataTextureLoader {
    private static final Map<Long, Integer> HATA_NAME_MAP = ClientProxy.HATA_NAME_MAP;
    private static final Path CONFIG_HATA_FOLDER = Paths.get(Minecraft.getMinecraft().gameDir.getPath(), "config", TouhouLittleMaid.MOD_ID, "hata_texture");
    private static final String JAR_HATA_FOLDER = "/assets/touhou_little_maid/hata_texture";
    private static final String ACCEPTED_HATA_TEXTURE_SUFFIX = ".png";
    private static HataTextureManager HATA_TEXTURE_MANAGER = ClientProxy.HATA_TEXTURE_MANAGER;

    /**
     * 解压默认的指物旗图片
     */
    private static void unzipHataTexture() {
        File[] files = CONFIG_HATA_FOLDER.toFile().listFiles((dir, name) -> name.endsWith(ACCEPTED_HATA_TEXTURE_SUFFIX));
        boolean dirNotExist = !Files.isDirectory(CONFIG_HATA_FOLDER);
        boolean dirIsEmpty = files == null || files.length < 1;
        boolean shouldCopyDir = dirNotExist || dirIsEmpty;
        if (shouldCopyDir) {
            GetJarResources.copyTouhouLittleMaidFolder(JAR_HATA_FOLDER, CONFIG_HATA_FOLDER);
        }
    }

    /**
     * 重载指物旗图片
     */
    public static void onHataTextureReload() {
        HATA_TEXTURE_MANAGER.clear();
        HATA_NAME_MAP.clear();
        unzipHataTexture();
        File[] files = CONFIG_HATA_FOLDER.toFile().listFiles((dir, name) -> name.endsWith(ACCEPTED_HATA_TEXTURE_SUFFIX));
        if (files == null || files.length < 1) {
            throw new NullPointerException();
        }
        for (File file : files) {
            try {
                HATA_NAME_MAP.put(FileUtils.checksumCRC32(file), HATA_TEXTURE_MANAGER.loadTexture(file.toString()));
            } catch (IOException e) {
                TouhouLittleMaid.LOGGER.error(e);
                return;
            }
        }
        TouhouLittleMaid.LOGGER.info("Loaded {} Hata Textures", HATA_NAME_MAP.size());
    }
}
