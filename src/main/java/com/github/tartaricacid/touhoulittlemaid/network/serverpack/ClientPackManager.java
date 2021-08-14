package com.github.tartaricacid.touhoulittlemaid.network.serverpack;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.CustomJsAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockVersion;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CustomModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.ServerPackTexture;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.ModelData;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.EasterEgg;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
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

import static com.github.tartaricacid.touhoulittlemaid.client.resources.CustomResourcesLoader.*;
import static com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy.GSON;

@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public final class ClientPackManager {
    public static final List<Long> CRC32_LIST_FROM_SERVER = Lists.newArrayList();

    private static final Pattern DOMAIN = Pattern.compile("^assets/([\\w.]+)/$");
    private static final Path ROOT_FOLDER = Paths.get(System.getProperty("user.home")).resolve("touhou_little_maid").resolve("server_pack");
    private static final Map<Long, File> CRC32_FILE_MAP = Maps.newHashMap();
    private static final Logger LOGGER = TouhouLittleMaid.LOGGER;
    private static final Marker MARKER = MarkerManager.getMarker("ClientPackManager");

    public static void initCrc32Info() {
        CRC32_FILE_MAP.clear();
        File packFolder = ROOT_FOLDER.toFile();
        if (!packFolder.isDirectory()) {
            try {
                Files.createDirectories(packFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        File[] files = packFolder.listFiles();
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

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEnterClientWorld(PlayerEvent.PlayerLoggedInEvent event) {
        if (!CRC32_LIST_FROM_SERVER.isEmpty()) {
            CRC32_LIST_FROM_SERVER.clear();
            CustomResourcesLoader.reloadResources();
        }
    }

    public static File storePackFile(byte[] data, long crc32) throws IOException {
        File file = ROOT_FOLDER.resolve(String.valueOf(crc32)).toFile();
        FileUtils.writeByteArrayToFile(file, data);
        return file;
    }

    public static void readModelFromZipFile(File file) {
        try (ZipFile zipFile = new ZipFile(file)) {
            Enumeration<? extends ZipEntry> iteration = zipFile.entries();
            while (iteration.hasMoreElements()) {
                Matcher matcher = DOMAIN.matcher(iteration.nextElement().getName());
                if (matcher.find()) {
                    String domain = matcher.group(1);
                    loadMaidModelPack(zipFile, domain);
                    loadChairModelPack(zipFile, domain);
                    readLanguageFile(zipFile, domain);
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private static void loadMaidModelPack(ZipFile zipFile, String domain) {
        LOGGER.info(MARKER, "Touhou little maid mod's model is loading...");
        ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", domain, MAID_MODEL.getJsonFileName()));
        if (entry == null) {
            return;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            CustomModelPack<MaidModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<MaidModelInfo>>() {
                    }.getType());
            pack.decorate();
            // 加载图标贴图
            if (pack.getIcon() != null) {
                Minecraft.getMinecraft().renderEngine.loadTexture(pack.getIcon(),
                        new ServerPackTexture(zipFile, pack.getIcon()));
            }
            for (MaidModelInfo maidModelItem : pack.getModelList()) {
                // 尝试加载模型
                EntityModelJson modelJson = loadModel(zipFile, maidModelItem.getModel());
                // 加载贴图
                Minecraft.getMinecraft().renderEngine.loadTexture(maidModelItem.getTexture(),
                        new ServerPackTexture(zipFile, maidModelItem.getTexture()));
                // 加载动画
                @Nullable List<Object> animations = CustomJsAnimationManger.getCustomAnimation(zipFile, maidModelItem);
                if (modelJson != null) {
                    // 加载彩蛋，彩蛋不允许为空
                    if (maidModelItem.getEasterEgg() != null
                            && StringUtils.isNotBlank(maidModelItem.getEasterEgg().getTag())) {
                        putMaidEasterEggData(maidModelItem, modelJson, animations);
                    } else {
                        putMaidModelData(maidModelItem, modelJson, animations);
                    }
                    // 打印日志
                    LOGGER.info(MARKER, "Loaded model: {}", maidModelItem.getModel());
                }
            }
            // 加入包之前，移除那些彩蛋模型
            pack.getModelList().removeIf(maidModelInfo -> maidModelInfo.getEasterEgg() != null);
            MAID_MODEL.addPack(pack);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.info(MARKER, "Touhou little maid mod's model is loaded");
    }

    private static void putMaidEasterEggData(MaidModelInfo maidModelItem, EntityModelJson modelJson, List<Object> animations) {
        EasterEgg easterEgg = maidModelItem.getEasterEgg();
        ModelData data = new ModelData(modelJson, maidModelItem, animations);
        if (easterEgg.isEncrypt()) {
            MAID_MODEL.putEasterEggEncryptTagModel(easterEgg.getTag(), data);
        } else {
            MAID_MODEL.putEasterEggNormalTagModel(easterEgg.getTag(), data);
        }
    }

    private static void putMaidModelData(MaidModelInfo maidModelItem, EntityModelJson modelJson, List<Object> animations) {
        String id = maidModelItem.getModelId().toString();
        // 如果加载的模型不为空
        MAID_MODEL.putModel(id, modelJson);
        MAID_MODEL.putInfo(id, maidModelItem);
        if (animations != null && animations.size() > 0) {
            MAID_MODEL.putAnimation(id, animations);
        }
    }

    private static void loadChairModelPack(ZipFile zipFile, String domain) {
        LOGGER.info(MARKER, "Touhou little maid mod's model is loading...");
        ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", domain, CHAIR_MODEL.getJsonFileName()));
        if (entry == null) {
            return;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            // 将其转换为 pojo 对象
            // 这个 pojo 是二次修饰的过的对象，所以一部分数据异常已经进行了处理或者抛出
            CustomModelPack<ChairModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<ChairModelInfo>>() {
                    }.getType());
            pack.decorate();
            // 加载图标贴图
            if (pack.getIcon() != null) {
                Minecraft.getMinecraft().renderEngine.loadTexture(pack.getIcon(),
                        new ServerPackTexture(zipFile, pack.getIcon()));
            }
            for (ChairModelInfo chairModelItem : pack.getModelList()) {
                // 尝试加载模型
                EntityModelJson modelJson = loadModel(zipFile, chairModelItem.getModel());
                // 加载贴图
                Minecraft.getMinecraft().renderEngine.loadTexture(chairModelItem.getTexture(),
                        new ServerPackTexture(zipFile, chairModelItem.getTexture()));
                // 加载动画
                @Nullable List<Object> animations = CustomJsAnimationManger.getCustomAnimation(zipFile, chairModelItem);
                if (modelJson != null) {
                    String id = chairModelItem.getModelId().toString();
                    // 如果加载的模型不为空
                    CHAIR_MODEL.putModel(id, modelJson);
                    CHAIR_MODEL.putInfo(id, chairModelItem);
                    if (animations != null && animations.size() > 0) {
                        CHAIR_MODEL.putAnimation(id, animations);
                    }
                    // 打印日志
                    LOGGER.info(MARKER, "Loaded model: {}", chairModelItem.getModel());
                }
            }
            CHAIR_MODEL.addPack(pack);
        } catch (IOException ignore) {
            // 忽略错误，因为资源域很多
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.info(MARKER, "Touhou little maid mod's model is loaded");
    }

    @Nullable
    public static EntityModelJson loadModel(ZipFile zipFile, ResourceLocation modelLocation) {
        String path = String.format("assets/%s/%s", modelLocation.getNamespace(), modelLocation.getPath());
        ZipEntry entry = zipFile.getEntry(path);
        if (entry == null) {
            return null;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            CustomModelPOJO pojo = CommonProxy.GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), CustomModelPOJO.class);
            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals(BedrockVersion.LEGACY.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModel() != null) {
                    return new EntityModelJson(pojo, BedrockVersion.LEGACY);
                } else {
                    // 否则日志给出提示
                    LOGGER.warn(MARKER, "{} model file don't have model field", modelLocation);
                }
            }

            // 判定是不是 1.12.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals(BedrockVersion.NEW.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelNew() != null) {
                    return new EntityModelJson(pojo, BedrockVersion.NEW);
                } else {
                    // 否则日志给出提示
                    LOGGER.warn(MARKER, "{} model file don't have model field", modelLocation);
                    return null;
                }
            }

            LOGGER.warn(MARKER, "{} model version is not 1.10.0 or 1.12.0", modelLocation);
        } catch (IOException ioe) {
            // 可能用来判定错误，打印下
            LOGGER.warn(MARKER, "Failed to load model: {}", modelLocation);
            ioe.printStackTrace();
        }
        // 如果前面出了错，返回 Null
        return null;
    }

    private static void readLanguageFile(ZipFile zipFile, String domain) throws IOException {
        String english = String.format("assets/%s/lang/en_us.lang", domain);
        String current = String.format("assets/%s/lang/%s.lang", domain, Minecraft.getMinecraft().gameSettings.language);
        ZipEntry englishEntry = zipFile.getEntry(english);
        if (englishEntry != null) {
            I18n.i18nLocale.loadLocaleData(zipFile.getInputStream(englishEntry));
        }
        if (!current.equals(english)) {
            ZipEntry currentEntry = zipFile.getEntry(current);
            if (currentEntry != null) {
                I18n.i18nLocale.loadLocaleData(zipFile.getInputStream(currentEntry));
            }
        }
    }

    public static Map<Long, File> getCrc32FileMap() {
        return CRC32_FILE_MAP;
    }
}
