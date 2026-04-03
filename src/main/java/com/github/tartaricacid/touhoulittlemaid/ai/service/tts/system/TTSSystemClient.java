package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.system;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.entity.TTSCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSConfig;
import com.github.tartaricacid.touhoulittlemaid.ai.service.tts.TTSSystemServices;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TTSSystemClient implements TTSClient, TTSSystemServices {
    @Override
    public void play(String message, TTSConfig config, TTSCallback callback) {
        if (isClient()) {
            onHandle(message);
        }
    }

    @OnlyIn(Dist.CLIENT)
    private void onHandle(String message) {
        Minecraft mc = Minecraft.getInstance();
        mc.getNarrator().narrator.say(message, true);
    }
}
