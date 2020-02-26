package com.github.tartaricacid.touhoulittlemaid.danmaku;

import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2019/11/22 20:53
 **/
public final class CustomSpellCardEntry {
    private String id;
    private String nameKey;
    private String descriptionKey;
    private String author;
    private String version;
    private Object script;
    private ResourceLocation icon;
    private ResourceLocation snapshoot;
    private int cooldown;

    public CustomSpellCardEntry(String id, String nameKey, String descriptionKey, String author, String version,
                                Object script, int cooldown, ResourceLocation icon, ResourceLocation snapshoot) {
        this.id = id;
        this.nameKey = nameKey;
        this.descriptionKey = descriptionKey;
        this.author = author;
        this.version = version;
        this.script = script;
        this.cooldown = cooldown;
        this.icon = icon;
        this.snapshoot = snapshoot;
    }

    public String getId() {
        return id;
    }

    public String getNameKey() {
        return nameKey;
    }

    public String getDescriptionKey() {
        return descriptionKey;
    }

    public String getAuthor() {
        return author;
    }

    public String getVersion() {
        return version;
    }

    public Object getScript() {
        return script;
    }

    public int getCooldown() {
        return cooldown;
    }

    @Nonnull
    public ResourceLocation getIcon() {
        return icon;
    }

    @Nonnull
    public ResourceLocation getSnapshoot() {
        return snapshoot;
    }
}
