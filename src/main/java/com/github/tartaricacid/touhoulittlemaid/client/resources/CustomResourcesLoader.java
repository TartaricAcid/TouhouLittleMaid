package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.CustomJsAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.model.BedrockVersion;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CubesItem;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CustomModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.*;
import com.github.tartaricacid.touhoulittlemaid.network.serverpack.ClientPackManager;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SideOnly(Side.CLIENT)
public class CustomResourcesLoader {
    public static final Gson GSON = new GsonBuilder().registerTypeAdapter(ResourceLocation.class, new ResourceLocation.Serializer())
            .registerTypeAdapter(CubesItem.class, new CubesItem.Deserializer()).create();
    public static final CustomMaidModelResources MAID_MODEL = new CustomMaidModelResources("maid_model.json", Lists.newArrayList(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap());
    public static final CustomChairModelResources CHAIR_MODEL = new CustomChairModelResources("maid_chair.json", Lists.newArrayList(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap());
    public static final CustomSoundResources SOUND_INFO = new CustomSoundResources("maid_sound.json", Lists.newArrayList());
    private static final Logger LOGGER = TouhouLittleMaid.LOGGER;
    private static final Marker MARKER = MarkerManager.getMarker("ResourcesLoader");
    private static IResourceManager manager = Minecraft.getMinecraft().getResourceManager();

    public static void reloadResources() {
        CustomJsAnimationManger.clearAll();
        MAID_MODEL.clearAll();
        CHAIR_MODEL.clearAll();
        SOUND_INFO.clearAll();
        // 重载数据
        loadMaidModelPack();
        loadChairModelPack();
        loadSoundInfo();
        if (!ClientPackManager.CRC32_LIST_FROM_SERVER.isEmpty()) {
            for (long crc32 : ClientPackManager.CRC32_LIST_FROM_SERVER) {
                ClientPackManager.readModelFromZipFile(ClientPackManager.getCrc32FileMap().get(crc32));
            }
        }
    }

    private static void loadMaidModelPack() {
        LOGGER.info(MARKER, "Touhou little maid mod's model is loading...");
        // 遍历所有的资源包，获取到模型文件
        for (String domain : manager.getResourceDomains()) {
            InputStream input = null;
            try {
                // 获取所有资源域下的指定文件
                ResourceLocation res = new ResourceLocation(domain, MAID_MODEL.getJsonFileName());
                input = manager.getResource(res).getInputStream();
                // 将其转换为 pojo 对象
                // 这个 pojo 是二次修饰的过的对象，所以一部分数据异常已经进行了处理或者抛出
                CustomModelPack<MaidModelInfo> pack = GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), new TypeToken<CustomModelPack<MaidModelInfo>>() {
                }.getType());
                pack.decorate();
                for (MaidModelInfo maidModelItem : pack.getModelList()) {
                    // 尝试加载模型
                    EntityModelJson modelJson = loadModel(maidModelItem.getModel());
                    // 加载动画
                    @Nullable List<Object> animations = CustomJsAnimationManger.getCustomAnimation(maidModelItem);
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
            } catch (IOException ignore) {
                // 忽略错误，因为资源域很多
            } catch (JsonSyntaxException e) {
                LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(input);
            }
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

    private static void loadChairModelPack() {
        LOGGER.info(MARKER, "Touhou little maid mod's model is loading...");
        // 遍历所有的资源包，获取到模型文件
        for (String domain : manager.getResourceDomains()) {
            InputStream input = null;
            try {
                // 获取所有资源域下的指定文件
                ResourceLocation res = new ResourceLocation(domain, CHAIR_MODEL.getJsonFileName());
                input = manager.getResource(res).getInputStream();
                // 将其转换为 pojo 对象
                // 这个 pojo 是二次修饰的过的对象，所以一部分数据异常已经进行了处理或者抛出
                CustomModelPack<ChairModelInfo> pack = GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), new TypeToken<CustomModelPack<ChairModelInfo>>() {
                }.getType());
                pack.decorate();
                for (ChairModelInfo chairModelItem : pack.getModelList()) {
                    // 尝试加载模型
                    EntityModelJson modelJson = loadModel(chairModelItem.getModel());
                    // 加载动画
                    @Nullable List<Object> animations = CustomJsAnimationManger.getCustomAnimation(chairModelItem);
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
            } finally {
                IOUtils.closeQuietly(input);
            }
        }
        LOGGER.info(MARKER, "Touhou little maid mod's model is loaded");
    }

    @Nullable
    public static EntityModelJson loadModel(ResourceLocation modelLocation) {
        InputStream input = null;
        try {
            input = manager.getResource(modelLocation).getInputStream();
            CustomModelPOJO pojo = GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), CustomModelPOJO.class);

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
        } finally {
            // 关闭输入流
            IOUtils.closeQuietly(input);
        }

        // 如果前面出了错，返回 Null
        return null;
    }

    public static void loadSoundInfo() {
        LOGGER.info(MARKER, "Touhou little maid mod's sound info is loading...");
        // 遍历所有的资源包，获取到模型文件
        for (String domain : manager.getResourceDomains()) {
            InputStream input = null;
            try {
                // 获取所有资源域下的指定文件
                ResourceLocation res = new ResourceLocation(domain, SOUND_INFO.getJsonFileName());
                input = manager.getResource(res).getInputStream();
                // 将其转换为 pojo 对象
                // 这个 pojo 是二次修饰的过的对象，所以一部分数据异常已经进行了处理或者抛出
                SoundPackInfo info = GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), SoundPackInfo.class);
                SOUND_INFO.putInfo(info.decorate());
                // 打印日志
                LOGGER.info(MARKER, "Loaded sound info from: {}", domain);
            } catch (IOException ignore) {
                // 忽略错误，因为资源域很多
            } catch (JsonSyntaxException e) {
                LOGGER.warn(MARKER, "Fail to parse sound info from domain {}", domain);
                e.printStackTrace();
            } finally {
                IOUtils.closeQuietly(input);
            }
        }
        LOGGER.info(MARKER, "Touhou little maid mod's sound info is loaded");
    }
}
