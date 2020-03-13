package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.google.common.base.Predicate;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IShearable;

import static com.github.tartaricacid.touhoulittlemaid.inventory.MaidInventoryItemHandler.isIllegalItem;

public final class EntityMaidPredicate {
    public static final Predicate<Entity> IS_PICKUP = entity -> (
            (entity instanceof EntityItem && !isIllegalItem(((EntityItem) entity).getItem()))
                    || entity instanceof EntityXPOrb
                    || entity instanceof EntityPowerPoint
                    || entity instanceof EntityArrow
    ) && !entity.isInWater();

    public static final Predicate<Entity> IS_MOB = entity -> entity instanceof EntityMob
            && entity.isEntityAlive();

    public static final Predicate<Entity> CAN_SHEAR = entity -> entity instanceof IShearable
            && ((IShearable) entity).isShearable(new ItemStack(Items.SHEARS), entity.world, entity.getPosition())
            && entity.isEntityAlive();

    public static final Predicate<Entity> IS_COW = entity -> entity instanceof EntityCow
            && !((EntityCow) entity).isChild()
            && entity.isEntityAlive();
}
