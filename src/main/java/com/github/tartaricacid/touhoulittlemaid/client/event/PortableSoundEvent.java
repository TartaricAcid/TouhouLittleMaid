package com.github.tartaricacid.touhoulittlemaid.client.event;


import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.audio.PortableAudioSound;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.net.URL;

/**
 * @author TartaricAcid
 */
@SideOnly(Side.CLIENT)
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID, value = Side.CLIENT)
public class PortableSoundEvent {
    private static final float DEFAULT_VOLUME = 1.0f;
    private static final float DEFAULT_DISTANCE = 16.0f;
    private static final String DEFAULT_FORMAT = "mp3";

    @SubscribeEvent
    public static void onSoundPlay(PlaySoundSourceEvent event) {
        ISound sound = event.getSound();
        if (sound instanceof PortableAudioSound) {
            URL songUrl = ((PortableAudioSound) sound).getSongUrl();
            float volume = sound.getVolume();
            float distance = DEFAULT_DISTANCE;
            if (volume > DEFAULT_VOLUME) {
                distance *= volume;
            }
            boolean soundCanRepeat = sound.canRepeat() && sound.getRepeatDelay() == 0;
            try {
                event.getManager().sndSystem.newStreamingSource(false, event.getUuid(),
                        songUrl, DEFAULT_FORMAT, soundCanRepeat,
                        sound.getXPosF(), sound.getYPosF(), sound.getZPosF(),
                        sound.getAttenuationType().getTypeInt(), distance);
                Minecraft.getMinecraft().ingameGUI.setOverlayMessage(I18n.format("info.touhou_little_maid.portable_audio.playing"),
                        true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
