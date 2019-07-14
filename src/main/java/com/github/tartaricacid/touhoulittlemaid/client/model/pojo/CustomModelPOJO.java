package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.gson.annotations.SerializedName;

public class CustomModelPOJO {
    @SerializedName("format_version")
    private String formatVersion;

    @SerializedName("geometry.model")
    private GeometryModel geometryModel;

    public String getFormatVersion() {
        return formatVersion;
    }

    public GeometryModel getGeometryModel() {
        return geometryModel;
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