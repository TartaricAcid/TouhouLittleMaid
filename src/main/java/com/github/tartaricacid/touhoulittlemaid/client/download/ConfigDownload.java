package com.github.tartaricacid.touhoulittlemaid.client.download;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.TeaconConfig;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.List;

public final class ConfigDownload {
    private static final List<String> FILE_SHOULD_DOWNLOAD = Lists.newArrayList();

    public static void checkFile() {
        FILE_SHOULD_DOWNLOAD.clear();
        Collection<File> files = FileUtils.listFiles(CustomPackLoader.PACK_FOLDER.toFile(), new String[]{"zip"}, false);
        for (String name : TeaconConfig.DOWNLOAD_MODEL_PACK.get()) {
            if (files.stream().noneMatch(file -> name.equals(file.getName())) && !FILE_SHOULD_DOWNLOAD.contains(name)) {
                FILE_SHOULD_DOWNLOAD.add(name);
            }
        }
    }

    public static void downloadFile() {
        for (String name : FILE_SHOULD_DOWNLOAD) {
            InfoGetManager.DOWNLOAD_INFO_LIST_ALL.stream()
                    .filter(info -> info.getFileName().equals(name)).findFirst()
                    .ifPresent(InfoGetManager::downloadResourcesPack);
        }
    }
}
