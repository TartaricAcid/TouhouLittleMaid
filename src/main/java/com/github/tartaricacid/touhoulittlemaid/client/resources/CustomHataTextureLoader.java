package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.HataTextureManager;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
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
    private static final Path HATA_FOLDER = Paths.get(Minecraft.getMinecraft().gameDir.getPath(), "config", TouhouLittleMaid.MOD_ID, "hata_texture");
    private static final String ACCEPTED_HATA_TEXTURE_SUFFIX = ".png";
    private static HataTextureManager HATA_TEXTURE_MANAGER = ClientProxy.HATA_TEXTURE_MANAGER;

    /**
     * 解压默认的指物旗图片
     */
    private static void unzipHataTexture(@Nonnull URL url) {
        File[] files = HATA_FOLDER.toFile().listFiles((dir, name) -> name.endsWith(ACCEPTED_HATA_TEXTURE_SUFFIX));
        boolean dirNotExist = !Files.isDirectory(HATA_FOLDER);
        boolean dirIsEmpty = files != null && files.length < 1;
        boolean shouldCopyDir = dirNotExist || dirIsEmpty;
        if (shouldCopyDir) {
            try {
                FileUtils.copyDirectory(new File(url.toURI()), new File(HATA_FOLDER.toUri()));
            } catch (IOException | URISyntaxException e) {
                TouhouLittleMaid.LOGGER.error(e);
            }
        }
    }

    /**
     * 重载指物旗图片
     */
    public static void onHataTextureReload(@Nonnull URL url) {
        HATA_TEXTURE_MANAGER.clear();
        HATA_NAME_MAP.clear();
        unzipHataTexture(url);
        File[] files = HATA_FOLDER.toFile().listFiles((dir, name) -> name.endsWith(ACCEPTED_HATA_TEXTURE_SUFFIX));
        if (files == null) {
            return;
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
