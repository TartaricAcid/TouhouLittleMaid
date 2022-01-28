package com.github.tartaricacid.touhoulittlemaid.client.download.pojo;

public enum DownloadStatus {
    // 未下载
    NOT_DOWNLOAD,
    // 已下载
    DOWNLOADED,
    // 下载中
    DOWNLOADING,
    // 需要更新
    NEED_UPDATE;

    public static boolean canDownload(DownloadStatus status) {
        return status == NOT_DOWNLOAD || status == NEED_UPDATE;
    }
}
