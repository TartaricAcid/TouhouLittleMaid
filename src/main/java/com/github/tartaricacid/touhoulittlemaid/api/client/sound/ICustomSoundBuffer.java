package com.github.tartaricacid.touhoulittlemaid.api.client.sound;

import com.mojang.blaze3d.audio.SoundBuffer;

import javax.annotation.Nullable;

/**
 * 用来标记女仆模组拥有自定义 Sound Buffer 的音频
 */
public interface ICustomSoundBuffer {
    /**
     * 获取 Sound Buffer
     *
     * @return 可能为 null，当为 null 时，不进行音频播放
     */
    @Nullable
    SoundBuffer getSoundBuffer();
}