package com.github.tartaricacid.touhoulittlemaid.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ServerConfig {
    // 客户端需要下载的包
    public static ForgeConfigSpec.ConfigValue<List<List<String>>> CLIENT_PACK_DOWNLOAD_URLS;

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("The maid pack that the client player needs to download, needs to fill in the URL and the SHA1 value of the file");
        builder.comment("Example: [[\"https://www.dropbox.com/download/apple.zip\", \"ed5747c5f1a858d51fa0dac43182a683ecaeb124\"], [\"https://www.dropbox.com/download/cat.zip\", \"bc905aa63c662e675c780b22e8dda3d2394670e7\"]]");
        CLIENT_PACK_DOWNLOAD_URLS = builder.define("ClientPackDownloadUrls", Lists.newArrayList());

        return builder.build();
    }
}
