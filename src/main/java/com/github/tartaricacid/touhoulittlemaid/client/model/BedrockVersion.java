package com.github.tartaricacid.touhoulittlemaid.client.model;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.model.pojo.BedrockModelPOJO;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

public enum BedrockVersion {
    /**
     * 旧版本基岩版模型，仅限 1.10.0
     */
    LEGACY("[1.10.0]"),
    /**
     * 新版本基岩版模型，往后的 1.14.0，1.16.0 1.21.0 通通用此版本读取
     */
    NEW("[1.12.0,)");

    private final VersionRange versionRange;

    BedrockVersion(String version) {
        this.versionRange = createFromVersionSpec(version);
    }

    public static boolean isNewVersion(BedrockModelPOJO bedrockModel) {
        DefaultArtifactVersion inputVersion = new DefaultArtifactVersion(bedrockModel.getFormatVersion());
        return NEW.versionRange.containsVersion(inputVersion);
    }

    public static boolean isLegacyVersion(BedrockModelPOJO bedrockModel) {
        DefaultArtifactVersion inputVersion = new DefaultArtifactVersion(bedrockModel.getFormatVersion());
        return LEGACY.versionRange.containsVersion(inputVersion);
    }

    private static VersionRange createFromVersionSpec(final String spec) {
        try {
            return VersionRange.createFromVersionSpec(spec);
        } catch (InvalidVersionSpecificationException e) {
            TouhouLittleMaid.LOGGER.fatal("Failed to parse version spec {}", spec, e);
            throw new RuntimeException("Failed to parse spec", e);
        }
    }
}
