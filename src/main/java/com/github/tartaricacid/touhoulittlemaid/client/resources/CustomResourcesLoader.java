package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.CustomJsAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CustomModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ChairModelInfo;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPack;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.SoundPackInfo;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy.GSON;

@SideOnly(Side.CLIENT)
public class CustomResourcesLoader {
    public static final CustomMaidModelResources MAID_MODEL = new CustomMaidModelResources("maid_model.json", Lists.newArrayList(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap());
    public static final CustomChairModelResources CHAIR_MODEL = new CustomChairModelResources("maid_chair.json", Lists.newArrayList(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap());
    public static final CustomSoundResources SOUND_INFO = new CustomSoundResources("maid_sound.json", Lists.newArrayList());
    private static final Logger LOGGER = TouhouLittleMaid.LOGGER;
    private static final Marker MARKER = MarkerManager.getMarker("ResourcesLoader");
    private static final String OLD_BEDROCK_VERSION = "1.10.0";
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
                        String id = maidModelItem.getModelId().toString();
                        // 如果加载的模型不为空
                        MAID_MODEL.putModel(id, modelJson);
                        MAID_MODEL.putInfo(id, maidModelItem);
                        if (animations != null && animations.size() > 0) {
                            MAID_MODEL.putAnimation(id, animations);
                        }
                        // 打印日志
                        LOGGER.info(MARKER, "Loaded model: {}", maidModelItem.getModel());
                    }
                }
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
            CustomModelPOJO pojo = CommonProxy.GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), CustomModelPOJO.class);

            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (!pojo.getFormatVersion().equals(OLD_BEDROCK_VERSION)) {
                LOGGER.warn(MARKER, "{} model version is not 1.10.0", modelLocation);
                // TODO: 2019/7/26 添加对高版本基岩版模型的兼容
                return null;
            }

            // 如果 model 字段不为空
            if (pojo.getGeometryModel() != null) {
                return new EntityModelJson(pojo);
            } else {
                // 否则日志给出提示
                LOGGER.warn(MARKER, "{} model file don't have model field", modelLocation);
            }
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
