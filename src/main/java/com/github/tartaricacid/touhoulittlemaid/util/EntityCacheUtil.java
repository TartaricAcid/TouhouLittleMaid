package com.github.tartaricacid.touhoulittlemaid.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.concurrent.TimeUnit;

@SideOnly(Side.CLIENT)
public final class EntityCacheUtil {
    /**
     * 实体缓存，在客户端会大量运用实体渲染，这个缓存可以减少重复创建实体带来的性能问题
     */
    public static final Cache<String, Entity> ENTITY_CACHE = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();

    public static void clearMaidDataResidue(EntityMaid maid, boolean clearEquipmentData) {
        maid.setShowSasimono(false);
        maid.hurtResistantTime = 0;
        maid.hurtTime = 0;
        maid.deathTime = 0;
        maid.setSitting(false);
        maid.setBackpackLevel(EntityMaid.EnumBackPackLevel.EMPTY);
        maid.setCustomNameTag("");
        if (clearEquipmentData) {
            for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
                maid.setItemStackToSlot(slot, ItemStack.EMPTY);
            }
        }
    }
}
