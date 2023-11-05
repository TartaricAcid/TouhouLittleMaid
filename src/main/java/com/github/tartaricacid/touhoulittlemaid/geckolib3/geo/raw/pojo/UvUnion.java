package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;


import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;

import java.io.Serializable;
import java.lang.reflect.Type;

@JsonAdapter(UvUnion.Serializer.class)
public class UvUnion implements Serializable {
    public double[] boxUVCoords;
    public UvFaces faceUV;
    public boolean isBoxUV;

    protected static class Serializer implements JsonSerializer<UvUnion>, JsonDeserializer<UvUnion> {
        @Override
                public UvUnion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            UvUnion result = new UvUnion();
            if (json.isJsonArray()) {
                result.isBoxUV = true;
                result.boxUVCoords = context.deserialize(json, double[].class);
            } else if (json.isJsonObject()) {
                result.isBoxUV = false;
                result.faceUV = context.deserialize(json, UvFaces.class);
            }
            return result;
        }

        @Override
                public JsonElement serialize(UvUnion src, Type typeOfSrc, JsonSerializationContext context) {
            return src.isBoxUV ? context.serialize(src.boxUVCoords) : context.serialize(src.faceUV);
        }
    }
}
