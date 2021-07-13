package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.item.BackpackLevel;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;

import java.util.concurrent.TimeUnit;

public final class EntityCacheUtil {
    /**
     * 实体缓存，在客户端会大量运用实体渲染，这个缓存可以减少重复创建实体带来的性能问题
     */
    public static final Cache<EntityType<?>, Entity> ENTITY_CACHE = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();

    public static void clearMaidDataResidue(EntityMaid maid, boolean clearEquipmentData) {
        maid.hurtDuration = 0;
        maid.hurtTime = 0;
        maid.deathTime = 0;
        maid.setInSittingPose(false);
        maid.setBackpackLevel(BackpackLevel.EMPTY);
        maid.setCustomName(StringTextComponent.EMPTY);
        if (clearEquipmentData) {
            for (EquipmentSlotType slot : EquipmentSlotType.values()) {
                maid.setItemSlot(slot, ItemStack.EMPTY);
            }
        }
    }
}
