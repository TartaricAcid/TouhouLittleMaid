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
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.ChatBubbleInfo;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resource.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.client.sound.CustomSoundLoader;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatText;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import com.github.tartaricacid.touhoulittlemaid.util.GetJarResources;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
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
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid.LOGGER;

@OnlyIn(Dist.CLIENT)
public class CustomPackLoader {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(CubesItem.class, new CubesItem.Deserializer())
            .registerTypeAdapter(ChatText.class, new ChatText.Serializer())
            .create();
    public static final MaidModels MAID_MODELS = MaidModels.getInstance();
    public static final ChairModels CHAIR_MODELS = ChairModels.getInstance();
    private static final Set<ResourceLocation> TMP_REGISTER_TEXTURE = Sets.newHashSet();
    private static final String COMMENT_SYMBOL = "#";
    private static final String CUSTOM_PACK_DIR_NAME = "tlm_custom_pack";
    public static final Path PACK_FOLDER = Paths.get(Minecraft.getInstance().gameDirectory.toURI()).resolve(CUSTOM_PACK_DIR_NAME);
    private static final String DEFAULT_PACK_NAME = "touhou_little_maid-1.0.0.zip";
    private static final Marker MARKER = MarkerManager.getMarker("CustomPackLoader");
    private static final Pattern DOMAIN = Pattern.compile("^assets/([\\w.]+)/$");

    public static void reloadPacks() {
        // 清除
        CustomJsAnimationManger.clearAll();
        MAID_MODELS.clearAll();
        CHAIR_MODELS.clearAll();
        TMP_REGISTER_TEXTURE.clear();
        CustomSoundLoader.clear();

        // 读取
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
                    CustomSoundLoader.loadSoundPack(rootPath, domain);
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
                    CustomSoundLoader.loadSoundPack(zipFile, domain);
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
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            CustomModelPack<MaidModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<MaidModelInfo>>() {
                    }.getType());
            pack.decorate();
            // 加载图标贴图
            if (pack.getIcon() != null) {
                registerFilePackTexture(rootPath, pack.getIcon());
            }
            for (MaidModelInfo maidModelItem : pack.getModelList()) {
                loadChatBubble(rootPath, maidModelItem);
                if (maidModelItem.isGeckoModel()) {
                    loadGeckoMaidModelElement(rootPath, maidModelItem);
                } else {
                    loadMaidModelElement(rootPath, maidModelItem);
                }
            }
            MAID_MODELS.addPack(pack);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    private static void loadMaidModelElement(Path rootPath, MaidModelInfo maidModelItem) {
        // 尝试加载模型
        BedrockModel<EntityMaid> modelJson = loadMaidModel(rootPath, maidModelItem.getModel());
        // 加载贴图
        registerFilePackTexture(rootPath, maidModelItem.getTexture());
        // 加载动画
        @Nullable List<Object> animations = CustomJsAnimationManger.getCustomAnimation(rootPath, maidModelItem);
        if (modelJson != null) {
            // 加载彩蛋，彩蛋不允许为空
            if (maidModelItem.getEasterEgg() != null && StringUtils.isNotBlank(maidModelItem.getEasterEgg().getTag())) {
                putMaidEasterEggData(maidModelItem, modelJson, animations);
            } else {
                putMaidModelData(maidModelItem, modelJson, animations);
            }
            // 打印日志
            LOGGER.debug(MARKER, "Loaded model: {}", maidModelItem.getModel());
        }
    }

    private static void loadChatBubble(Path rootPath, MaidModelInfo maidModelItem) {
        // 加载聊天气泡背景
        ChatBubbleInfo chatBubble = maidModelItem.getChatBubble();
        registerFilePackTexture(rootPath, chatBubble.getBg());
        // 加载聊聊天气泡图标
        ChatBubbleInfo.Text text = chatBubble.getText();
        text.getMain().values().forEach(chatTexts -> chatTexts.stream().filter(ChatText::isIcon)
                .forEach(chatText -> registerFilePackTexture(rootPath, chatText.getIconPath())));
        text.getSpecial().values().forEach(chatTexts -> chatTexts.stream().filter(ChatText::isIcon)
                .forEach(chatText -> registerFilePackTexture(rootPath, chatText.getIconPath())));
        text.getOther().values().forEach(chatTexts -> chatTexts.stream().filter(ChatText::isIcon)
                .forEach(chatText -> registerFilePackTexture(rootPath, chatText.getIconPath())));
    }

