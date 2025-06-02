package com.github.tartaricacid.touhoulittlemaid.ai.service.llm;

import com.mojang.serialization.Codec;

public enum Role {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
    TOOL("tool");

    public static final Codec<Role> CODEC = Codec.STRING.xmap(Role::byId, Role::getId);

    private final String id;

    Role(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static Role byId(String id) {
        for (Role role : values()) {
            if (role.id.equals(id)) {
                return role;
            }
        }
        return Role.ASSISTANT;
    }
}
