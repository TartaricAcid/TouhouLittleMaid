package com.github.tartaricacid.touhoulittlemaid.client.audio.music;

import com.google.gson.annotations.SerializedName;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public class MusicJsonInfo {
    @SerializedName("site")
    private String site;

    @SerializedName("creator")
    private String creator;

    @SerializedName("version")
    private String version;

    @SerializedName("date")
    private String date;

    @SerializedName("desc")
    private String desc;

    @SerializedName("content")
    private Content content;

    public static MusicJsonInfo getDefaultInstance() {
        MusicJsonInfo info = new MusicJsonInfo();
        info.site = "netease";
        info.creator = "null";
        info.version = "1.0.0";
        info.date = "1970-01-01";
        info.desc = "";
        return info;
    }

    public String getSite() {
        return site;
    }

    public String getCreator() {
        return creator;
    }

    public String getVersion() {
        return version;
    }

    public String getDate() {
        return date;
    }

    public String getDesc() {
        return desc;
    }

    public Content getContent() {
        return content;
    }

    public static class Content {
        @SerializedName("play_list")
        private long[] playList;

        public long[] getPlayList() {
            return playList;
        }
    }
}
