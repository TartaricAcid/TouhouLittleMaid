package com.github.tartaricacid.touhoulittlemaid.ai.service.stt;

import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.aliyun.STTAliyunClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.stt.player2.STTPlayer2Client;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;

import javax.annotation.Nullable;
import java.net.http.HttpClient;

public final class STTFactory {
    @Nullable
    public static STTClient getSttClient(HttpClient client) {
        String url = AIConfig.STT_URL.get();

        if (AIConfig.STT_TYPE.get().equals(STTApiType.PLAYER2)) {
            return STTPlayer2Client.create(client).baseUrl(url);
        }

        if (AIConfig.STT_TYPE.get().equals(STTApiType.ALIYUN)) {
            return STTAliyunClient.create(client).baseUrl(url);
        }

        return null;
    }
}
