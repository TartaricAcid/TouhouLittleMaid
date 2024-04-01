package com.github.tartaricacid.touhoulittlemaid.block.properties;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum PicnicMatPart implements StringRepresentable {
    CENTER,
    SIDE;

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.US);
    }

    public boolean isCenter() {
        return this == CENTER;
    }
}
