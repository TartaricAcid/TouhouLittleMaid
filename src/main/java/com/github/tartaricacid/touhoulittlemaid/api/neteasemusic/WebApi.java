package com.github.tartaricacid.touhoulittlemaid.api.neteasemusic;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author 内个球
 */
public final class WebApi {
    public static final int TYPE_SONG = 1;
    public static final int TYPE_ALBUM = 10;
    public static final int TYPE_SINGER = 100;
    public static final int TYPE_PLAY_LIST = 1000;
    public static final int TYPE_USER = 1002;
    public static final int TYPE_RADIO = 1009;

    private final HashMap<String, String> requestPropertyData;

    public WebApi(HashMap<String, String> requestPropertyData) {
        this.requestPropertyData = requestPropertyData;
    }

    public String search(String key, long size, long page, int type) throws Exception {
        String url = "http://music.163.com/weapi/cloudsearch/get/web?csrf_token=";
        String param = "{\"s\":\"" + key + "\",\"type\":" + type + ",\"offset\":" + (page - 1) * size + ",\"limit\":" + size + ",\"total\":true,\"csrf_token\":\"\"}";
        String encrypt = EncryptUtils.encryptedParam(param);
        return NetWorker.post(url, encrypt, requestPropertyData);
    }

    public String album(long albumId) throws Exception {
        String url = "http://music.163.com/weapi/v1/album/" + albumId + "?id=" + albumId + "&offset=0&total=true&limit=12";
        String param = "{\"album_id\":" + albumId + ",\"csrf_token\":\"\"}";
        String encrypt = EncryptUtils.encryptedParam(param);
        return NetWorker.post(url, encrypt, requestPropertyData);
    }

    public String song(long songId) throws IOException {
        String url = "http://music.163.com/api/song/detail/?id=" + songId + "&ids=%5B" + songId + "%5D";
        return NetWorker.get(url, requestPropertyData);
    }

    public String songs(long... songIds) throws IOException {
        String ids = StringUtils.deleteWhitespace(Arrays.toString(songIds));
        String url = "http://music.163.com/api/song/detail/?ids=" + URLEncoder.encode(ids, "utf-8");
        return NetWorker.get(url, requestPropertyData);
    }

    public String lyric(long songId) throws IOException {
        String url = "http://music.163.com/api/song/lyric/?id=" + songId + "&lv=-1&kv=-1&tv=-1";
        return NetWorker.get(url, requestPropertyData);
    }

    public String mp3(long quality, long... songIds) throws Exception {
        String url = "http://music.163.com/weapi/song/enhance/player/url?csrf_token=";
        String param = "{\"ids\":" + Arrays.toString(songIds) + ",\"br\":" + quality + ",\"csrf_token\":\"\"}";
        String encrypt = EncryptUtils.encryptedParam(param);
        return NetWorker.post(url, encrypt, requestPropertyData);
    }

    public String songComments(long songId, long size, long page) throws Exception {
        String url = "http://music.163.com/weapi/v1/resource/comments/R_SO_4_" + songId + "?csrf_token=";
        String param = "{\"id\":" + songId + ",\"offset\":" + (page - 1) * size + ",\"limit\":" + size + ",\"total\":true ,\"csrf_token\":\"\"}";
        String encrypt = EncryptUtils.encryptedParam(param);
        return NetWorker.post(url, encrypt, requestPropertyData);
    }

    public String listComments(long listId, long size, long page) throws Exception {
        String url = "http://music.163.com/weapi/v1/resource/comments/A_PL_0_" + listId + "?csrf_token=";
        String param = "{\"id\":" + listId + ",\"offset\":" + (page - 1) * size + ",\"limit\":" + size + ",\"total\":true ,\"csrf_token\":\"\"}";
        String encrypt = EncryptUtils.encryptedParam(param);
        return NetWorker.post(url, encrypt, requestPropertyData);
    }

    public String list(long listId) throws Exception {
        String url = "http://music.163.com/weapi/v3/playlist/detail?csrf_token=";
        String param = "{\"id\":" + listId + ",\"csrf_token\":\"\"}";
        String encrypt = EncryptUtils.encryptedParam(param);
        return NetWorker.post(url, encrypt, requestPropertyData);
    }

    public String userAllList(long userId, long size, long page) throws Exception {
        String url = "http://music.163.com/api/user/playlist/?uid=" + userId + "&offset=0&total=true&limit=1000";
        return NetWorker.get(url, requestPropertyData);
    }
}
