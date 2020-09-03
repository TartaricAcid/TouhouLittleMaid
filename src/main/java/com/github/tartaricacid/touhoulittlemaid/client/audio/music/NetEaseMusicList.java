package com.github.tartaricacid.touhoulittlemaid.client.audio.music;

import com.google.common.collect.Lists;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.List;


/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
public class NetEaseMusicList {
    @SerializedName(value = "result", alternate = "playlist")
    private PlayList playList;

    @SerializedName("code")
    private int code;

    @Expose(deserialize = false)
    private long listId;

    @Expose(deserialize = false)
    private MusicJsonInfo musicJsonInfo;

    public PlayList getPlayList() {
        return playList;
    }

    public int getCode() {
        return code;
    }

    public MusicJsonInfo getMusicJsonInfo() {
        return musicJsonInfo;
    }

    public void setMusicJsonInfo(MusicJsonInfo musicJsonInfo) {
        this.musicJsonInfo = musicJsonInfo;
    }

    public long getListId() {
        return listId;
    }

    public void setListId(long listId) {
        this.listId = listId;
    }

    public static class Track {
        @SerializedName("id")
        private long id;

        @SerializedName("name")
        private String name;

        @SerializedName(value = "artists", alternate = "ar")
        private List<Artist> artists;

        @SerializedName(value = "album", alternate = "al")
        private Album album;

        @SerializedName(value = "duration", alternate = "dt")
        private int duration;

        @Expose(deserialize = false)
        private String artistStr;

        @Expose(deserialize = false)
        private String albumName;

        @Expose(deserialize = false)
        private String time;

        @Expose(deserialize = false)
        private int timeInTicks;

        public long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getArtists() {
            return artistStr;
        }

        public String getAlbum() {
            return albumName;
        }

        public String getDuration() {
            return time;
        }

        public int getDurationInTicks() {
            return timeInTicks;
        }

        public Track deco() {
            List<String> artistsTmp = Lists.newArrayList();
            artists.forEach(artist -> artistsTmp.add(artist.name));
            artistStr = StringUtils.join(artistsTmp, '/');

            albumName = album.name;

            int min = duration / 60_000;
            int sec = (duration % 60000) / 1000;
            String minStr = min <= 9 ? ("0" + min) : ("" + min);
            String secStr = sec <= 9 ? ("0" + sec) : ("" + sec);
            time = String.format("%s:%s", minStr, secStr);

            timeInTicks = duration / 50;
            return this;
        }
    }

    private static class Creator {
        @SerializedName("nickname")
        private String nickname;
    }

    private static class Artist {
        @SerializedName("name")
        private String name;
    }

    private static class Album {
        @SerializedName("name")
        private String name;
    }

    public class PlayList {
        @SerializedName("name")
        private String name;

        @SerializedName("trackCount")
        private int trackCount;

        @SerializedName("playCount")
        private int playCount;

        @SerializedName("creator")
        private Creator creator;

        @SerializedName("createTime")
        private long createTime;

        @SerializedName("subscribedCount")
        private int subscribedCount;

        @SerializedName("shareCount")
        private int shareCount;

        @SerializedName("tags")
        private List<String> tags = Lists.newArrayList();

        @SerializedName("description")
        private String description = "";

        @SerializedName("tracks")
        private List<Track> tracks;

        @SerializedName("trackIds")
        private List<TrackId> trackIds;

        @Expose(deserialize = false)
        private String creatorName;

        @Expose(deserialize = false)
        private String formatCreateTime;

        @Expose(deserialize = false)
        private String tagStr;

        public String getName() {
            return name;
        }

        public int getTrackCount() {
            return trackCount;
        }

        public int getPlayCount() {
            return playCount;
        }

        public String getCreator() {
            return creatorName;
        }

        public String getCreateTime() {
            return formatCreateTime;
        }

        public int getSubscribedCount() {
            return subscribedCount;
        }

        public int getShareCount() {
            return shareCount;
        }

        public String getTags() {
            return tagStr;
        }

        public String getDescription() {
            return description;
        }

        public List<Track> getTracks() {
            return tracks;
        }

        public List<TrackId> getTrackIds() {
            return trackIds;
        }

        public PlayList deco() {
            creatorName = creator.nickname;
            formatCreateTime = DateFormatUtils.format(createTime, "yyyy-MM-dd");
            tagStr = StringUtils.join(tags, '/');
            tracks.forEach(Track::deco);
            trackCount = tracks.size();
            description = description == null ? "" : description;
            return this;
        }

        public class TrackId {
            @SerializedName("id")
            private long id;

            public long getId() {
                return id;
            }
        }
    }
}
