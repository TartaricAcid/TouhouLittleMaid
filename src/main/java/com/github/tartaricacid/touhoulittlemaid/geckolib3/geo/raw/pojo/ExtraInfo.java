package com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

public class ExtraInfo implements Serializable {
    @SerializedName("name")
    private String name;
    @SerializedName("tips")
    private String tips = StringUtils.EMPTY;
    @SerializedName("extra_animation_names")
    private String[] extraAnimationNames = null;
    @SerializedName("authors")
    private String[] authors = null;
    @SerializedName("license")
    private String license = "All Rights Reserved";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String[] getExtraAnimationNames() {
        return extraAnimationNames;
    }

    public void setExtraAnimationNames(String[] extraAnimationNames) {
        this.extraAnimationNames = extraAnimationNames;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
