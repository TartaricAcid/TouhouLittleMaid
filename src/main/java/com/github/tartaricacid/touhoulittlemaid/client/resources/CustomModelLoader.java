package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.EnumAnimationType;
import com.github.tartaricacid.touhoulittlemaid.client.animation.pojo.CustomAnimation;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CustomModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPackPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.util.TriConsumer;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author TartaricAcid
 * @date 2019/7/10 20:12
 **/
@SideOnly(Side.CLIENT)
public final class CustomModelLoader {
    private static final Logger LOGGER = TouhouLittleMaid.LOGGER;
    private static final Marker MARKER = MarkerManager.getMarker("ModelLoader");
    private static final String OLD_BEDROCK_VERSION = "1.10.0";
    private static final String ANIMATION_VERSION = "1.8.0";
    private static IResourceManager manager = Minecraft.getMinecraft().getResourceManager();

    /**
     * 重新装载所有的模型包
     * 实际上也可用来首次加载资源包，我就是这么做的，省代码了
     */
    public static void reloadModelPack(CustomModelResources resources) {
        resources.clearAll();
        // 重载数据
        loadModelPack(resources);
    }

    /**
     * 加载所有的模型包
     */
    private static void loadModelPack(CustomModelResources resources) {
        LOGGER.info(MARKER, "Touhou little maid mod's model is loading...");
        loadModelPackMain(resources.getJsonFileName(), pojo -> {
            // 加载模型
            loadModelList(pojo.getModelList(), ((modelItem, modelJson, animations) -> {
                String id = modelItem.getModelId().toString();
                resources.putModel(id, modelJson);
                resources.putInfo(id, modelItem);
                resources.putAnimation(id, animations);
            }));
            // 装填模型包列表
            resources.addPack(pojo);
        });
        LOGGER.info(MARKER, "Touhou little maid mod's model is loaded");
    }

    /**
     * 遍历所有资源域指定文件，进行特定操作
     */
    private static void loadModelPackMain(String pathIn, Consumer<CustomModelPackPOJO> consumer) {
        // 遍历所有的资源包，获取到模型文件
        for (String domain : manager.getResourceDomains()) {
            try {
                // 获取所有资源域下的指定文件
                ResourceLocation res = new ResourceLocation(domain, pathIn);
                InputStream inputStream = manager.getResource(res).getInputStream();
                // 将其转换为 pojo 对象
                // 这个 pojo 是二次修饰的过的对象，所以一部分数据异常已经进行了处理或者抛出
                CustomModelPackPOJO pojo = CommonProxy.readModelPack(inputStream);
                // 关闭输入流
                IOUtils.closeQuietly(inputStream);
                consumer.accept(pojo);
            } catch (IOException ignore) {
                // 忽略错误，因为资源域很多
            } catch (JsonSyntaxException e) {
                LOGGER.warn(MARKER, "Fail to parse model pack in domain {}", domain);
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载所有的模型列表
     * 传入的 modelItem 是已经二次修饰过的
     */
    private static void loadModelList(List<ModelItem> modelList, TriConsumer<ModelItem, EntityModelJson, HashMap<EnumAnimationType, CustomAnimation>> triConsumer) throws JsonSyntaxException {
        for (ModelItem modelItem : modelList) {
            // 尝试加载模型
            EntityModelJson modelJson = loadModel(modelItem.getModel(), modelItem.getFormat());
            if (modelJson != null) {
                // 加载动画
                HashMap<EnumAnimationType, CustomAnimation> animations = Maps.newHashMap();
                for (String name : modelItem.getAnimations().keySet()) {
                    animations.put(EnumAnimationType.getTypeByName(name), loadAnimation(modelItem.getAnimations().get(name)));
                }
                // 如果加载的模型不为空
                triConsumer.accept(modelItem, modelJson, animations);
                // 打印日志
                LOGGER.info(MARKER, "Loaded model: {}", modelItem.getModel());
            }
        }
    }

    /**
     * 将对应的 json 模型文件转换为模型对象
     *
     * @return 如果加载出错，会返回 Null
     */
    @Nullable
    private static EntityModelJson loadModel(ResourceLocation modelLocation, int format) {
        try {
            InputStream input = manager.getResource(modelLocation).getInputStream();
            CustomModelPOJO pojo = CommonProxy.GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), CustomModelPOJO.class);
            // 关闭输入流
            IOUtils.closeQuietly(input);

            // 先判断是不是 1.10.0 版本基岩版模型文件
            if (!pojo.getFormatVersion().equals(OLD_BEDROCK_VERSION)) {
                LOGGER.warn(MARKER, "{} model version is not 1.10.0", modelLocation);
                // TODO: 2019/7/26 添加对高版本基岩版模型的兼容
                return null;
            }

            // 如果 model 字段不为空
            if (pojo.getGeometryModel() != null) {
                return new EntityModelJson(pojo, format);
            } else {
                // 否则日志给出提示
                LOGGER.warn(MARKER, "{} model file don't have model field", modelLocation);
            }
        } catch (IOException ioe) {
            // 可能用来判定错误，打印下
            LOGGER.warn(MARKER, "Failed to load model: {}", modelLocation);
        }

        // 如果前面出了错，返回 Null
        return null;
    }

    /**
     * 将对应的动画文件转换为动画对象
     *
     * @return 如果加载出错，会返回 Null
     */
    @Nullable
    private static CustomAnimation loadAnimation(ResourceLocation modelLocation) {
        try {
            InputStream input = manager.getResource(modelLocation).getInputStream();
            CustomAnimation animation = CommonProxy.GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), CustomAnimation.class);
            // 关闭输入流
            IOUtils.closeQuietly(input);
            // 先判断是不是 1.8.0 版本基岩版动画文件
            if (!animation.getFormatVersion().equals(ANIMATION_VERSION)) {
                LOGGER.warn(MARKER, "{} animation version is not 1.8.0", modelLocation);
                return null;
            }
            return animation;
        } catch (IOException ioe) {
            // 可能用来判定错误，打印下
            LOGGER.warn(MARKER, "Failed to load animation: {}", modelLocation);
        }
        // 如果前面出了错，返回 Null
        return null;
    }
}
