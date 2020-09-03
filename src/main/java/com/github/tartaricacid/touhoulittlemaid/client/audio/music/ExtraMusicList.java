package com.github.tartaricacid.touhoulittlemaid.client.audio.music;


import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ExtraMusicList {
    @SerializedName("code")
    private int code;

    @SerializedName("songs")
    private List<NetEaseMusicList.Track> tracks = Lists.newArrayList();

    public int getCode() {
        return code;
    }

    public List<NetEaseMusicList.Track> getTracks() {
        return tracks;
    }
}
