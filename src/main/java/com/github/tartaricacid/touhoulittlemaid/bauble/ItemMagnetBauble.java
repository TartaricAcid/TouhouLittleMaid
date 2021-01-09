package com.github.tartaricacid.touhoulittlemaid.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaidPredicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

public class ItemMagnetBauble implements IMaidBauble {
    private static final int DELAY = 3 * 20;
    private static final int RANGE = 6;

    @Override
    public void onTick(AbstractEntityMaid entityMaid, ItemStack baubleItem) {
        if (entityMaid.ticksExisted % DELAY == 0) {
            World world = entityMaid.getEntityWorld();
            EntityMaid maid = (EntityMaid) entityMaid;
            if (entityMaid.isPickup() && entityMaid.isTamed()) {
                List<Entity> entityList = world.getEntitiesInAABBexcluding(entityMaid,
                        entityMaid.getEntityBoundingBox().grow(RANGE), EntityMaidPredicate.IS_PICKUP);
                if (!entityList.isEmpty() && entityMaid.isEntityAlive()) {
                    for (Entity entityPickup : entityList) {
                        // 如果是物品
                        if (entityPickup instanceof EntityItem) {
                            maid.pickupItem((EntityItem) entityPickup, false);
                        }
                        // 如果是经验
                        if (entityPickup instanceof EntityXPOrb) {
                            maid.pickupXPOrb((EntityXPOrb) entityPickup);
                        }
                        // 如果是 P 点
                        if (entityPickup instanceof EntityPowerPoint) {
                            maid.pickupPowerPoint((EntityPowerPoint) entityPickup);
                        }
                        // 如果是箭
                        if (entityPickup instanceof EntityArrow) {
                            maid.pickupArrow((EntityArrow) entityPickup, false);
                        }
                    }
                }
            }
        }
    }
}
