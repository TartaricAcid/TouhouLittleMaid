package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.request;

public enum OpusBitRate {
    AUTO(-1000),
    LOWEST(24000),
    LOW(32000),
    MEDIUM(48000),
    HIGH(64000);

    private final int bitRate;

    OpusBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getBitRate() {
        return bitRate;
    }
}
