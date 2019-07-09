package com.github.tartaricacid.touhoulittlemaid.client.model.pojo;

import com.google.gson.annotations.SerializedName;

public class CustomModelPOJO {
    @SerializedName("format_version")
    private String formatVersion;

    @SerializedName("geometry.EntityMaidModel")
    private GeometryEntityMaidModel geometryEntityMaidModel;

    public String getFormatVersion() {
        return formatVersion;
    }

    public GeometryEntityMaidModel getGeometryEntityMaidModel() {
        return geometryEntityMaidModel;
    }

    @Override
    public String toString() {
        return
                "CustomModelPOJO{" +
                        "format_version = '" + formatVersion + '\'' +
                        ",geometry.EntityMaidModel = '" + geometryEntityMaidModel + '\'' +
                        "}";
    }
}