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
    public static final SoundEvent MAID_MILK = registerSound("maid.mode.milk");
    public static final SoundEvent MAID_TORCH = registerSound("maid.mode.torch");
    public static final SoundEvent MAID_FEED_ANIMAL = registerSound("maid.mode.feed_animal");
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
    public static final SoundEvent CAMERA_USE = registerSound("item.camera_use");
    public static final SoundEvent ALBUM_OPEN = registerSound("item.album_open");
    public static final SoundEvent ALTAR_CRAFT = registerSound("block.altar_craft");
    public static final SoundEvent RINNOSUKE_AMBIENT = registerSound("monster.rinnosuke_ambient");
    public static final SoundEvent RINNOSUKE_DEATH = registerSound("monster.rinnosuke_death");
    public static final SoundEvent RINNOSUKE_HURT = registerSound("monster.rinnosuke_hurt");
    public static final SoundEvent BOX_OPEN = registerSound("entity.box");

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
