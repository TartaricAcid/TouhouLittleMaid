package com.github.tartaricacid.touhoulittlemaid.client.download;

import com.github.tartaricacid.touhoulittlemaid.client.resource.CustomPackLoader;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.TeaconConfig;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ConfigDownload {
    private static final List<String> FILE_SHOULD_DOWNLOAD = Lists.newArrayList();

    public static void checkFile() {
        FILE_SHOULD_DOWNLOAD.clear();
        Collection<File> files = FileUtils.listFiles(CustomPackLoader.PACK_FOLDER.toFile(), new String[]{"zip"}, false);
        Set<String> configList = new HashSet<>(TeaconConfig.DOWNLOAD_MODEL_PACK.get());
        configList.add("lilies_model_pack-1.1.5.zip");
        configList.add("arcaea_maid_pack-1.0.2.zip");
        configList.add("genshin_impact-1.1.5.zip");
        configList.add("national_sachet-1.0.6.zip");
        configList.add("azuru_line-1.1.0.zip");
        configList.add("plants_vs_zombies-1.0.6.zip");
        configList.add("tartaric_acid_things-1.1.1.zip");
        configList.add("mimikko-1.3.4.zip");
        configList.add("the_journey_of_elaina-1.3.1.zip");
        configList.add("senren_banka-1.0.0.zip");
        configList.add("hanaya_baka-1.1.4.zip");
        configList.add("buildcraft_robot-1.0.0.zip");
        configList.add("ascmtp-1.0.0.zip");
        for (String name : configList) {
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
