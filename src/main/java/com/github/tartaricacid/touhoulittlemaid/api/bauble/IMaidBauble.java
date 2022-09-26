package com.github.tartaricacid.touhoulittlemaid.api.bauble;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

public interface IMaidBauble {
    Random RANDOM = new Random();

    /**
     * Maid tick the bauble
     *
     * @param maid       EntityMaid
     * @param baubleItem ItemStack
     */
    default void onTick(EntityMaid maid, ItemStack baubleItem) {
    }

    /**
     * Get a chat bubble when maid has the bauble
     *
     * @return chat bubble id
     */
    default String getChatBubbleId() {
        return StringUtils.EMPTY;
    }
}
