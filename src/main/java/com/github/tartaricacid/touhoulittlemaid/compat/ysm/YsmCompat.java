package com.github.tartaricacid.touhoulittlemaid.compat.ysm;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

public class YsmCompat {
    private static final String MOD_ID = "yes_steve_model";
    private static final VersionRange VERSION_RANGE;
    private static boolean INSTALLED = false;

    static {
        try {
            VERSION_RANGE = VersionRange.createFromVersionSpec("[2.3.3,)");
        } catch (InvalidVersionSpecificationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void init() {
        ModList.get().getModContainerById(MOD_ID).ifPresent(modContainer -> {
            ArtifactVersion version = modContainer.getModInfo().getVersion();
            if (VERSION_RANGE.containsVersion(version)) {
                INSTALLED = true;
            } else {
                // 开发环境下，version 是空的，所以需要额外判断
                INSTALLED = !FMLEnvironment.production;
            }
        });
    }

    public static boolean isInstalled() {
        return INSTALLED;
    }
}
