package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.system;

import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SystemClient implements TTSClient {
    @Override
    public void play(String message, TTSConfig config, ResponseCallback<byte[]> callback) {
        if (Dist.CLIENT.isClient()) {
            onHandle(message);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(String message) {
        Minecraft mc = Minecraft.getInstance();
        mc.getNarrator().narrator.say(message, true);
    }
}