    private static void loadGeckoMaidModelElement(Path rootPath, MaidModelInfo maidModelItem) throws IOException {
        ResourceLocation uid = maidModelItem.getModelId();
        // 尝试加载模型
        ResourceLocation modelLocation = maidModelItem.getModel();
        File modelFile = rootPath.resolve("assets").resolve(modelLocation.getNamespace()).resolve(modelLocation.getPath()).toFile();
        if (!modelFile.isFile()) {
            return;
        }
        try (InputStream fileInputStream = Files.newInputStream(modelFile.toPath())) {
            GeckoModelLoader.registerGeo(uid, fileInputStream);
        }
        // 加载贴图
        registerFilePackTexture(rootPath, maidModelItem.getTexture());
        // 加载动画
        List<ResourceLocation> animation = maidModelItem.getAnimation();
        if (animation == null || animation.size() == 0) {
            return;
        }
        AnimationFile animationData = new AnimationFile();
        for (ResourceLocation animationPath : animation) {
            if (animationPath.equals(GeckoModelLoader.DEFAULT_MAID_ANIMATION)) {
                break;
            }
            File animationFile = rootPath.resolve("assets").resolve(animationPath.getNamespace()).resolve(animationPath.getPath()).toFile();
            if (!animationFile.isFile()) {
                continue;
            }
            try (InputStream fileInputStream = Files.newInputStream(animationFile.toPath())) {
                GeckoModelLoader.mergeAnimationFile(fileInputStream, animationData);
            }
        }
        GeckoModelLoader.registerMaidAnimations(uid, animationData);
        if (maidModelItem.getEasterEgg() != null && StringUtils.isNotBlank(maidModelItem.getEasterEgg().getTag())) {
            putMaidEasterEggData(maidModelItem, null, null);
        } else {
            MAID_MODELS.putInfo(uid.toString(), maidModelItem);
        }
        // 打印日志
        LOGGER.debug(MARKER, "Loaded model: {}", maidModelItem.getModel());
    }

