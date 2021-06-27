package com.github.tartaricacid.touhoulittlemaid.entity.passive;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.common.IForgeShearable;

import java.util.function.Predicate;

public final class EntityMaidPredicates {
    public static final Predicate<Entity> IS_PICKUP = entity -> (
            (entity instanceof ItemEntity && EntityMaid.canInsertItem(((ItemEntity) entity).getItem()))
                    || entity instanceof ExperienceOrbEntity
                    || entity instanceof EntityPowerPoint
                    || entity instanceof AbstractArrowEntity
    ) && !entity.isInWater();

    public static final Predicate<Entity> IS_MONSTER = entity -> entity instanceof MonsterEntity
            && entity.isAlive();

    public static final Predicate<Entity> CAN_SHEAR = entity -> entity instanceof IForgeShearable
            && ((IForgeShearable) entity).isShearable(new ItemStack(Items.SHEARS), entity.level, entity.blockPosition())
            && entity.isAlive();

    public static final Predicate<Entity> IS_COW = entity -> entity instanceof CowEntity
            && !((CowEntity) entity).isBaby()
            && entity.isAlive();
}
