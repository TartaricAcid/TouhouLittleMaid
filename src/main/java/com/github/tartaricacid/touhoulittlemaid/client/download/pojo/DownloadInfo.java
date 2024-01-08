package com.github.tartaricacid.touhoulittlemaid.client.download.pojo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import net.minecraft.util.Mth;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DownloadInfo {
    private static final String[] UNITS = new String[]{"B", "kB", "MB", "GB", "TB"};
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private volatile DownloadStatus status = DownloadStatus.NOT_DOWNLOAD;

    @SerializedName("language")
    private HashMap<String, HashMap<String, String>> language = Maps.newHashMap();

    @SerializedName("author")
    private List<String> author = Lists.newArrayList();

    @SerializedName("file_name")
    private String fileName = "";

    @SerializedName("file_size")
    private long fileSize;

    @SerializedName("checksum")
    private long checksum;

    @SerializedName("upload_time")
    private long uploadTime;

    @SerializedName("name")
    private String name = "";

    @SerializedName("version")
    private String version = "";

    @SerializedName("url")
    private String url = "";

    @SerializedName("desc")
    private String desc = "";

    @SerializedName("license")
    private String license = "All Right Reserved";

    @SerializedName("website")
    private String website = "";

    @SerializedName("keyword")
    private String keyword = "";

    @SerializedName("old_version")
    private List<Long> oldVersion = Lists.newArrayList();

    /**
     * type: maid, chair, sound
     */
    @SerializedName("type")
    private List<String> type = Lists.newArrayList();

    @Expose(deserialize = false)
    private String formatFileSize = "";

    @Expose(deserialize = false)
    private String formatData;

    /**
     * 来自 https://stackoverflow.com/a/5599842
     */
    private static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + UNITS[digitGroups];
    }

    public List<String> getAuthor() {
        return author;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileSize() {
        return fileSize;
    }

    public long getChecksum() {
        return checksum;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getUrl() {
        return url;
    }

    public String getDesc() {
        return desc;
    }

    public HashMap<String, String> getLanguage(String region) {
        return language.get(region);
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
    }

    public String getFormatData() {
        return formatData;
    }

    public String getLicense() {
        return license;
    }

    public String getFormatFileSize() {
        return formatFileSize;
    }

    public String getWebsite() {
        return website;
    }

    public String getKeyword() {
        return keyword;
    }

    public List<Long> getOldVersion() {
        return oldVersion;
    }

    public DownloadInfo decorate() {
        this.formatFileSize = readableFileSize(getFileSize());
        this.formatData = DATE_FORMAT.format(new Date(this.uploadTime));
        if (StringUtils.isNotBlank(this.keyword)) {
            this.keyword = this.keyword.toLowerCase(Locale.US);
        }
        return this;
    }

    public boolean hasType(TypeEnum typeEnum) {
        return type.contains(typeEnum.getName());
    }

    public int getTypeCount() {
        return type.size();
    }

    public enum TypeEnum {
        // 资源包的类型
        MAID, CHAIR, SOUND;

        public static TypeEnum getTypeByIndex(int index) {
            return TypeEnum.values()[Mth.clamp(index, 0, TypeEnum.values().length)];
        }

        public String getName() {
            return this.name().toLowerCase(Locale.US);
        }
    }
}