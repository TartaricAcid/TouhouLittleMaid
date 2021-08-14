package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Nullable;
import java.util.List;

public class CustomModelPOJO {
    @SerializedName("format_version")
    private String formatVersion;

    @SerializedName("geometry.model")
    @Nullable
    private GeometryModelLegacy geometryModel;

    @SerializedName("minecraft:geometry")
    @Nullable
    private List<GeometryModelNew> geometryModelNew;

    public String getFormatVersion() {
        return formatVersion;
    }

    @Nullable
    public GeometryModelLegacy getGeometryModel() {
        return geometryModel;
    }

    @Nullable
    public GeometryModelNew getGeometryModelNew() {
        if (geometryModelNew == null) {
            return null;
        }
        return geometryModelNew.get(0);
    }

    @Override
    public String toString() {
        return
                "CustomModelPOJO{" +
                        "format_version = '" + formatVersion + '\'' +
                        ",geometry.model = '" + geometryModel + '\'' +
                        "}";
    }
}