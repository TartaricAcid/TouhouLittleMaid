package com.github.tartaricacid.touhoulittlemaid.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.List;

public class ServerConfig {
    // 客户端需要下载的包
    public static ForgeConfigSpec.ConfigValue<List<String>> CLIENT_PACK_DOWNLOAD_URLS;

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("The maid pack that the client player needs to download, needs to fill in the URL and the SHA1 value of the file");
        builder.comment("Example: [\"https://www.dropbox.com/download/apple.zip\", \"https://www.dropbox.com/download/cat.zip\"]");
        CLIENT_PACK_DOWNLOAD_URLS = builder.define("ClientPackDownloadUrls", Lists.newArrayList());

        return builder.build();
    }
}
