package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.google.common.collect.Maps;
import net.minecraft.util.math.MathHelper;

import java.util.*;

/**
 * @author TartaricAcid
 * @date 2020/1/4 14:00
 * <p>
 * 抽奖的算法类
 **/
public final class DrawCalculation {
    private static final HashMap<String, Integer> MODEL_TO_LEVEL = Maps.newHashMap();
    private static final HashMap<String, Integer> MODEL_TO_WEIGHT = Maps.newHashMap();
    private static final HashMap<Integer, List<String>> LEVEL_TO_MODEL = Maps.newHashMap();
    private static Random RANDOM = new Random();

    /**
     * 依据设定的权重，获取扭蛋的等级
     *
     * @return 扭蛋的等级
     */
    public static int getGashaponLevel() {
        int[] weightArray = {
                GeneralConfig.GASHAPON_CONFIG.gashaponWeights1,
                GeneralConfig.GASHAPON_CONFIG.gashaponWeights2,
                GeneralConfig.GASHAPON_CONFIG.gashaponWeights3,
                GeneralConfig.GASHAPON_CONFIG.gashaponWeights4,
                GeneralConfig.GASHAPON_CONFIG.gashaponWeights5
        };
        int totalWeight = Arrays.stream(weightArray).sum();
        int randomNum = RANDOM.nextInt(totalWeight);
        int level = 0;
        for (int weight : weightArray) {
            randomNum -= weight;
            level += 1;
            if (randomNum < 0) {
                break;
            }
        }
        return level;
    }

    /**
     * 依据 csv 文件设定的权重，获取对应的模型 id
     *
     * @param level 对应的扭蛋等级
     * @return 模型 id
     */
    public static String getCouponModelId(int level) {
        List<String> modelIdList = LEVEL_TO_MODEL.get(level);
        int totalWeight = 0;
        for (String modelId : modelIdList) {
            totalWeight += MODEL_TO_WEIGHT.get(modelId);
        }
        int randomNum = RANDOM.nextInt(totalWeight);
        for (String modelId : modelIdList) {
            randomNum -= MODEL_TO_WEIGHT.get(modelId);
            if (randomNum < 0) {
                return modelId;
            }
        }
        return "";
    }

    public static void clearAllData() {
        MODEL_TO_LEVEL.clear();
        MODEL_TO_WEIGHT.clear();
        LEVEL_TO_MODEL.clear();
    }

    public static void addData(String modelId, int weight, int level) {
        MODEL_TO_LEVEL.put(modelId, level);
        MODEL_TO_WEIGHT.put(modelId, weight);
        if (LEVEL_TO_MODEL.containsKey(level)) {
            LEVEL_TO_MODEL.get(level).add(modelId);
        } else {
            List<String> list = new ArrayList<>();
            list.add(modelId);
            LEVEL_TO_MODEL.put(level, list);
        }
    }

    public static int getModelLevel(String modelId) {
        if (MODEL_TO_LEVEL.containsKey(modelId)) {
            return MathHelper.clamp(MODEL_TO_LEVEL.get(modelId), 1, 5);
        }
        return 1;
    }
}
