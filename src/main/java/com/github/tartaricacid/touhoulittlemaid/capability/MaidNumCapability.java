package com.github.tartaricacid.touhoulittlemaid.capability;

import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import net.minecraft.nbt.IntTag;
import net.minecraft.util.Mth;

public class MaidNumCapability {
    private int num = 0;
    private boolean dirty;

    public boolean canAdd() {
        return this.num + 1 <= getMaxNum();
    }

    public void add() {
        this.add(1);
    }

    public void add(int num) {
        if (num + this.num <= getMaxNum()) {
            this.num += num;
        } else {
            this.num = getMaxNum();
        }
        markDirty();
    }

    public void min(int num) {
        if (num <= this.num) {
            this.num -= num;
        } else {
            this.num = 0;
        }
        markDirty();
    }

    public void set(int num) {
        this.num = Mth.clamp(num, 0, getMaxNum());
        markDirty();
    }

    public int getMaxNum() {
        return MaidConfig.OWNER_MAX_MAID_NUM.get();
    }

    public int get() {
        return this.num;
    }

    public void markDirty() {
        dirty = true;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public IntTag serializeNBT() {
        return IntTag.valueOf(this.num);
    }

    public void deserializeNBT(IntTag nbt) {
        this.num = nbt.getAsInt();
    }
}
