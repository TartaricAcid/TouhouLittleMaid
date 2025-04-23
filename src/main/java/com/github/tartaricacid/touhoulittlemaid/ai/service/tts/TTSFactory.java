package com.github.tartaricacid.touhoulittlemaid.ai.service.tts;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.setting.Site;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.TTSFishAudioClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.request.Format;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.request.OpusBitRate;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.request.TTSFishAudioRequest;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits.TTSGptSovitsClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.gptsovits.request.TTSGptSovitsRequest;

import javax.annotation.Nullable;
import java.net.http.HttpClient;

public final class TTSFactory {
    @Nullable
    public static TTSClient<?> getTtsClient(HttpClient client, Site site) {
        if (site.getApiType().equals(TTSApiType.FISH_AUDIO.getName())) {
            String ttsApiKey = site.getApiKey();
            String ttsBaseUrl = site.getUrl();
            return TTSFishAudioClient.create(client)
                    .apiKey(ttsApiKey)
                    .baseUrl(ttsBaseUrl);
        }

        if (site.getApiType().equals(TTSApiType.GPT_SOVITS.getName())) {
            String ttsApiKey = site.getApiKey();
            String ttsBaseUrl = site.getUrl();
            return TTSGptSovitsClient.create(client)
                    .apiKey(ttsApiKey)
                    .baseUrl(ttsBaseUrl);
        }

        return null;
    }

    @Nullable
    public static TTSRequest getTtsRequest(Site site, String ttsText, String ttsLang, String model) {
        if (site.getApiType().equals(TTSApiType.FISH_AUDIO.getName())) {
            return TTSFishAudioRequest.create()
                    .setReferenceId(model)
                    .setFormat(Format.OPUS)
                    // OPUS 极低比特率情况下，音质效果也还不错
                    .setOpusBitrate(OpusBitRate.LOWEST)
                    .setText(ttsText);
        }

        if (site.getApiType().equals(TTSApiType.GPT_SOVITS.getName())) {
            return TTSGptSovitsRequest.create()
                    .setText(ttsText)
                    .setTextLang(ttsLang)
                    .setSiteExtraArgs(site);
        }

        return null;
    }
}
