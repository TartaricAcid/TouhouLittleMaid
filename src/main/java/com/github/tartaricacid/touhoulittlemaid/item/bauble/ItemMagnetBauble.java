package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;

public class ItemMagnetBauble implements IMaidBauble {
    private static final int DELAY = 3 * 20;
    private static final int RANGE = 6;

    @Override
    public void onTick(EntityMaid maid, ItemStack baubleItem) {
        if (maid.tickCount % DELAY == 0) {
            handlePickup(maid);
        }
    }

    private void handlePickup(EntityMaid maid) {
        Level world = maid.level();
        if (maid.isPickup() && maid.isTame()) {
            List<Entity> entities = world.getEntities(maid, maid.getBoundingBox().inflate(RANGE), maid::canPickup);
            if (!entities.isEmpty() && maid.isAlive()) {
                for (Entity entityPickup : entities) {
                    // 如果是物品
                    if (entityPickup instanceof ItemEntity) {
                        maid.pickupItem((ItemEntity) entityPickup, false);
                    }
                    // 如果是经验
                    if (entityPickup instanceof ExperienceOrb) {
                        maid.pickupXPOrb((ExperienceOrb) entityPickup);
                    }
                    // 如果是 P 点
                    if (entityPickup instanceof EntityPowerPoint) {
                        maid.pickupPowerPoint((EntityPowerPoint) entityPickup);
                    }
                    // 如果是箭
                    if (entityPickup instanceof AbstractArrow) {
                        maid.pickupArrow((AbstractArrow) entityPickup, false);
                    }
                }
            }
        }
    }
}
