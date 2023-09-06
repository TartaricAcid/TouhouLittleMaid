package com.github.tartaricacid.touhoulittlemaid.client.event;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.config.InGameMaidConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class MaidSoundFreqEvent {
    private static final String MAID_SOUND_PREFIX = "maid.";

    @SubscribeEvent
    public static void onPlaySoundEvent(PlaySoundEvent event) {
        if (event.getSound() == null) {
            return;
        }
        ResourceLocation location = event.getSound().getLocation();
        double soundFrequency = InGameMaidConfig.INSTANCE.getSoundFrequency();
        if (soundFrequency < 1 && checkSoundName(location)) {
            if (Math.random() > soundFrequency) {
                event.setResultSound(null);
            }
        }
    }

    private static boolean checkSoundName(ResourceLocation name) {
        return name.getNamespace().equals(TouhouLittleMaid.MOD_ID) && name.getPath().startsWith(MAID_SOUND_PREFIX);
    }
}
