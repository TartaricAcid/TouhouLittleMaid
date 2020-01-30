package com.github.tartaricacid.touhoulittlemaid.client.animation.pojo;

import com.google.common.collect.Maps;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * @author TartaricAcid
 * @date 2020/1/29 18:46
 **/
public class KeyFrameItem {
    private HashMap<Float, Float[]> rotationList = Maps.newHashMap();

    private HashMap<Float, Float[]> positionList = Maps.newHashMap();

    public HashMap<Float, Float[]> getRotationList() {
        return rotationList;
    }

    public HashMap<Float, Float[]> getPositionList() {
        return positionList;
    }

    public static class Serializer implements JsonDeserializer<KeyFrameItem> {
        @Override
        public KeyFrameItem deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            KeyFrameItem keyFrameItem = new KeyFrameItem();
            Gson gson = new Gson();

            if (object.has("rotation")) {
                JsonElement element = object.get("rotation");
                if (element.isJsonArray()) {
                    keyFrameItem.rotationList.put(0.0f, gson.fromJson(element, Float[].class));
                } else {
                    keyFrameItem.rotationList = gson.fromJson(element, new TypeToken<HashMap<Float, Float[]>>() {
                    }.getType());
                }
            }

            if (object.has("position")) {
                JsonElement element = object.get("position");
                if (element.isJsonArray()) {
                    keyFrameItem.positionList.put(0.0f, gson.fromJson(element, Float[].class));
                } else {
                    keyFrameItem.positionList = gson.fromJson(element, new TypeToken<HashMap<Float, Float[]>>() {
                    }.getType());
                }
            }
            return keyFrameItem;
        }
    }
}
