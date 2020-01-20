package com.github.tartaricacid.touhoulittlemaid.client.download.pojo;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.util.List;

public class DownloadInfo {
    private static final String[] UNITS = new String[]{"B", "kB", "MB", "GB", "TB"};
    private volatile DownloadStatus status = DownloadStatus.NOT_DOWNLOAD;
    private String formatFileSize = "";

    @SerializedName("author")
    private List<String> author = Lists.newArrayList();

    @SerializedName("history")
    private List<Long> history = Lists.newArrayList();

    @SerializedName("file_name")
    private String fileName = "";

    @SerializedName("file_size")
    private long fileSize;

    @SerializedName("checksum")
    private long checksum;

    @SerializedName("name")
    private String name = "";

    @SerializedName("version")
    private String version = "";

    @SerializedName("url")
    private String url = "";

    @SerializedName("desc")
    private String desc = "";

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

    public List<Long> getHistory() {
        return history;
    }

    public DownloadStatus getStatus() {
        return status;
    }

    public void setStatus(DownloadStatus status) {
        this.status = status;
    }

    public String getFormatFileSize() {
        return formatFileSize;
    }

    public DownloadInfo decorate() {
        this.formatFileSize = readableFileSize(getFileSize());
        return this;
    }
}