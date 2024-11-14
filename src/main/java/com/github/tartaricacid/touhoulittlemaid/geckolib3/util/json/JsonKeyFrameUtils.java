/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.util.json;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.bone.EasingType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.bone.RawBoneKeyFrame;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.value.IValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import static com.github.tartaricacid.touhoulittlemaid.geckolib3.util.AnimationUtils.convertSecondsToTicks;

/**
 * 用于将 json 转换成关键帧的工具类
 */
public class JsonKeyFrameUtils {
    public static void getKeyFrames(List<RawBoneKeyFrame> boneKeyFrames, @Nullable JsonElement element, MolangParser parser) throws NumberFormatException {
        if (element == null) {
            return;
        }

        if (element.isJsonPrimitive() || element.isJsonArray()) {
            RawBoneKeyFrame keyframe = new RawBoneKeyFrame();
            keyframe.startTick = 0;
            readPreKeyFrame(element, keyframe, parser);
            boneKeyFrames.add(keyframe);
            return;
        }

        if (!element.isJsonObject()) {
            return;
        }

        JsonObject obj = element.getAsJsonObject();
        for (String time : obj.keySet()) {
            RawBoneKeyFrame keyframe = new RawBoneKeyFrame();
            keyframe.startTick = convertSecondsToTicks(Double.parseDouble(time));

            JsonElement item = obj.get(time);
            if (item.isJsonPrimitive() || item.isJsonArray()) {
                readPreKeyFrame(item, keyframe, parser);
            } else if (item.isJsonObject()) {
                JsonObject jsonObject = item.getAsJsonObject();

                if (jsonObject.has("vector")) {
                    JsonElement vector = jsonObject.get("vector");
                    JsonElement easing = jsonObject.get("easing");

                    readPreKeyFrame(vector, keyframe, parser);
                    tryGetEasingType(easing, keyframe);
                } else {
                    JsonElement pre = jsonObject.get("pre");
                    JsonElement post = jsonObject.get("post");
                    JsonElement easing = jsonObject.get("lerp_mode");

                    if (pre != null && post != null) {
                        readPreKeyFrame(pre, keyframe, parser);
                        readPostKeyFrame(post, keyframe, parser);
                        keyframe.contiguous = false;
                    } else {
                        if (pre != null) {
                            readPreKeyFrame(pre, keyframe, parser);
                        } else if (post != null) {
                            // 没错，post 赋给 pre
                            readPreKeyFrame(post, keyframe, parser);
                        }
                    }
                    tryGetEasingType(easing, keyframe);
                }
            }

            boneKeyFrames.add(keyframe);
        }

        // 排序，因为 json 读取是乱序的
        boneKeyFrames.sort(Comparator.comparingDouble(k -> k.startTick));
    }

    private static void tryGetEasingType(JsonElement element, RawBoneKeyFrame keyframe) {
        if (element == null || !element.isJsonPrimitive() || !element.getAsJsonPrimitive().isString()) {
            return;
        }
        String easingTypeText = element.getAsJsonPrimitive().getAsString().toLowerCase(Locale.ENGLISH);
        if ("linear".equals(easingTypeText)) {
            keyframe.easingType = EasingType.LINEAR;
        } else if ("catmullrom".equals(easingTypeText)) {
            keyframe.easingType = EasingType.CATMULLROM;
        }
    }

    private static void readPreKeyFrame(JsonElement element, RawBoneKeyFrame keyframe, MolangParser parser) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                IValue value = parser.parseExpression(primitive.getAsString());
                keyframe.preXValue = value;
                keyframe.preYValue = value;
                keyframe.preZValue = value;
            } else if (primitive.isNumber()) {
                double value = primitive.getAsDouble();
                keyframe.preX = value;
                keyframe.preY = value;
                keyframe.preZ = value;
            }
            return;
        }

        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            if (array.size() <= 0) {
                return;
            }

            if (array.size() >= 3) {
                JsonPrimitive xPri = array.get(0).getAsJsonPrimitive();
                JsonPrimitive yPri = array.get(1).getAsJsonPrimitive();
                JsonPrimitive zPri = array.get(2).getAsJsonPrimitive();

                if (xPri.isString()) {
                    keyframe.preXValue = parser.parseExpression(xPri.getAsString());
                } else if (xPri.isNumber()) {
                    keyframe.preX = xPri.getAsDouble();
                }

                if (yPri.isString()) {
                    keyframe.preYValue = parser.parseExpression(yPri.getAsString());
                } else if (yPri.isNumber()) {
                    keyframe.preY = yPri.getAsDouble();
                }

                if (zPri.isString()) {
                    keyframe.preZValue = parser.parseExpression(zPri.getAsString());
                } else if (zPri.isNumber()) {
                    keyframe.preZ = zPri.getAsDouble();
                }

                return;
            }

            JsonPrimitive primitive = array.get(0).getAsJsonPrimitive();
            if (primitive.isString()) {
                IValue value = parser.parseExpression(primitive.getAsString());
                keyframe.preXValue = value;
                keyframe.preYValue = value;
                keyframe.preZValue = value;
            } else if (primitive.isNumber()) {
                double value = primitive.getAsDouble();
                keyframe.preX = value;
                keyframe.preY = value;
                keyframe.preZ = value;
            }
        }
    }

    private static void readPostKeyFrame(JsonElement element, RawBoneKeyFrame keyframe, MolangParser parser) {
        if (element.isJsonPrimitive()) {
            JsonPrimitive primitive = element.getAsJsonPrimitive();
            if (primitive.isString()) {
                IValue value = parser.parseExpression(primitive.getAsString());
                keyframe.postXValue = value;
                keyframe.postYValue = value;
                keyframe.postZValue = value;
            } else if (primitive.isNumber()) {
                double value = primitive.getAsDouble();
                keyframe.postX = value;
                keyframe.postY = value;
                keyframe.postZ = value;
            }
            return;
        }

        if (element.isJsonArray()) {
            JsonArray array = element.getAsJsonArray();
            if (array.size() <= 0) {
                return;
            }

            if (array.size() >= 3) {
                JsonPrimitive xPri = array.get(0).getAsJsonPrimitive();
                JsonPrimitive yPri = array.get(1).getAsJsonPrimitive();
                JsonPrimitive zPri = array.get(2).getAsJsonPrimitive();

                if (xPri.isString()) {
                    keyframe.postXValue = parser.parseExpression(xPri.getAsString());
                } else if (xPri.isNumber()) {
                    keyframe.postX = xPri.getAsDouble();
                }

                if (yPri.isString()) {
                    keyframe.postYValue = parser.parseExpression(yPri.getAsString());
                } else if (yPri.isNumber()) {
                    keyframe.postY = yPri.getAsDouble();
                }

                if (zPri.isString()) {
                    keyframe.postZValue = parser.parseExpression(zPri.getAsString());
                } else if (zPri.isNumber()) {
                    keyframe.postZ = zPri.getAsDouble();
                }

                return;
            }

            JsonPrimitive primitive = array.get(0).getAsJsonPrimitive();
            if (primitive.isString()) {
                IValue value = parser.parseExpression(primitive.getAsString());
                keyframe.postXValue = value;
                keyframe.postYValue = value;
                keyframe.postZValue = value;
            } else if (primitive.isNumber()) {
                double value = primitive.getAsDouble();
                keyframe.postX = value;
                keyframe.postY = value;
                keyframe.postZ = value;
            }
        }
    }
}
