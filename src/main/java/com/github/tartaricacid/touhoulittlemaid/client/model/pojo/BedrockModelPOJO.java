package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.util.List;

public class BedrockModelPOJO {
    @SerializedName("format_version")
    private String formatVersion;

    @SerializedName("geometry.model")
    @Nullable
    private GeometryModelLegacy geometryModelLegacy;

    @SerializedName("minecraft:geometry")
    @Nullable
    private List<GeometryModelNew> geometryModelNew;

    public String getFormatVersion() {
        return formatVersion;
    }

    @Nullable
    public GeometryModelLegacy getGeometryModelLegacy() {
        return geometryModelLegacy;
    }

    @Nullable
    public GeometryModelNew getGeometryModelNew() {
        if (geometryModelNew == null) {
            return null;
        }
        return geometryModelNew.getFirst();
    }
}