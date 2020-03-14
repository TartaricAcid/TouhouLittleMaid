package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.SoundPackInfo;

import java.util.List;

public class CustomSoundResources {
    private String jsonFileName;
    public List<SoundPackInfo> infoList;

    public CustomSoundResources(String jsonFileName, List<SoundPackInfo> infoList) {
        this.jsonFileName = jsonFileName;
        this.infoList = infoList;
    }

    public void clearAll() {
        infoList.clear();
    }

    public String getJsonFileName() {
        return jsonFileName;
    }

    public void putInfo(SoundPackInfo info) {
        this.infoList.add(info);
    }

    public List<SoundPackInfo> getInfoList() {
        return infoList;
    }
}
