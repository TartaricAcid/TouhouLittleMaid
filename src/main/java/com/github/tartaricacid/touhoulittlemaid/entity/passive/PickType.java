package com.github.tartaricacid.touhoulittlemaid.entity.passive;

public enum PickType {
    ONLY_ITEM(true, false),
    ONLY_XP(false, true),
    ALL(true, true);

    private final boolean pickItem;
    private final boolean pickXp;

    PickType(boolean pickItem, boolean pickXp) {
        this.pickItem = pickItem;
        this.pickXp = pickXp;
    }

    public boolean canPickItem() {
        return pickItem;
    }

    public boolean canPickXp() {
        return pickXp;
    }

    public static String getTransKey(final PickType pickType) {
        return switch (pickType) {
            case ONLY_ITEM -> "gui.touhou_little_maid.maid_config.value.item";
            case ONLY_XP -> "gui.touhou_little_maid.maid_config.value.xp";
            default -> "gui.touhou_little_maid.maid_config.value.all";
        };
    }

    public static PickType getNextPickType(final PickType pickType) {
        return values()[(pickType.ordinal() + 1) % values().length];
    }

    public static PickType getPreviousPickType(final PickType pickType) {
        int index = pickType.ordinal() - 1;
        if (index < 0) {
            index = values().length - 1;
        }
        return values()[index % values().length];
    }
}