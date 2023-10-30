package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.sound.data.MaidSoundInstance;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.InGameMaidConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class MaidSoundFreqEvent {
    @SubscribeEvent
    public static void onPlaySoundEvent(PlaySoundEvent event) {
        if (event.getSound() instanceof MaidSoundInstance maidSoundInstance) {
            double soundFrequency = InGameMaidConfig.INSTANCE.getSoundFrequency();
            if (soundFrequency < 1 && !maidSoundInstance.isTestSound()) {
                if (Math.random() > soundFrequency) {
                    event.setSound(null);
                }
            }
        }
    }
}