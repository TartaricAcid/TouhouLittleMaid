package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityThrowPowerPoint extends ProjectileItemEntity {
    public static final EntityType<EntityThrowPowerPoint> TYPE = EntityType.Builder.<EntityThrowPowerPoint>of(EntityThrowPowerPoint::new, EntityClassification.MISC)
            .sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("throw_power_point");

    public EntityThrowPowerPoint(EntityType<EntityThrowPowerPoint> type, World worldIn) {
        super(type, worldIn);
    }

    public EntityThrowPowerPoint(World world, LivingEntity thrower) {
        super(TYPE, thrower, world);
    }

    public EntityThrowPowerPoint(World world, double x, double y, double z) {
        super(TYPE, x, y, z, world);
    }

    @Override
    protected Item getDefaultItem() {
        return InitItems.POWER_POINT.get();
    }

    @Override
    protected float getGravity() {
        return 0.07F;
    }

    @Override
    protected void onHit(RayTraceResult result) {
        super.onHit(result);
        if (!this.level.isClientSide) {
            this.level.levelEvent(Constants.WorldEvents.POTION_IMPACT_INSTANT, this.blockPosition(), PotionUtils.getColor(Potions.HEALING));
            int count = 30 + this.level.random.nextInt(30) + this.level.random.nextInt(30);
            while (count > 0) {
                int value = EntityPowerPoint.getPowerValue(count);
                count -= value;
                this.level.addFreshEntity(new EntityPowerPoint(this.level, this.getX(), this.getY(), this.getZ(), value));
            }
            this.remove();
        }
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
