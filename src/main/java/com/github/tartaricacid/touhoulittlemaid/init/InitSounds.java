package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TouhouLittleMaid.MOD_ID);

    public static final RegistryObject<SoundEvent> MAID_IDLE = registerSound("maid.mode.idle");
    public static final RegistryObject<SoundEvent> MAID_ATTACK = registerSound("maid.mode.attack");
    public static final RegistryObject<SoundEvent> MAID_RANGE_ATTACK = registerSound("maid.mode.range_attack");
    public static final RegistryObject<SoundEvent> MAID_DANMAKU_ATTACK = registerSound("maid.mode.danmaku_attack");
    public static final RegistryObject<SoundEvent> MAID_FARM = registerSound("maid.mode.farm");
    public static final RegistryObject<SoundEvent> MAID_FEED = registerSound("maid.mode.feed");
    public static final RegistryObject<SoundEvent> MAID_SHEARS = registerSound("maid.mode.shears");
    public static final RegistryObject<SoundEvent> MAID_MILK = registerSound("maid.mode.milk");
    public static final RegistryObject<SoundEvent> MAID_REMOVE_SNOW = registerSound("maid.mode.snow");
    public static final RegistryObject<SoundEvent> MAID_TORCH = registerSound("maid.mode.torch");
    public static final RegistryObject<SoundEvent> MAID_FEED_ANIMAL = registerSound("maid.mode.feed_animal");
    public static final RegistryObject<SoundEvent> MAID_EXTINGUISHING = registerSound("maid.mode.extinguishing");
    public static final RegistryObject<SoundEvent> MAID_BREAK = registerSound("maid.mode.break");
    public static final RegistryObject<SoundEvent> MAID_FURNACE = registerSound("maid.mode.furnace");
    public static final RegistryObject<SoundEvent> MAID_BREWING = registerSound("maid.mode.brewing");
    public static final RegistryObject<SoundEvent> MAID_FIND_TARGET = registerSound("maid.ai.find_target");
    public static final RegistryObject<SoundEvent> MAID_HURT = registerSound("maid.ai.hurt");
    public static final RegistryObject<SoundEvent> MAID_HURT_FIRE = registerSound("maid.ai.hurt_fire");
    public static final RegistryObject<SoundEvent> MAID_PLAYER = registerSound("maid.ai.hurt_player");
    public static final RegistryObject<SoundEvent> MAID_TAMED = registerSound("maid.ai.tamed");
    public static final RegistryObject<SoundEvent> MAID_ITEM_GET = registerSound("maid.ai.item_get");
    public static final RegistryObject<SoundEvent> MAID_DEATH = registerSound("maid.ai.death");
    public static final RegistryObject<SoundEvent> MAID_HOT = registerSound("maid.environment.hot");
    public static final RegistryObject<SoundEvent> MAID_COLD = registerSound("maid.environment.cold");
    public static final RegistryObject<SoundEvent> MAID_RAIN = registerSound("maid.environment.rain");
    public static final RegistryObject<SoundEvent> MAID_SNOW = registerSound("maid.environment.snow");
    public static final RegistryObject<SoundEvent> MAID_MORNING = registerSound("maid.environment.morning");
    public static final RegistryObject<SoundEvent> MAID_NIGHT = registerSound("maid.environment.night");
    public static final RegistryObject<SoundEvent> MAID_CREDIT = registerSound("maid.credit");
    public static final RegistryObject<SoundEvent> CAMERA_USE = registerSound("item.camera_use");
    public static final RegistryObject<SoundEvent> ALBUM_OPEN = registerSound("item.album_open");
    public static final RegistryObject<SoundEvent> ALTAR_CRAFT = registerSound("block.altar_craft");
    public static final RegistryObject<SoundEvent> GOMOKU = registerSound("block.gomoku");
    public static final RegistryObject<SoundEvent> GOMOKU_RESET = registerSound("block.gomoku_reset");
    public static final RegistryObject<SoundEvent> BOX_OPEN = registerSound("entity.box");
    public static final RegistryObject<SoundEvent> COMPASS_POINT = registerSound("item.compass");
    public static final RegistryObject<SoundEvent> FAIRY_AMBIENT = registerSound("entity.fairy.ambient");
    public static final RegistryObject<SoundEvent> FAIRY_DEATH = registerSound("entity.fairy.death");
    public static final RegistryObject<SoundEvent> FAIRY_HURT = registerSound("entity.fairy.hurt");

    private static RegistryObject<SoundEvent> registerSound(String name) {
        return SOUNDS.register(name, () -> SoundEvent.createFixedRangeEvent(new ResourceLocation(TouhouLittleMaid.MOD_ID, name), 16.0F));
    }
}
