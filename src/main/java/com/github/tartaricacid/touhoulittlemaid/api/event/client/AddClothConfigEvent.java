package com.github.tartaricacid.touhoulittlemaid.api.event.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraftforge.eventbus.api.Event;

public class AddClothConfigEvent extends Event {
    private final ConfigBuilder root;
    private final ConfigEntryBuilder entryBuilder;

    public AddClothConfigEvent(ConfigBuilder root, ConfigEntryBuilder entryBuilder) {
        this.root = root;
        this.entryBuilder = entryBuilder;
    }

    public ConfigBuilder getRoot() {
        return root;
    }

    public ConfigEntryBuilder getEntryBuilder() {
        return entryBuilder;
    }
}
