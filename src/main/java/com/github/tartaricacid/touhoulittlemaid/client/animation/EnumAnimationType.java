package com.github.tartaricacid.touhoulittlemaid.client.animation;

/**
 * @author TartaricAcid
 * @date 2020/1/31 01:00
 **/
public enum EnumAnimationType {
    // 总是一直播放的动画
    ALWAYS("always");

    private String name;

    EnumAnimationType(String name) {
        this.name = name;
    }

    public static EnumAnimationType getTypeByName(String name) {
        for (EnumAnimationType type :
                EnumAnimationType.values()) {
            if (name.equals(type.getName())) {
                return type;
            }
        }
        return ALWAYS;
    }

    public String getName() {
        return name;
    }
}
