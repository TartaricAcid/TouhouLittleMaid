package com.github.tartaricacid.simplebedrockmodel.client.bedrock.pojo;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.lang.reflect.Type;

public class CubesItem {
    private float[] uv;
    private FaceUVsItem faceUv;
    private boolean mirror = false;
    private boolean hasMirror = false;

    @Expose
    @SerializedName("inflate")
    private float inflate;

    @Expose
    @SerializedName("size")
    private float[] size;

    @Expose
    @SerializedName("origin")
    private float[] origin;

    @Expose
    @SerializedName("rotation")
    private float[] rotation;

    @Expose
    @SerializedName("pivot")
    private float[] pivot;

    public float[] getUv() {
        return uv;
    }

    @Nullable
    public FaceUVsItem getFaceUv() {
        return faceUv;
    }

    public boolean isMirror() {
        return mirror;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public boolean isHasMirror() {
        return hasMirror;
    }

    public float getInflate() {
        return inflate;
    }

    /**
     * 基岩版这货居然可以为浮点数，服了
     */
    public float[] getSize() {
        return size;
    }

    public float[] getOrigin() {
        return origin;
    }

    @Nullable
    public float[] getRotation() {
        return rotation;
    }

    @Nullable
    public float[] getPivot() {
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
                    JsonArray array = uvElement.getAsJsonArray();
                    cube.uv = new float[array.size()];
                    for (int i = 0; i < array.size(); i++) {
                        cube.uv[i] = array.get(i).getAsFloat();
                    }
                }
                if (uvElement.isJsonObject()) {
                    cube.faceUv = new Gson().fromJson(uvElement, FaceUVsItem.class);
                }

                JsonElement mirrorElement = obj.get("mirror");
                if (mirrorElement != null && mirrorElement.isJsonPrimitive()) {
                    JsonPrimitive primitive = mirrorElement.getAsJsonPrimitive();
                    if (primitive.isBoolean()) {
                        cube.mirror = primitive.getAsBoolean();
                        cube.hasMirror = true;
                    }
                }
            }
            return cube;
        }
    }
}