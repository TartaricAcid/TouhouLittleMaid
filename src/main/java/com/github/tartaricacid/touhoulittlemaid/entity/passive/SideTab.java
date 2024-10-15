package com.github.tartaricacid.touhoulittlemaid.entity.passive;

public enum SideTab {
    TASK_CONFIG(0),
    TASK_BOOK(1),
    TASK_INFO(2),
    GLOBAL_CONFIG(3);

    private final int index;

    SideTab(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
