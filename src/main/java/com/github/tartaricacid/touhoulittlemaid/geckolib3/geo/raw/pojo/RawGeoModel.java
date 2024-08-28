package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import com.google.gson.annotations.SerializedName;

public class RawGeoModel {
    @SerializedName("format_version")
    private FormatVersion formatVersion;
    @SerializedName("minecraft:geometry")
    private MinecraftGeometry[] minecraftGeometry;

    public FormatVersion getFormatVersion() {
        return formatVersion;
    }

    public MinecraftGeometry[] getMinecraftGeometry() {
        return minecraftGeometry;
    }
}
