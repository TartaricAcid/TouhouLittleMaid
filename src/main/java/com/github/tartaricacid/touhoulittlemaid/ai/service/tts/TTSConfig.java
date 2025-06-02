package com.github.tartaricacid.touhoulittlemaid.ai.service.tts;

/**
 * 语音合成配置
 *
 * @param model    模型名称
 * @param language 语音合成的语种语言，遵循 <a href="https://en.wikipedia.org/wiki/ISO_639-1">ISO 639-1</a> 标准
 */
public record TTSConfig(String model, String language) {
}
