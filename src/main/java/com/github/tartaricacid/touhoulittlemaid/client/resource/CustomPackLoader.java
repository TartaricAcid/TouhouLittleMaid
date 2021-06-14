package com.github.tartaricacid.touhoulittlemaid.client.resource;

import com.github.tartaricacid.touhoulittlemaid.client.animation.CustomJsAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.BedrockModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.ZipPackTexture;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.ChairModels;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.MaidModels;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ClientLanguageMap;
import net.minecraft.client.resources.Language;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.LanguageMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
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

import static com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid.LOGGER;

@OnlyIn(Dist.CLIENT)
public class CustomPackLoader {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer()).create();
    public static final MaidModels MAID_MODEL = MaidModels.getInstance();
    public static final ChairModels CHAIR_MODEL = ChairModels.getInstance();
    private static final String COMMENT_SYMBOL = "#";
    private static final String LEGACY_BEDROCK_VERSION = "1.10.0";
    private static final String BEDROCK_VERSION = "1.12.0";
    private static final Marker MARKER = MarkerManager.getMarker("CustomPackLoader");
    private static final Pattern DOMAIN = Pattern.compile("^assets/([\\w.]+)/$");
    private static final Path PACK_FOLDER = Paths.get(Minecraft.getInstance().gameDirectory.toURI()).resolve("tlm_custom_pack");

    public static void reloadPacks() {
        CustomJsAnimationManger.clearAll();
        MAID_MODEL.clearAll();
        CHAIR_MODEL.clearAll();
        // 重载数据
        loadPacks();
    }

    private static void loadPacks() {
        File packFolder = PACK_FOLDER.toFile();
        if (!packFolder.isDirectory()) {
            try {
                Files.createDirectories(packFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        File[] files = packFolder.listFiles(((dir, name) -> name.endsWith(".zip")));
        if (files == null) {
            return;
        }
        for (File file : files) {
            readModelFromZipFile(file);
        }
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
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loading...");
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
                Minecraft.getInstance().textureManager.register(pack.getIcon(),
                        new ZipPackTexture(zipFile.getName(), pack.getIcon()));
                // 获取图标的长宽

            }
            for (MaidModelInfo maidModelItem : pack.getModelList()) {
                // 尝试加载模型
                BedrockModel<EntityMaid> modelJson = loadMaidModel(zipFile, maidModelItem.getModel());
                // 加载贴图
                Minecraft.getInstance().textureManager.register(maidModelItem.getTexture(),
                        new ZipPackTexture(zipFile.getName(), maidModelItem.getTexture()));
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
                    LOGGER.debug(MARKER, "Loaded model: {}", maidModelItem.getModel());
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
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    private static void putMaidEasterEggData(MaidModelInfo maidModelItem, BedrockModel<EntityMaid> modelJson, List<Object> animations) {
        MaidModelInfo.EasterEgg easterEgg = maidModelItem.getEasterEgg();
        MaidModels.ModelData data = new MaidModels.ModelData(modelJson, maidModelItem, animations);
        if (easterEgg.isEncrypt()) {
            MAID_MODEL.putEasterEggEncryptTagModel(easterEgg.getTag(), data);
        } else {
            MAID_MODEL.putEasterEggNormalTagModel(easterEgg.getTag(), data);
        }
    }

    private static void putMaidModelData(MaidModelInfo maidModelItem, BedrockModel<EntityMaid> modelJson, List<Object> animations) {
        String id = maidModelItem.getModelId().toString();
        // 如果加载的模型不为空
        MAID_MODEL.putModel(id, modelJson);
        MAID_MODEL.putInfo(id, maidModelItem);
        if (animations != null && animations.size() > 0) {
            MAID_MODEL.putAnimation(id, animations);
        }
    }

    private static void loadChairModelPack(ZipFile zipFile, String domain) {
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loading...");
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
                Minecraft.getInstance().textureManager.register(pack.getIcon(),
                        new ZipPackTexture(zipFile.getName(), pack.getIcon()));
            }
            for (ChairModelInfo chairModelItem : pack.getModelList()) {
                // 尝试加载模型
                BedrockModel<EntityChair> modelJson = loadChairModel(zipFile, chairModelItem.getModel());
                // 加载贴图
                Minecraft.getInstance().textureManager.register(chairModelItem.getTexture(),
                        new ZipPackTexture(zipFile.getName(), chairModelItem.getTexture()));
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
                    LOGGER.debug(MARKER, "Loaded model: {}", chairModelItem.getModel());
                }
            }
            CHAIR_MODEL.addPack(pack);
        } catch (IOException ignore) {
            // 忽略错误，因为资源域很多
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    @Nullable
    public static BedrockModel<EntityMaid> loadMaidModel(ZipFile zipFile, ResourceLocation modelLocation) {
        String path = String.format("assets/%s/%s", modelLocation.getNamespace(), modelLocation.getPath());
        ZipEntry entry = zipFile.getEntry(path);
        if (entry == null) {
            return null;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            BedrockModelPOJO pojo = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class);
            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (!pojo.getFormatVersion().equals(LEGACY_BEDROCK_VERSION)) {
                LOGGER.warn(MARKER, "{} model version is not 1.10.0", modelLocation);
                // TODO: 2020/9/1 添加对高版本基岩版模型的兼容
                return null;
            }
            // 如果 model 字段不为空
            if (pojo.getGeometryModel() != null) {
                return new BedrockModel<>(pojo);
            } else {
                // 否则日志给出提示
                LOGGER.warn(MARKER, "{} model file don't have model field", modelLocation);
            }
        } catch (IOException ioe) {
            // 可能用来判定错误，打印下
            LOGGER.warn(MARKER, "Failed to load model: {}", modelLocation);
            ioe.printStackTrace();
        }
        // 如果前面出了错，返回 Null
        return null;
    }

    @Nullable
    public static BedrockModel<EntityChair> loadChairModel(ZipFile zipFile, ResourceLocation modelLocation) {
        String path = String.format("assets/%s/%s", modelLocation.getNamespace(), modelLocation.getPath());
        ZipEntry entry = zipFile.getEntry(path);
        if (entry == null) {
            return null;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            BedrockModelPOJO pojo = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class);
            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (!pojo.getFormatVersion().equals(LEGACY_BEDROCK_VERSION)) {
                LOGGER.warn(MARKER, "{} model version is not 1.10.0", modelLocation);
                // TODO: 2020/9/1 添加对高版本基岩版模型的兼容
                return null;
            }
            // 如果 model 字段不为空
            if (pojo.getGeometryModel() != null) {
                return new BedrockModel<>(pojo);
            } else {
                // 否则日志给出提示
                LOGGER.warn(MARKER, "{} model file don't have model field", modelLocation);
            }
        } catch (IOException ioe) {
            // 可能用来判定错误，打印下
            LOGGER.warn(MARKER, "Failed to load model: {}", modelLocation);
            ioe.printStackTrace();
        }
        // 如果前面出了错，返回 Null
        return null;
    }

    private static void readLanguageFile(ZipFile zipFile, String namespace) throws IOException {
        Language language = Minecraft.getInstance().getLanguageManager().getSelected();

        Map<String, String> oldLangData = LanguageMap.getInstance().getLanguageData();
        Map<String, String> newLangData = Maps.newHashMap();

        String defaultLangPath = String.format("assets/%s/lang/en_us.lang", namespace);
        String currentLangPath = String.format("assets/%s/lang/%s.lang", namespace, language.getCode());
        getLanguageMap(zipFile, newLangData, defaultLangPath);
        getLanguageMap(zipFile, newLangData, currentLangPath);

        newLangData.putAll(oldLangData);
        ((ClientLanguageMap) LanguageMap.getInstance()).storage = newLangData;
    }

    private static void getLanguageMap(ZipFile zipFile, Map<String, String> langData, String defaultLangPath) throws IOException {
        ZipEntry entry = zipFile.getEntry(defaultLangPath);
        if (entry == null) {
            return;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            List<String> lines = IOUtils.readLines(stream, StandardCharsets.UTF_8);
            for (String s : lines) {
                if (!s.startsWith(COMMENT_SYMBOL)) {
                    String[] map = s.split("=", 2);
                    if (map.length == 2) {
                        langData.put(map[0], map[1]);
                    }
                }
            }
        }
    }
}
