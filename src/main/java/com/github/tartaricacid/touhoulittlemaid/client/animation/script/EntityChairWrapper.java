package com.github.tartaricacid.touhoulittlemaid.client.animation.script;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import net.minecraft.entity.player.EntityPlayer;

public class EntityChairWrapper {
    private EntityChair chair;

    public void setChair(EntityChair chair) {
        this.chair = chair;
    }

    public boolean isRidingPlayer() {
        return chair.getRidingEntity() instanceof EntityPlayer;
    }
}
