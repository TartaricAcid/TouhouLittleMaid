package com.github.tartaricacid.touhoulittlemaid.ai.service.stt.tencent;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Base64;

public record RequestMessage(
        @SerializedName("EngSerViceType") String engSerViceType,
        @SerializedName("SourceType") int sourceType,
        @SerializedName("VoiceFormat") String voiceFormat,
        @SerializedName("Data") String data,
        @SerializedName("DataLen") int dataLen,
        @Nullable @SerializedName("HotwordList") String hotWord
) {
    private static final int SOURCE_TYPE_POST_BODY = 1;

    public static RequestMessage createWav(STTTencentSite site, byte[] data) {
        int dataLen = data.length;
        // 将 data 变成 Base64 编码的字符串
        String dataBase64 = Base64.getEncoder().encodeToString(data);
        String type = site.getEngSerViceType();
        @Nullable String hotWord = StringUtils.trimToNull(site.getHotWord());
        return new RequestMessage(type, SOURCE_TYPE_POST_BODY, "wav", dataBase64, dataLen, hotWord);
    }
}
