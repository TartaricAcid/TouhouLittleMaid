package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, TouhouLittleMaid.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_IDLE = registerSound("maid.mode.idle");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_ATTACK = registerSound("maid.mode.attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_RANGE_ATTACK = registerSound("maid.mode.range_attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_DANMAKU_ATTACK = registerSound("maid.mode.danmaku_attack");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_FARM = registerSound("maid.mode.farm");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_FEED = registerSound("maid.mode.feed");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_SHEARS = registerSound("maid.mode.shears");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_MILK = registerSound("maid.mode.milk");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_REMOVE_SNOW = registerSound("maid.mode.snow");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_TORCH = registerSound("maid.mode.torch");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_FEED_ANIMAL = registerSound("maid.mode.feed_animal");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_EXTINGUISHING = registerSound("maid.mode.extinguishing");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_BREAK = registerSound("maid.mode.break");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_FURNACE = registerSound("maid.mode.furnace");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_BREWING = registerSound("maid.mode.brewing");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_FIND_TARGET = registerSound("maid.ai.find_target");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_HURT = registerSound("maid.ai.hurt");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_HURT_FIRE = registerSound("maid.ai.hurt_fire");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_PLAYER = registerSound("maid.ai.hurt_player");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_TAMED = registerSound("maid.ai.tamed");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_ITEM_GET = registerSound("maid.ai.item_get");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_DEATH = registerSound("maid.ai.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_HOT = registerSound("maid.environment.hot");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_COLD = registerSound("maid.environment.cold");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_RAIN = registerSound("maid.environment.rain");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_SNOW = registerSound("maid.environment.snow");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_MORNING = registerSound("maid.environment.morning");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_NIGHT = registerSound("maid.environment.night");
    public static final DeferredHolder<SoundEvent, SoundEvent> MAID_CREDIT = registerSound("maid.credit");
    public static final DeferredHolder<SoundEvent, SoundEvent> CAMERA_USE = registerSound("item.camera_use");
    public static final DeferredHolder<SoundEvent, SoundEvent> ALBUM_OPEN = registerSound("item.album_open");
    public static final DeferredHolder<SoundEvent, SoundEvent> ALTAR_CRAFT = registerSound("block.altar_craft");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOMOKU = registerSound("block.gomoku");
    public static final DeferredHolder<SoundEvent, SoundEvent> GOMOKU_RESET = registerSound("block.gomoku_reset");
    public static final DeferredHolder<SoundEvent, SoundEvent> BOX_OPEN = registerSound("entity.box");
    public static final DeferredHolder<SoundEvent, SoundEvent> COMPASS_POINT = registerSound("item.compass");
    public static final DeferredHolder<SoundEvent, SoundEvent> FAIRY_AMBIENT = registerSound("entity.fairy.ambient");
    public static final DeferredHolder<SoundEvent, SoundEvent> FAIRY_DEATH = registerSound("entity.fairy.death");
    public static final DeferredHolder<SoundEvent, SoundEvent> FAIRY_HURT = registerSound("entity.fairy.hurt");

    private static DeferredHolder<SoundEvent, SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, name), 16.0F));
    }
}
