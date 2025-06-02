package com.github.tartaricacid.touhoulittlemaid.ai.service.tts;

public enum TTSApiType {
    PLAYER2("player2"),
    FISH_AUDIO("fish-audio"),
    GPT_SOVITS("gpt-sovits"),
    SILICONFLOW("siliconflow"),
    SYSTEM("system");

    private final String name;

    TTSApiType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
