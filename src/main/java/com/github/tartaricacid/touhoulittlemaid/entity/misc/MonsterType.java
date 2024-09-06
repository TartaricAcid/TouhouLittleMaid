package com.github.tartaricacid.touhoulittlemaid.entity.misc;

public enum MonsterType {
    FRIENDLY("MonsterFriendly"),
    NEUTRAL("MonsterNeutral"),
    HOSTILE("MonsterHostile");

    private final String tagName;

    MonsterType(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }
}