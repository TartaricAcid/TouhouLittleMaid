package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RawGeoModel implements Serializable {
    @SerializedName("format_version")
    private FormatVersion formatVersion;
    @SerializedName("minecraft:geometry")
    private MinecraftGeometry[] minecraftGeometry;

    public FormatVersion getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(FormatVersion value) {
        this.formatVersion = value;
    }

    public MinecraftGeometry[] getMinecraftGeometry() {
        return minecraftGeometry;
    }

    public void setMinecraftGeometry(MinecraftGeometry[] value) {
        this.minecraftGeometry = value;
    }
}
