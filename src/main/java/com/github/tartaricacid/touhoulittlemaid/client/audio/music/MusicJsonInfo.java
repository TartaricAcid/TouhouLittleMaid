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
