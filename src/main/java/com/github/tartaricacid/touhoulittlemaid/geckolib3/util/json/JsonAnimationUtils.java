/*
 * Copyright (c) 2020.
 * Author: Bernie G. (Gecko)
 */

package com.github.tartaricacid.touhoulittlemaid.geckolib3.util.json;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.Animation;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.ILoopType;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.BoneAnimation;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.EventKeyFrame;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.ParticleEventKeyFrame;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.keyframe.VectorKeyFrameList;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.AnimationUtils;
import com.github.tartaricacid.touhoulittlemaid.mclib.math.IValue;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.gson.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.util.JSONException;

import java.util.*;

@SuppressWarnings({"unchecked", "rawtypes"})
public class JsonAnimationUtils {
    private static Gson GSON = null;

    public static Set<Map.Entry<String, JsonElement>> getAnimations(JsonObject json) {
        return json.getAsJsonObject("animations").entrySet();
    }

    public static List<Map.Entry<String, JsonElement>> getBones(JsonObject json) {
        JsonObject bones = json.getAsJsonObject("bones");
        return bones == null ? Collections.emptyList() : new ArrayList<>(bones.entrySet());
    }

    public static Set<Map.Entry<String, JsonElement>> getRotationKeyFrames(JsonObject json) {
        JsonElement rotationObject = json.get("rotation");
        if (rotationObject.isJsonArray()) {
            return ImmutableSet.of(new AbstractMap.SimpleEntry("0", rotationObject.getAsJsonArray()));
        }
        if (rotationObject.isJsonPrimitive()) {
            JsonPrimitive primitive = rotationObject.getAsJsonPrimitive();
            JsonElement jsonElement = getGson().toJsonTree(Arrays.asList(primitive, primitive, primitive));
            return ImmutableSet.of(new AbstractMap.SimpleEntry("0", jsonElement));
        }
        Set<Map.Entry<String, JsonElement>> output = Sets.newLinkedHashSet();
        if (rotationObject.isJsonObject()) {
            JsonObject jsonObject = rotationObject.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entrySet : jsonObject.entrySet()) {
                if (entrySet.getValue().isJsonObject()) {
                    JsonObject valueObject = entrySet.getValue().getAsJsonObject();
                    if (valueObject.has("post")) {
                        output.add(new AbstractMap.SimpleEntry<>(entrySet.getKey(), valueObject.get("post")));
                    }
                } else {
                    output.add(new AbstractMap.SimpleEntry<>(entrySet.getKey(), entrySet.getValue()));
                }
            }
        }
        return output;
    }

    public static Set<Map.Entry<String, JsonElement>> getPositionKeyFrames(JsonObject json) {
        JsonElement positionObject = json.get("position");
        if (positionObject.isJsonArray()) {
            return ImmutableSet.of(new AbstractMap.SimpleEntry("0", positionObject.getAsJsonArray()));
        }
        if (positionObject.isJsonPrimitive()) {
            JsonPrimitive primitive = positionObject.getAsJsonPrimitive();
            JsonElement jsonElement = getGson().toJsonTree(Arrays.asList(primitive, primitive, primitive));
            return ImmutableSet.of(new AbstractMap.SimpleEntry("0", jsonElement));
        }
        Set<Map.Entry<String, JsonElement>> output = Sets.newLinkedHashSet();
        if (positionObject.isJsonObject()) {
            JsonObject jsonObject = positionObject.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entrySet : jsonObject.entrySet()) {
                if (entrySet.getValue().isJsonObject()) {
                    JsonObject valueObject = entrySet.getValue().getAsJsonObject();
                    if (valueObject.has("post")) {
                        output.add(new AbstractMap.SimpleEntry<>(entrySet.getKey(), valueObject.get("post")));
                    }
                } else {
                    output.add(new AbstractMap.SimpleEntry<>(entrySet.getKey(), entrySet.getValue()));
                }
            }
        }
        return output;
    }

    public static Set<Map.Entry<String, JsonElement>> getScaleKeyFrames(JsonObject json) {
        JsonElement scaleObject = json.get("scale");
        if (scaleObject.isJsonArray()) {
            return ImmutableSet.of(new AbstractMap.SimpleEntry("0", scaleObject.getAsJsonArray()));
        }
        if (scaleObject.isJsonPrimitive()) {
            JsonPrimitive primitive = scaleObject.getAsJsonPrimitive();
            JsonElement jsonElement = getGson().toJsonTree(Arrays.asList(primitive, primitive, primitive));
            return ImmutableSet.of(new AbstractMap.SimpleEntry("0", jsonElement));
        }
        Set<Map.Entry<String, JsonElement>> output = Sets.newLinkedHashSet();
        if (scaleObject.isJsonObject()) {
            JsonObject jsonObject = scaleObject.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entrySet : jsonObject.entrySet()) {
                if (entrySet.getValue().isJsonObject()) {
                    JsonObject valueObject = entrySet.getValue().getAsJsonObject();
                    if (valueObject.has("post")) {
                        output.add(new AbstractMap.SimpleEntry<>(entrySet.getKey(), valueObject.get("post")));
                    }
                } else {
                    output.add(new AbstractMap.SimpleEntry<>(entrySet.getKey(), entrySet.getValue()));
                }
            }
        }
        return output;
    }

    public static List<Map.Entry<String, JsonElement>> getSoundEffectFrames(JsonObject json) {
        JsonObject soundEffects = json.getAsJsonObject("sound_effects");
        return soundEffects == null ? Collections.emptyList() : new ObjectArrayList<>(soundEffects.entrySet());
    }

    public static List<Map.Entry<String, JsonElement>> getParticleEffectFrames(JsonObject json) {
        JsonObject particleEffects = json.getAsJsonObject("particle_effects");
        return particleEffects == null ? Collections.emptyList() : new ObjectArrayList<>(particleEffects.entrySet());
    }

    public static List<Map.Entry<String, JsonElement>> getCustomInstructionKeyFrames(JsonObject json) {
        JsonObject customInstructions = json.getAsJsonObject("timeline");
        return customInstructions == null ? Collections.emptyList() : new ArrayList<>(customInstructions.entrySet());
    }

    private static JsonElement getObjectByKey(Set<Map.Entry<String, JsonElement>> json, String key)
            throws JSONException {
        for (Map.Entry<String, JsonElement> entry : json) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        throw new JSONException("Could not find key: " + key);
    }

    public static Map.Entry<String, JsonElement> getAnimation(JsonObject animationFile, String animationName)
            throws JSONException {
        return new AbstractMap.SimpleEntry(animationName, getObjectByKey(getAnimations(animationFile), animationName));
    }

    public static Set<Map.Entry<String, JsonElement>> getObjectListAsArray(JsonObject json) {
        return json.entrySet();
    }

    public static Animation deserializeJsonToAnimation(Map.Entry<String, JsonElement> element, MolangParser parser)
            throws ClassCastException, IllegalStateException {
        Animation animation = new Animation();
        JsonObject animationJsonObject = element.getValue().getAsJsonObject();

        // 设置有关动画的一些 metadata
        animation.animationName = element.getKey();
        JsonElement animationLength = animationJsonObject.get("animation_length");
        animation.animationLength = animationLength == null ? -1
                : AnimationUtils.convertSecondsToTicks(animationLength.getAsDouble());
        animation.boneAnimations = new ObjectArrayList<>();
        animation.loop = ILoopType.fromJson(animationJsonObject.get("loop"));

        // 处理声音关键帧
        for (Map.Entry<String, JsonElement> keyFrame : getSoundEffectFrames(animationJsonObject)) {
            animation.soundKeyFrames.add(new EventKeyFrame<>(Double.parseDouble(keyFrame.getKey()) * 20,
                    keyFrame.getValue().getAsJsonObject().get("effect").getAsString()));
        }

        // 处理粒子关键帧
        for (Map.Entry<String, JsonElement> keyFrame : getParticleEffectFrames(animationJsonObject)) {
            JsonObject object = keyFrame.getValue().getAsJsonObject();
            JsonElement effect = object.get("effect");
            JsonElement locator = object.get("locator");
            JsonElement preEffectScript = object.get("pre_effect_script");
            animation.particleKeyFrames.add(new ParticleEventKeyFrame(Double.parseDouble(keyFrame.getKey()) * 20,
                    effect == null ? "" : effect.getAsString(), locator == null ? "" : locator.getAsString(),
                    preEffectScript == null ? "" : preEffectScript.getAsString()));
        }

        // 处理自定义指令关键帧
        for (Map.Entry<String, JsonElement> keyFrame : getCustomInstructionKeyFrames(animationJsonObject)) {
            animation.customInstructionKeyframes.add(new EventKeyFrame(Double.parseDouble(keyFrame.getKey()) * 20,
                    keyFrame.getValue() instanceof JsonArray
                            ? convertJsonArrayToList(keyFrame.getValue().getAsJsonArray()).toString()
                            : keyFrame.getValue().getAsString()));
        }

        // 此动画中使用的所有骨骼的列表
        for (Map.Entry<String, JsonElement> bone : getBones(animationJsonObject)) {
            BoneAnimation boneAnimation = new BoneAnimation(bone.getKey());
            JsonObject boneJsonObj = bone.getValue().getAsJsonObject();
            try {
                boneAnimation.scaleKeyFrames = JsonKeyFrameUtils.convertJsonToKeyFrames(new ObjectArrayList<>(getScaleKeyFrames(boneJsonObj)), parser);
            } catch (Exception e) {
                // 没有缩放关键帧
                boneAnimation.scaleKeyFrames = new VectorKeyFrameList<>();
            }

            try {
                boneAnimation.positionKeyFrames = JsonKeyFrameUtils.convertJsonToKeyFrames(new ObjectArrayList<>(getPositionKeyFrames(boneJsonObj)), parser);
            } catch (Exception e) {
                // 没有位置关键帧
                boneAnimation.positionKeyFrames = new VectorKeyFrameList<>();
            }

            try {
                boneAnimation.rotationKeyFrames = JsonKeyFrameUtils.convertJsonToRotationKeyFrames(new ObjectArrayList<>(getRotationKeyFrames(boneJsonObj)), parser);
            } catch (Exception e) {
                // 没有旋转关键帧
                boneAnimation.rotationKeyFrames = new VectorKeyFrameList<>();
            }

            animation.boneAnimations.add(boneAnimation);
        }
        if (animation.animationLength == -1) {
            animation.animationLength = calculateLength(animation.boneAnimations);
        }
        return animation;
    }

    private static double calculateLength(List<BoneAnimation> boneAnimations) {
        double longestLength = 0;
        for (BoneAnimation animation : boneAnimations) {
            double xKeyframeTime = animation.rotationKeyFrames.getLastKeyframeTime();
            double yKeyframeTime = animation.positionKeyFrames.getLastKeyframeTime();
            double zKeyframeTime = animation.scaleKeyFrames.getLastKeyframeTime();
            longestLength = maxAll(longestLength, xKeyframeTime, yKeyframeTime, zKeyframeTime);
        }
        return longestLength == 0 ? Double.MAX_VALUE : longestLength;
    }

    static List<IValue> convertJsonArrayToList(JsonArray array) {
        return getGson().fromJson(array, ArrayList.class);
    }

    private static Gson getGson() {
        if (GSON == null) {
            GSON = new Gson();
        }
        return GSON;
    }

    public static double maxAll(double... values) {
        double max = 0;
        for (double value : values) {
            max = Math.max(value, max);
        }
        return max;
    }
}
