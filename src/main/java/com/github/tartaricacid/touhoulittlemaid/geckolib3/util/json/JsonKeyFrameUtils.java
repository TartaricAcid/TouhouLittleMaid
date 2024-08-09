/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.util.json;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.ConstantValue;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.easing.EasingType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.KeyFrame;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.VectorKeyFrameList;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangException;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.AnimationUtils;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;
import java.util.Map;

/**
 * 用于将 json 转换成关键帧的工具类
 */
@SuppressWarnings({"unchecked", "rawtypes", "ReassignedVariable"})
public class JsonKeyFrameUtils {
    private static VectorKeyFrameList<KeyFrame<IValue>> convertJson(List<Map.Entry<String, JsonElement>> element, boolean isRotation,
                                                                    MolangParser parser) throws NumberFormatException, MolangException {
        IValue previousXValue = null;
        IValue previousYValue = null;
        IValue previousZValue = null;

        var xKeyFrames = new ObjectArrayList<KeyFrame<IValue>>();
        var yKeyFrames = new ObjectArrayList<KeyFrame<IValue>>();
        var zKeyFrames = new ObjectArrayList<KeyFrame<IValue>>();

        for (int i = 0; i < element.size(); i++) {
            Map.Entry<String, JsonElement> keyframe = element.get(i);
            // TODO: 改成标准基岩版的插值类型
            if ("easing".equals(keyframe.getKey()) || "easingArgs".equals(keyframe.getKey())) {
                continue;
            }
            Map.Entry<String, JsonElement> previousKeyFrame = i == 0 ? null : element.get(i - 1);

            double previousKeyFrameLocation = previousKeyFrame == null ? 0 : Double.parseDouble(previousKeyFrame.getKey());
            double currentKeyFrameLocation = NumberUtils.isCreatable(keyframe.getKey()) ? Double.parseDouble(keyframe.getKey()) : 0;
            double animationTimeDifference = currentKeyFrameLocation - previousKeyFrameLocation;

            JsonArray vectorJsonArray = getKeyFrameVector(keyframe.getValue());
            IValue xValue = parseExpression(parser, vectorJsonArray.get(0));
            IValue yValue = parseExpression(parser, vectorJsonArray.get(1));
            IValue zValue = parseExpression(parser, vectorJsonArray.get(2));

            IValue currentXValue = isRotation && xValue instanceof ConstantValue ? ConstantValue.fromDouble(Math.toRadians(-xValue.get())) : xValue;
            IValue currentYValue = isRotation && yValue instanceof ConstantValue ? ConstantValue.fromDouble(Math.toRadians(-yValue.get())) : yValue;
            IValue currentZValue = isRotation && zValue instanceof ConstantValue ? ConstantValue.fromDouble(Math.toRadians(zValue.get())) : zValue;
            KeyFrame xKeyFrame;
            KeyFrame yKeyFrame;
            KeyFrame zKeyFrame;

            if (keyframe.getValue().isJsonObject() && hasEasingType(keyframe.getValue())) {
                EasingType easingType = getEasingType(keyframe.getValue());
                if (hasEasingArgs(keyframe.getValue())) {
                    List<IValue> easingArgs = getEasingArgs(keyframe.getValue());
                    xKeyFrame = new KeyFrame(AnimationUtils.convertSecondsToTicks(animationTimeDifference), i == 0 ? currentXValue : previousXValue, currentXValue, easingType, easingArgs);
                    yKeyFrame = new KeyFrame(AnimationUtils.convertSecondsToTicks(animationTimeDifference), i == 0 ? currentYValue : previousYValue, currentYValue, easingType, easingArgs);
                    zKeyFrame = new KeyFrame(AnimationUtils.convertSecondsToTicks(animationTimeDifference), i == 0 ? currentZValue : previousZValue, currentZValue, easingType, easingArgs);
                } else {
                    xKeyFrame = new KeyFrame(AnimationUtils.convertSecondsToTicks(animationTimeDifference), i == 0 ? currentXValue : previousXValue, currentXValue, easingType);
                    yKeyFrame = new KeyFrame(AnimationUtils.convertSecondsToTicks(animationTimeDifference), i == 0 ? currentYValue : previousYValue, currentYValue, easingType);
                    zKeyFrame = new KeyFrame(AnimationUtils.convertSecondsToTicks(animationTimeDifference), i == 0 ? currentZValue : previousZValue, currentZValue, easingType);
                }
            } else {
                xKeyFrame = new KeyFrame(AnimationUtils.convertSecondsToTicks(animationTimeDifference), i == 0 ? currentXValue : previousXValue, currentXValue);
                yKeyFrame = new KeyFrame(AnimationUtils.convertSecondsToTicks(animationTimeDifference), i == 0 ? currentYValue : previousYValue, currentYValue);
                zKeyFrame = new KeyFrame(AnimationUtils.convertSecondsToTicks(animationTimeDifference), i == 0 ? currentZValue : previousZValue, currentZValue);
            }

            previousXValue = currentXValue;
            previousYValue = currentYValue;
            previousZValue = currentZValue;

            xKeyFrames.add(xKeyFrame);
            yKeyFrames.add(yKeyFrame);
            zKeyFrames.add(zKeyFrame);
        }

        return new VectorKeyFrameList<>(
                ObjectLists.unmodifiable(xKeyFrames),
                ObjectLists.unmodifiable(yKeyFrames),
                ObjectLists.unmodifiable(zKeyFrames));
    }

    private static JsonArray getKeyFrameVector(JsonElement element) {
        if (element.isJsonArray()) {
            return element.getAsJsonArray();
        } else {
            return element.getAsJsonObject().get("vector").getAsJsonArray();
        }
    }

    private static boolean hasEasingType(JsonElement element) {
        return element.getAsJsonObject().has("easing");
    }

    private static boolean hasEasingArgs(JsonElement element) {
        return element.getAsJsonObject().has("easingArgs");
    }

    private static EasingType getEasingType(JsonElement element) {
        final String easingString = element.getAsJsonObject().get("easing").getAsString();
        try {
            // TODO: 改成标准基岩版的插值类型
            return EasingType.getEasingTypeFromString(easingString);
        } catch (Exception e) {
            TouhouLittleMaid.LOGGER.fatal("Unknown easing type: {}", easingString);
            throw new RuntimeException(e);
        }
    }

    private static List<IValue> getEasingArgs(JsonElement element) {
        JsonObject asJsonObject = element.getAsJsonObject();
        JsonElement easingArgs = asJsonObject.get("easingArgs");
        JsonArray asJsonArray = easingArgs.getAsJsonArray();
        return JsonAnimationUtils.convertJsonArrayToList(asJsonArray);
    }

    public static VectorKeyFrameList<KeyFrame<IValue>> convertJsonToKeyFrames(List<Map.Entry<String, JsonElement>> element, MolangParser parser) throws NumberFormatException, MolangException {
        return convertJson(element, false, parser);
    }

    public static VectorKeyFrameList<KeyFrame<IValue>> convertJsonToRotationKeyFrames(List<Map.Entry<String, JsonElement>> element, MolangParser parser) throws NumberFormatException, MolangException {
        VectorKeyFrameList<KeyFrame<IValue>> frameList = convertJson(element, true, parser);
        return new VectorKeyFrameList(frameList.xKeyFrames, frameList.yKeyFrames, frameList.zKeyFrames);
    }

    public static IValue parseExpression(MolangParser parser, JsonElement element) throws MolangException {
        if (element.getAsJsonPrimitive().isString()) {
            return parser.parseJson(element);
        } else {
            return ConstantValue.fromDouble(element.getAsDouble());
        }
    }
}
