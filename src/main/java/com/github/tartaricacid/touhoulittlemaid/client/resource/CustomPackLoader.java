package com.github.tartaricacid.touhoulittlemaid.client.resource;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.CustomJsAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockModel;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockVersion;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.BedrockModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CubesItem;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.FilePackTexture;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.ZipPackTexture;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.ChairModels;
import com.github.tartaricacid.touhoulittlemaid.client.resource.models.MaidModels;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.GetJarResources;
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
import java.io.*;
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
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(CubesItem.class, new CubesItem.Deserializer()).create();
    public static final MaidModels MAID_MODELS = MaidModels.getInstance();
    public static final ChairModels CHAIR_MODELS = ChairModels.getInstance();
    private static final String COMMENT_SYMBOL = "#";
    private static final String CUSTOM_PACK_DIR_NAME = "tlm_custom_pack";
    private static final String DEFAULT_PACK_NAME = "touhou_little_maid-1.0.0.zip";
    private static final Marker MARKER = MarkerManager.getMarker("CustomPackLoader");
    private static final Pattern DOMAIN = Pattern.compile("^assets/([\\w.]+)/$");
    private static final Path PACK_FOLDER = Paths.get(Minecraft.getInstance().gameDirectory.toURI()).resolve(CUSTOM_PACK_DIR_NAME);

    public static void reloadPacks() {
        CustomJsAnimationManger.clearAll();
        MAID_MODELS.clearAll();
        CHAIR_MODELS.clearAll();
        initPacks();
    }

    private static void initPacks() {
        File packFolder = PACK_FOLDER.toFile();
        if (!packFolder.isDirectory()) {
            try {
                Files.createDirectories(packFolder.toPath());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        checkDefaultPack();
        loadPacks(packFolder);
    }

    private static void checkDefaultPack() {
        // 不管存不存在，强行覆盖
        String jarDefaultPackPath = String.format("/assets/%s/%s/%s", TouhouLittleMaid.MOD_ID, CUSTOM_PACK_DIR_NAME, DEFAULT_PACK_NAME);
        GetJarResources.copyTouhouLittleMaidFile(jarDefaultPackPath, PACK_FOLDER, DEFAULT_PACK_NAME);
    }

    private static void loadPacks(File packFolder) {
        File[] files = packFolder.listFiles((dir, name) -> true);
        if (files == null) {
            return;
        }
        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".zip")) {
                readModelFromZipFile(file);
            }
            if (file.isDirectory()) {
                readModelFromFolder(file);
            }
        }
    }

    public static void readModelFromFolder(File root) {
        try {
            File[] domainFiles = root.toPath().resolve("assets").toFile().listFiles((dir, name) -> true);
            if (domainFiles == null) {
                return;
            }
            for (File domainDir : domainFiles) {
                if (domainDir.isDirectory()) {
                    Path rootPath = root.toPath();
                    String domain = domainDir.getName();
                    loadMaidModelPack(rootPath, domain);
                    loadChairModelPack(rootPath, domain);
                    readLanguageFile(rootPath, domain);
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
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

    private static void loadMaidModelPack(Path rootPath, String domain) {
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loading...");
        File file = rootPath.resolve("assets").resolve(domain).resolve(MAID_MODELS.getJsonFileName()).toFile();
        if (!file.isFile()) {
            return;
        }
        try (InputStream stream = new FileInputStream(file)) {
            CustomModelPack<MaidModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<MaidModelInfo>>() {
                    }.getType());
            pack.decorate();
            // 加载图标贴图
            if (pack.getIcon() != null) {
                Minecraft.getInstance().textureManager.register(pack.getIcon(),
                        new FilePackTexture(rootPath, pack.getIcon()));
                // 获取图标的长宽

            }
            for (MaidModelInfo maidModelItem : pack.getModelList()) {
                // 尝试加载模型
                BedrockModel<EntityMaid> modelJson = loadMaidModel(rootPath, maidModelItem.getModel());
                // 加载贴图
                Minecraft.getInstance().textureManager.register(maidModelItem.getTexture(),
                        new FilePackTexture(rootPath, maidModelItem.getTexture()));
                // 加载动画
                @Nullable List<Object> animations = CustomJsAnimationManger.getCustomAnimation(rootPath, maidModelItem);
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
            MAID_MODELS.addPack(pack);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    private static void loadMaidModelPack(ZipFile zipFile, String domain) {
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loading...");
        ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", domain, MAID_MODELS.getJsonFileName()));
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
            MAID_MODELS.addPack(pack);
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
            MAID_MODELS.putEasterEggEncryptTagModel(easterEgg.getTag(), data);
        } else {
            MAID_MODELS.putEasterEggNormalTagModel(easterEgg.getTag(), data);
        }
    }

    private static void putMaidModelData(MaidModelInfo maidModelItem, BedrockModel<EntityMaid> modelJson, List<Object> animations) {
        String id = maidModelItem.getModelId().toString();
        // 如果加载的模型不为空
        MAID_MODELS.putModel(id, modelJson);
        MAID_MODELS.putInfo(id, maidModelItem);
        if (animations != null && animations.size() > 0) {
            MAID_MODELS.putAnimation(id, animations);
        }
    }

    private static void loadChairModelPack(Path rootPath, String domain) {
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loading...");
        File file = rootPath.resolve("assets").resolve(domain).resolve(CHAIR_MODELS.getJsonFileName()).toFile();
        if (!file.isFile()) {
            return;
        }
        try (InputStream stream = new FileInputStream(file)) {
            // 将其转换为 pojo 对象
            // 这个 pojo 是二次修饰的过的对象，所以一部分数据异常已经进行了处理或者抛出
            CustomModelPack<ChairModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<ChairModelInfo>>() {
                    }.getType());
            pack.decorate();
            // 加载图标贴图
            if (pack.getIcon() != null) {
                Minecraft.getInstance().textureManager.register(pack.getIcon(),
                        new FilePackTexture(rootPath, pack.getIcon()));
            }
            for (ChairModelInfo chairModelItem : pack.getModelList()) {
                // 尝试加载模型
                BedrockModel<EntityChair> modelJson = loadChairModel(rootPath, chairModelItem.getModel());
                // 加载贴图
                Minecraft.getInstance().textureManager.register(chairModelItem.getTexture(),
                        new FilePackTexture(rootPath, chairModelItem.getTexture()));
                // 加载动画
                @Nullable List<Object> animations = CustomJsAnimationManger.getCustomAnimation(rootPath, chairModelItem);
                if (modelJson != null) {
                    String id = chairModelItem.getModelId().toString();
                    // 如果加载的模型不为空
                    CHAIR_MODELS.putModel(id, modelJson);
                    CHAIR_MODELS.putInfo(id, chairModelItem);
                    if (animations != null && animations.size() > 0) {
                        CHAIR_MODELS.putAnimation(id, animations);
                    }
                    // 打印日志
                    LOGGER.debug(MARKER, "Loaded model: {}", chairModelItem.getModel());
                }
            }
            CHAIR_MODELS.addPack(pack);
        } catch (IOException ignore) {
            // 忽略错误，因为资源域很多
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    private static void loadChairModelPack(ZipFile zipFile, String domain) {
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loading...");
        ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", domain, CHAIR_MODELS.getJsonFileName()));
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
                    CHAIR_MODELS.putModel(id, modelJson);
                    CHAIR_MODELS.putInfo(id, chairModelItem);
                    if (animations != null && animations.size() > 0) {
                        CHAIR_MODELS.putAnimation(id, animations);
                    }
                    // 打印日志
                    LOGGER.debug(MARKER, "Loaded model: {}", chairModelItem.getModel());
                }
            }
            CHAIR_MODELS.addPack(pack);
        } catch (IOException ignore) {
            // 忽略错误，因为资源域很多
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    @Nullable
    public static BedrockModel<EntityMaid> loadMaidModel(Path rootPath, ResourceLocation modelLocation) {
        File file = rootPath.resolve("assets").resolve(modelLocation.getNamespace()).resolve(modelLocation.getPath()).toFile();
        if (!file.isFile()) {
            return null;
        }
        try (InputStream stream = new FileInputStream(file)) {
            BedrockModelPOJO pojo = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class);
            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals(BedrockVersion.LEGACY.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelLegacy() != null) {
                    return new BedrockModel<>(pojo, BedrockVersion.LEGACY);
                } else {
                    // 否则日志给出提示
                    LOGGER.warn(MARKER, "{} model file don't have model field", modelLocation);
                    return null;
                }
            }

            // 判定是不是 1.12.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals(BedrockVersion.NEW.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelNew() != null) {
                    return new BedrockModel<>(pojo, BedrockVersion.NEW);
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
            if (pojo.getFormatVersion().equals(BedrockVersion.LEGACY.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelLegacy() != null) {
                    return new BedrockModel<>(pojo, BedrockVersion.LEGACY);
                } else {
                    // 否则日志给出提示
                    LOGGER.warn(MARKER, "{} model file don't have model field", modelLocation);
                    return null;
                }
            }

            // 判定是不是 1.12.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals(BedrockVersion.NEW.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelNew() != null) {
                    return new BedrockModel<>(pojo, BedrockVersion.NEW);
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

    @Nullable
    public static BedrockModel<EntityChair> loadChairModel(Path rootPath, ResourceLocation modelLocation) {
        File file = rootPath.resolve("assets").resolve(modelLocation.getNamespace()).resolve(modelLocation.getPath()).toFile();
        if (!file.isFile()) {
            return null;
        }
        try (InputStream stream = new FileInputStream(file)) {
            BedrockModelPOJO pojo = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), BedrockModelPOJO.class);
            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals(BedrockVersion.LEGACY.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelLegacy() != null) {
                    return new BedrockModel<>(pojo, BedrockVersion.LEGACY);
                } else {
                    // 否则日志给出提示
                    LOGGER.warn(MARKER, "{} model file don't have model field", modelLocation);
                    return null;
                }
            }

            // 判定是不是 1.12.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals(BedrockVersion.NEW.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelNew() != null) {
                    return new BedrockModel<>(pojo, BedrockVersion.NEW);
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
            if (pojo.getFormatVersion().equals(BedrockVersion.LEGACY.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelLegacy() != null) {
                    return new BedrockModel<>(pojo, BedrockVersion.LEGACY);
                } else {
                    // 否则日志给出提示
                    LOGGER.warn(MARKER, "{} model file don't have model field", modelLocation);
                    return null;
                }
            }

            // 判定是不是 1.12.0 版本基岩版模型文件
            if (pojo.getFormatVersion().equals(BedrockVersion.NEW.getVersion())) {
                // 如果 model 字段不为空
                if (pojo.getGeometryModelNew() != null) {
                    return new BedrockModel<>(pojo, BedrockVersion.NEW);
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

    private static void readLanguageFile(ZipFile zipFile, String namespace) throws IOException {
        Language language = Minecraft.getInstance().getLanguageManager().getSelected();

        Map<String, String> oldLangData = LanguageMap.getInstance().getLanguageData();
        Map<String, String> newLangData = Maps.newHashMap();

        String defaultLangPath = String.format("assets/%s/lang/en_us.lang", namespace);
        String currentLangPath = String.format("assets/%s/lang/%s.lang", namespace, language.getCode());
        getLanguageMap(zipFile, newLangData, defaultLangPath);
        getLanguageMap(zipFile, newLangData, currentLangPath);

        languageMapMerge(oldLangData, newLangData);
        // FIXME: 2021/10/17 和 Untranslated Items 模组冲突
        ((ClientLanguageMap) LanguageMap.getInstance()).storage = newLangData;
    }

    private static void readLanguageFile(Path rootPath, String namespace) throws IOException {
        Language language = Minecraft.getInstance().getLanguageManager().getSelected();

        Map<String, String> oldLangData = LanguageMap.getInstance().getLanguageData();
        Map<String, String> newLangData = Maps.newHashMap();

        String defaultLangPath = String.format("assets/%s/lang/en_us.lang", namespace);
        String currentLangPath = String.format("assets/%s/lang/%s.lang", namespace, language.getCode());
        getLanguageMap(rootPath, newLangData, defaultLangPath);
        getLanguageMap(rootPath, newLangData, currentLangPath);

        languageMapMerge(oldLangData, newLangData);
        // FIXME: 2021/10/17 和 Untranslated Items 模组冲突
        ((ClientLanguageMap) LanguageMap.getInstance()).storage = newLangData;
    }

    private static void getLanguageMap(Path rootPath, Map<String, String> langData, String defaultLangPath) throws IOException {
        File file = rootPath.resolve(defaultLangPath).toFile();
        if (!file.isFile()) {
            return;
        }
        try (InputStream stream = new FileInputStream(file)) {
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

    private static void languageMapMerge(Map<String, String> oldLangData, Map<String, String> newLangData) {
        for (String key : oldLangData.keySet()) {
            if (!newLangData.containsKey(key)) {
                newLangData.put(key, oldLangData.get(key));
            }
        }
    }
}