    private static void loadGeckoChairModelElement(Path rootPath, ChairModelInfo chairModelItem) throws IOException {
        ResourceLocation uid = chairModelItem.getModelId();
        // 尝试加载模型
        ResourceLocation modelLocation = chairModelItem.getModel();
        File modelFile = rootPath.resolve("assets").resolve(modelLocation.getNamespace()).resolve(modelLocation.getPath()).toFile();
        if (!modelFile.isFile()) {
            return;
        }
        try (InputStream fileInputStream = Files.newInputStream(modelFile.toPath())) {
            GeckoModelLoader.registerGeo(uid, fileInputStream);
        }
        // 加载贴图
        registerFilePackTexture(rootPath, chairModelItem.getTexture());
        // 加载动画
        List<ResourceLocation> animation = chairModelItem.getAnimation();
        if (animation == null || animation.size() == 0) {
            return;
        }
        AnimationFile animationData = new AnimationFile();
        for (ResourceLocation animationPath : animation) {
            if (animationPath.equals(GeckoModelLoader.DEFAULT_CHAIR_ANIMATION)) {
                break;
            }
            File animationFile = rootPath.resolve("assets").resolve(animationPath.getNamespace()).resolve(animationPath.getPath()).toFile();
            if (!animationFile.isFile()) {
                continue;
            }
            try (InputStream fileInputStream = Files.newInputStream(animationFile.toPath())) {
                GeckoModelLoader.mergeAnimationFile(fileInputStream, animationData);
            }
        }
        GeckoModelLoader.registerChairAnimations(uid, animationData);
        CHAIR_MODELS.putInfo(uid.toString(), chairModelItem);
        // 打印日志
        LOGGER.debug(MARKER, "Loaded model: {}", chairModelItem.getModel());
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
                registerZipPackTexture(zipFile.getName(), pack.getIcon());
            }
            for (MaidModelInfo maidModelItem : pack.getModelList()) {
                loadCharBubble(zipFile, maidModelItem);
                if (maidModelItem.isGeckoModel()) {
                    loadGeckoMaidModelElement(zipFile, maidModelItem);
                } else {
                    loadMaidModelElement(zipFile, maidModelItem);
                }
            }
            MAID_MODELS.addPack(pack);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
            e.printStackTrace();
        }
        LOGGER.debug(MARKER, "Touhou little maid mod's model is loaded");
    }

    private static void loadMaidModelElement(ZipFile zipFile, MaidModelInfo maidModelItem) {
        // 尝试加载模型
        BedrockModel<EntityMaid> modelJson = loadMaidModel(zipFile, maidModelItem.getModel());
        // 加载贴图
        registerZipPackTexture(zipFile.getName(), maidModelItem.getTexture());
        // 加载动画
        @Nullable List<Object> animations = CustomJsAnimationManger.getCustomAnimation(zipFile, maidModelItem);
        if (modelJson != null) {
            // 加载彩蛋，彩蛋不允许为空
            if (maidModelItem.getEasterEgg() != null && StringUtils.isNotBlank(maidModelItem.getEasterEgg().getTag())) {
                putMaidEasterEggData(maidModelItem, modelJson, animations);
            } else {
                putMaidModelData(maidModelItem, modelJson, animations);
            }
            // 打印日志
            LOGGER.debug(MARKER, "Loaded model: {}", maidModelItem.getModel());
        }
    }

    private static void loadCharBubble(ZipFile zipFile, MaidModelInfo maidModelItem) {
        // 加载聊天气泡背景
        ChatBubbleInfo chatBubble = maidModelItem.getChatBubble();
        registerZipPackTexture(zipFile.getName(), chatBubble.getBg());
        // 加载聊聊天气泡图标
        ChatBubbleInfo.Text text = chatBubble.getText();
        text.getMain().values().forEach(chatTexts -> chatTexts.stream().filter(ChatText::isIcon)
                .forEach(chatText -> registerZipPackTexture(zipFile.getName(), chatText.getIconPath())));
        text.getSpecial().values().forEach(chatTexts -> chatTexts.stream().filter(ChatText::isIcon)
                .forEach(chatText -> registerZipPackTexture(zipFile.getName(), chatText.getIconPath())));
        text.getOther().values().forEach(chatTexts -> chatTexts.stream().filter(ChatText::isIcon)
                .forEach(chatText -> registerZipPackTexture(zipFile.getName(), chatText.getIconPath())));
    }

    private static void loadGeckoMaidModelElement(ZipFile zipFile, MaidModelInfo maidModelItem) throws IOException {
        ResourceLocation uid = maidModelItem.getModelId();
        // 尝试加载模型
        ResourceLocation modelLocation = maidModelItem.getModel();
        String path = String.format("assets/%s/%s", modelLocation.getNamespace(), modelLocation.getPath());
        ZipEntry modelZipEntry = zipFile.getEntry(path);
        if (modelZipEntry == null) {
            return;
        }
        try (InputStream zipFileInputStream = zipFile.getInputStream(modelZipEntry)) {
            GeckoModelLoader.registerGeo(uid, zipFileInputStream);
        }
        // 加载贴图
        registerZipPackTexture(zipFile.getName(), maidModelItem.getTexture());
        // 加载动画
        List<ResourceLocation> animation = maidModelItem.getAnimation();
        if (animation == null || animation.size() == 0) {
            return;
        }
        AnimationFile animationData = new AnimationFile();
        for (ResourceLocation animationPath : animation) {
            if (animationPath.equals(GeckoModelLoader.DEFAULT_MAID_ANIMATION)) {
                break;
            }
            ZipEntry animationZipEntry = zipFile.getEntry(String.format("assets/%s/%s", animationPath.getNamespace(), animationPath.getPath()));
            if (animationZipEntry == null) {
                continue;
            }
            try (InputStream zipFileInputStream = zipFile.getInputStream(animationZipEntry)) {
                GeckoModelLoader.mergeAnimationFile(zipFileInputStream, animationData);
            }
        }
        GeckoModelLoader.registerMaidAnimations(uid, animationData);
        if (maidModelItem.getEasterEgg() != null && StringUtils.isNotBlank(maidModelItem.getEasterEgg().getTag())) {
            putMaidEasterEggData(maidModelItem, null, null);
        } else {
            MAID_MODELS.putInfo(uid.toString(), maidModelItem);
        }
        // 打印日志
        LOGGER.debug(MARKER, "Loaded model: {}", maidModelItem.getModel());
    }

    private static void loadGeckoChairModelElement(ZipFile zipFile, ChairModelInfo chairModelItem) throws IOException {
        ResourceLocation uid = chairModelItem.getModelId();
        // 尝试加载模型
        ResourceLocation modelLocation = chairModelItem.getModel();
        String path = String.format("assets/%s/%s", modelLocation.getNamespace(), modelLocation.getPath());
        ZipEntry modelZipEntry = zipFile.getEntry(path);
        if (modelZipEntry == null) {
            return;
        }
        try (InputStream zipFileInputStream = zipFile.getInputStream(modelZipEntry)) {
            GeckoModelLoader.registerGeo(uid, zipFileInputStream);
        }
        // 加载贴图
        registerZipPackTexture(zipFile.getName(), chairModelItem.getTexture());
        // 加载动画
        List<ResourceLocation> animation = chairModelItem.getAnimation();
        if (animation == null || animation.size() == 0) {
            return;
        }
        AnimationFile animationData = new AnimationFile();
        for (ResourceLocation animationPath : animation) {
            if (animationPath.equals(GeckoModelLoader.DEFAULT_CHAIR_ANIMATION)) {
                break;
            }
            ZipEntry animationZipEntry = zipFile.getEntry(String.format("assets/%s/%s", animationPath.getNamespace(), animationPath.getPath()));
            if (animationZipEntry == null) {
                continue;
            }
            try (InputStream zipFileInputStream = zipFile.getInputStream(animationZipEntry)) {
                GeckoModelLoader.mergeAnimationFile(zipFileInputStream, animationData);
            }
        }
        GeckoModelLoader.registerChairAnimations(uid, animationData);
        CHAIR_MODELS.putInfo(uid.toString(), chairModelItem);
        // 打印日志
        LOGGER.debug(MARKER, "Loaded model: {}", chairModelItem.getModel());
    }

    @SuppressWarnings("all")
    private static void putMaidEasterEggData(MaidModelInfo maidModelItem, @Nullable BedrockModel<EntityMaid> modelJson, @Nullable List<Object> animations) {
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
        try (InputStream stream = Files.newInputStream(file.toPath())) {
            // 将其转换为 pojo 对象
            // 这个 pojo 是二次修饰的过的对象，所以一部分数据异常已经进行了处理或者抛出
            CustomModelPack<ChairModelInfo> pack = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8),
                    new TypeToken<CustomModelPack<ChairModelInfo>>() {
                    }.getType());
            pack.decorate();
            // 加载图标贴图
            if (pack.getIcon() != null) {
                registerFilePackTexture(rootPath, pack.getIcon());
            }
            for (ChairModelInfo chairModelItem : pack.getModelList()) {
                if (chairModelItem.isGeckoModel()) {
                    loadGeckoChairModelElement(rootPath, chairModelItem);
                } else {
                    loadChairModelElement(rootPath, chairModelItem);
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

    private static void loadChairModelElement(Path rootPath, ChairModelInfo chairModelItem) {
        // 尝试加载模型
        BedrockModel<EntityChair> modelJson = loadChairModel(rootPath, chairModelItem.getModel());
        // 加载贴图
        registerFilePackTexture(rootPath, chairModelItem.getTexture());
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
                registerZipPackTexture(zipFile.getName(), pack.getIcon());
            }
            for (ChairModelInfo chairModelItem : pack.getModelList()) {
                if (chairModelItem.isGeckoModel()) {
                    loadGeckoChairModelElement(zipFile, chairModelItem);
                } else {
                    loadChairModelElement(zipFile, chairModelItem);
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

    private static void loadChairModelElement(ZipFile zipFile, ChairModelInfo chairModelItem) {
        // 尝试加载模型
        BedrockModel<EntityChair> modelJson = loadChairModel(zipFile, chairModelItem.getModel());
        // 加载贴图
        registerZipPackTexture(zipFile.getName(), chairModelItem.getTexture());
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

    @Nullable
    public static BedrockModel<EntityMaid> loadMaidModel(Path rootPath, ResourceLocation modelLocation) {
        File file = rootPath.resolve("assets").resolve(modelLocation.getNamespace()).resolve(modelLocation.getPath()).toFile();
        if (!file.isFile()) {
            return null;
        }
        try (InputStream stream = Files.newInputStream(file.toPath())) {
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
        try (InputStream stream = Files.newInputStream(file.toPath())) {
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

    public static void registerFilePackTexture(Path rootPath, ResourceLocation texturePath) {
        if (!TMP_REGISTER_TEXTURE.contains(texturePath)) {
            FilePackTexture filePackTexture = new FilePackTexture(rootPath, texturePath);
            if (filePackTexture.isExist()) {
                Minecraft.getInstance().textureManager.register(texturePath, filePackTexture);
                TMP_REGISTER_TEXTURE.add(texturePath);
            }
        }
    }

    public static void registerZipPackTexture(String zipFilePath, ResourceLocation texturePath) {
        if (!TMP_REGISTER_TEXTURE.contains(texturePath)) {
            ZipPackTexture zipPackTexture = new ZipPackTexture(zipFilePath, texturePath);
            if (zipPackTexture.isExist()) {
                Minecraft.getInstance().textureManager.register(texturePath, zipPackTexture);
                TMP_REGISTER_TEXTURE.add(texturePath);
            }
        }
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
        // FIXME: 还是会存在问题
        if (LanguageMap.getInstance() instanceof ClientLanguageMap) {
            ((ClientLanguageMap) LanguageMap.getInstance()).storage = newLangData;
        }
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
        // FIXME: 还是会存在问题
        if (LanguageMap.getInstance() instanceof ClientLanguageMap) {
            ((ClientLanguageMap) LanguageMap.getInstance()).storage = newLangData;
        }
    }

    private static void getLanguageMap(Path rootPath, Map<String, String> langData, String defaultLangPath) throws IOException {
        File file = rootPath.resolve(defaultLangPath).toFile();
        if (!file.isFile()) {
            return;
        }
        try (InputStream stream = Files.newInputStream(file.toPath())) {
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