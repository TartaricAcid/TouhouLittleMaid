package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.CustomModelPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPackPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.proxy.ClientProxy;
import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/7/10 20:12
 **/
@SideOnly(Side.CLIENT)
public final class CustomModelLoader {
    private static final Logger LOGGER = TouhouLittleMaid.LOGGER;
    private static final Gson GSON = new Gson();
    private static IResourceManager manager = Minecraft.getMinecraft().getResourceManager();

    /**
     * 获取客户端代理类的模型包列表数据
     */
    private static List<CustomModelPackPOJO> MODEL_PACK_LIST = ClientProxy.MODEL_PACK_LIST;
    /**
     * 获取模型资源域名和对应模型的映射表
     */
    private static HashMap<String, EntityModelJson> LOCATION_MODEL_MAP = ClientProxy.LOCATION_MODEL_MAP;

    /**
     * 加载所有的模型包
     */
    private static void loadModelPack() {
        LOGGER.info("Touhou little maid mod's model is loading...");

        // 遍历所有的资源包，获取到模型文件
        for (String domain : manager.getResourceDomains()) {
            try {
                // 获取所有资源域下的 maid_model.json 文件
                ResourceLocation res = new ResourceLocation(domain, "maid_model.json");
                InputStream input = manager.getResource(res).getInputStream();

                // 将其转换为 pojo 对象
                CustomModelPackPOJO pojo = GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), CustomModelPackPOJO.class);

                // 对必须的包名和模型列表做检查
                if (pojo.getPackName() != null && pojo.getModelList() != null) {
                    // 加载模型
                    loadModelList(pojo.getModelList(), res);
                    // 装填模型包列表
                    MODEL_PACK_LIST.add(pojo);
                    // 打印日志
                    LOGGER.info("{} file is loaded", res);
                } else {
                    // 否则日志给出提示
                    LOGGER.warn("{} file don't have pack_name field or model field", res);
                }
            } catch (IOException ignore) {
                // 忽略错误，因为资源域很多
            }
        }
        LOGGER.info("Touhou little maid mod's model is loaded");
    }

    /**
     * 加载所有的模型列表
     */
    private static void loadModelList(List<ModelItem> modelList, ResourceLocation res) {
        for (ModelItem model : modelList) {
            // 如果模型、模型名和模型材质都不为空
            if (model.getModel() != null && model.getName() != null && model.getTexture() != null) {
                // 尝试加载模型
                EntityModelJson modelJson = loadModel(new ResourceLocation(model.getModel()));
                if (modelJson != null) {
                    // 如果加载的模型不为空
                    // 塞入资源域到模型的映射列表
                    LOCATION_MODEL_MAP.put(model.getModel(), modelJson);
                    // 打印日志
                    LOGGER.info("Loaded model: {}", model.getModel());
                }
            } else {
                // 否则日志给出提示
                LOGGER.warn("{} file don't have model field or name field or texture field", res);
            }
        }
    }

    /**
     * 将对应的 json 模型文件转换为模型对象
     *
     * @return 如果加载出错，会返回 Null
     */
    @Nullable
    private static EntityModelJson loadModel(ResourceLocation modelLocation) {
        try {
            InputStream input = manager.getResource(modelLocation).getInputStream();
            CustomModelPOJO pojo = GSON.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), CustomModelPOJO.class);

            // 如果 model 字段不为空
            if (pojo.getGeometryModel() != null) {
                return new EntityModelJson(pojo);
            } else {
                // 否则日志给出提示
                LOGGER.warn("{} model file don't have model field", modelLocation);
            }
        } catch (IOException ioe) {
            // 可能用来判定错误，打印下
            LOGGER.warn("Failed to load model: {}", modelLocation);
        }

        // 如果前面出了错，返回 Null
        return null;
    }

    /**
     * 重新装载所有的模型包
     * 实际上也可用来首次加载资源包，我就是这么做的，省代码了
     */
    public static void reloadModelPack() {
        // 清空数据
        MODEL_PACK_LIST.clear();
        LOCATION_MODEL_MAP.clear();

        // 重载数据
        loadModelPack();
    }
}
