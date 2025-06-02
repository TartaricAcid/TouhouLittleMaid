package com.github.tartaricacid.touhoulittlemaid.ai.service.stt;

public enum STTApiType {
    PLAYER2("player2"),
    ALIYUN("aliyun"),
    SILICONFLOW("siliconflow");

    private final String name;

    STTApiType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static STTApiType getByName(String name) {
        for (STTApiType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return PLAYER2;
    }
}
