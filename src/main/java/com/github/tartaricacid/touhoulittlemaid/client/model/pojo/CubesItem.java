package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.common.collect.Lists;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.lang.reflect.Type;
import java.util.List;

public class CubesItem {
    private List<Float> uv;
    private FaceUVsItem faceUv;

    @Expose
    @SerializedName("mirror")
    private boolean mirror;

    @Expose
    @SerializedName("inflate")
    private float inflate;

    @Expose
    @SerializedName("size")
    private List<Float> size;

    @Expose
    @SerializedName("origin")
    private List<Float> origin;

    @Expose
    @SerializedName("rotation")
    private List<Float> rotation;

    @Expose
    @SerializedName("pivot")
    private List<Float> pivot;

    public List<Float> getUv() {
        return uv;
    }

    @Nullable
    public FaceUVsItem getFaceUv() {
        return faceUv;
    }

    public boolean isMirror() {
        return mirror;
    }

    public float getInflate() {
        return inflate;
    }

    /**
     * 基岩版这货居然可以为浮点数，服了
     */
    public List<Float> getSize() {
        return size;
    }

    public List<Float> getOrigin() {
        return origin;
    }

    @Nullable
    public List<Float> getRotation() {
        return rotation;
    }

    @Nullable
    public List<Float> getPivot() {
        return pivot;
    }

    public static class Deserializer implements JsonDeserializer<CubesItem> {
        @Override
        public CubesItem deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            CubesItem cube = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(json, CubesItem.class);
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                JsonElement uvElement = obj.get("uv");
                if (uvElement.isJsonArray()) {
                    cube.uv = Lists.newArrayList();
                    JsonArray array = uvElement.getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {
                        cube.uv.add(array.get(i).getAsFloat());
                    }
                    return cube;
                }
                if (uvElement.isJsonObject()) {
                    cube.faceUv = new Gson().fromJson(uvElement, FaceUVsItem.class);
                    return cube;
                }
            }
            return null;
        }
    }
}