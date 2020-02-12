package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

import javax.annotation.Nonnull;

/**
 * @author TartaricAcid
 * @date 2020/2/2 20:13
 **/
public final class EntityPlaceAndHit {
    private static final String GAME_RULES_DO_ENTITY_DROPS = "doEntityDrops";

    private EntityPlaceAndHit() {
    }

    public static boolean onAttackEntity(EntityLivingBase beHitEntity, @Nonnull DamageSource source, ItemStack dropStack) {
        if (!beHitEntity.world.isRemote && !beHitEntity.isDead) {
            // 如果实体是无敌的
            if (beHitEntity.isEntityInvulnerable(source)) {
                return false;
            }
            // 应用打掉的逻辑
            if (source.getTrueSource() instanceof EntityPlayer) {
                return applyHitEntityLogic(beHitEntity, (EntityPlayer) source.getTrueSource(), dropStack);
            }
        }
        return false;
    }

    private static boolean applyHitEntityLogic(EntityLivingBase beHitEntity, EntityPlayer player, ItemStack dropStack) {
        boolean isPlayerCreativeMode = player.capabilities.isCreativeMode;
        // 潜行状态才会运用击打逻辑
        if (player.isSneaking()) {
            beHitEntity.removePassengers();
            if (isPlayerCreativeMode && !beHitEntity.hasCustomName()) {
                // 如果是创造模式，而且该实体没有命名，直接消失
                beHitEntity.setDead();
            } else {
                // 否则应用实体转物品逻辑
                killEntity(beHitEntity, dropStack);
            }
        }
        return true;
    }

    /**
     * 将该实体从实体状态转变为物品状态
     */
    private static void killEntity(EntityLivingBase beHitEntity, ItemStack dropStack) {
        beHitEntity.setDead();
        if (beHitEntity.world.getGameRules().getBoolean(GAME_RULES_DO_ENTITY_DROPS)) {
            if (beHitEntity.hasCustomName()) {
                dropStack.setStackDisplayName(beHitEntity.getCustomNameTag());
            }
            beHitEntity.entityDropItem(dropStack, 0.0F);
        }
    }
}
