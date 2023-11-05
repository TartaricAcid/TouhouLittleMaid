package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidSoundInstance;
import net.minecraft.client.audio.AudioStreamBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PlayMaidSoundEvent {
    @SubscribeEvent
    public static void onPlaySoundSource(PlaySoundSourceEvent event) {
        if (event.getSound() instanceof MaidSoundInstance) {
            MaidSoundInstance instance = (MaidSoundInstance) event.getSound();
            AudioStreamBuffer soundBuffer = instance.getSoundBuffer();
            if (soundBuffer != null) {
                event.getSource().attachStaticBuffer(instance.getSoundBuffer());
                event.getSource().play();
            }
        }
    }
}
