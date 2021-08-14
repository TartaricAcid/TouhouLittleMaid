package com.github.tartaricacid.touhoulittlemaid.client.model;

public enum BedrockVersion {
    /**
     * 旧版本基岩版模型
     */
    LEGACY("1.10.0"),
    /**
     * 新版本基岩版模型
     */
    NEW("1.12.0");

    private final String version;

    BedrockVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
