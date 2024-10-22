package com.github.tartaricacid.touhoulittlemaid.entity.passive;

public enum SideTab {
    TASK_BOOK(0),
    GLOBAL_CONFIG(1);

    private final int index;

    SideTab(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
