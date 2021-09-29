package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidTickEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaidPredicates;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;

public class ItemMagnetBauble implements IMaidBauble {
    private static final int DELAY = 3 * 20;
    private static final int RANGE = 6;

    public ItemMagnetBauble() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onLivingDamage(MaidTickEvent event) {
        EntityMaid maid = event.getMaid();
        if (maid.tickCount % DELAY == 0) {
            int slot = ItemsUtil.getBaubleSlotInMaid(maid, this);
            if (slot >= 0) {
                handlePickup(maid);
            }
        }
    }

    private void handlePickup(EntityMaid maid) {
        World world = maid.level;
        if (maid.isPickup() && maid.isTame()) {
            List<Entity> entities = world.getEntities(maid, maid.getBoundingBox().inflate(RANGE), EntityMaidPredicates.IS_PICKUP);
            if (!entities.isEmpty() && maid.isAlive()) {
                for (Entity entityPickup : entities) {
                    // 如果是物品
                    if (entityPickup instanceof ItemEntity) {
                        maid.pickupItem((ItemEntity) entityPickup, false);
                    }
                    // 如果是经验
                    if (entityPickup instanceof ExperienceOrbEntity) {
                        maid.pickupXPOrb((ExperienceOrbEntity) entityPickup);
                    }
                    // 如果是 P 点
                    if (entityPickup instanceof EntityPowerPoint) {
                        maid.pickupPowerPoint((EntityPowerPoint) entityPickup);
                    }
                    // 如果是箭
                    if (entityPickup instanceof AbstractArrowEntity) {
                        maid.pickupArrow((AbstractArrowEntity) entityPickup, false);
                    }
                }
            }
        }
    }
}
