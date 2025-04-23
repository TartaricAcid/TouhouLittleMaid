package com.github.tartaricacid.touhoulittlemaid.ai.service.tts.fishaudio.request;

public enum OpusBitRate {
    AUTO(-1000),
    LOWEST(24),
    LOW(32),
    MEDIUM(48),
    HIGH(64);

    private final int bitRate;

    OpusBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getBitRate() {
        return bitRate;
    }
}
