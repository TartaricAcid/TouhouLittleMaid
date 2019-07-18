package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TartaricAcid
 * @date 2019/7/17 0:27
 **/
@Mod.EventBusSubscriber(modid = TouhouLittleMaid.MOD_ID)
public final class MaidSoundEvent {
    private static final List<SoundEvent> SOUND_LIST = new ArrayList<>();

    public static final SoundEvent MAID_IDLE = registerSound("maid.mode.idle");
    public static final SoundEvent MAID_ATTACK = registerSound("maid.mode.attack");
    public static final SoundEvent MAID_RANGE_ATTACK = registerSound("maid.mode.range_attack");
    public static final SoundEvent MAID_DANMAKU_ATTACK = registerSound("maid.mode.danmaku_attack");
    public static final SoundEvent MAID_FARM = registerSound("maid.mode.farm");
    public static final SoundEvent MAID_FEED = registerSound("maid.mode.feed");
    public static final SoundEvent MAID_SHEARS = registerSound("maid.mode.shears");
    public static final SoundEvent MAID_TORCH = registerSound("maid.mode.torch");
    public static final SoundEvent MAID_FIND_TARGET = registerSound("maid.ai.find_target");
    public static final SoundEvent MAID_HURT = registerSound("maid.ai.hurt");
    public static final SoundEvent MAID_HURT_FIRE = registerSound("maid.ai.hurt_fire");
    public static final SoundEvent MAID_PLAYER = registerSound("maid.ai.hurt_player");
    public static final SoundEvent MAID_TAMED = registerSound("maid.ai.tamed");
    public static final SoundEvent MAID_ITEM_GET = registerSound("maid.ai.item_get");
    public static final SoundEvent MAID_DEATH = registerSound("maid.ai.death");
    public static final SoundEvent MAID_HOT = registerSound("maid.environment.hot");
    public static final SoundEvent MAID_COLD = registerSound("maid.environment.cold");
    public static final SoundEvent MAID_RAIN = registerSound("maid.environment.rain");
    public static final SoundEvent MAID_SNOW = registerSound("maid.environment.snow");
    public static final SoundEvent MAID_MORNING = registerSound("maid.environment.morning");
    public static final SoundEvent MAID_NIGHT = registerSound("maid.environment.night");
    public static final SoundEvent OTHER_CREDIT = registerSound("other.credit");

    @SubscribeEvent
    public static void register(RegistryEvent.Register<SoundEvent> event) {
        for (SoundEvent sound : SOUND_LIST) {
            event.getRegistry().register(sound);
        }
    }

    private static SoundEvent registerSound(String name) {
        SoundEvent event = new SoundEvent(new ResourceLocation(TouhouLittleMaid.MOD_ID, name))
                .setRegistryName(TouhouLittleMaid.MOD_ID, name);
        SOUND_LIST.add(event);
        return event;
    }
}
