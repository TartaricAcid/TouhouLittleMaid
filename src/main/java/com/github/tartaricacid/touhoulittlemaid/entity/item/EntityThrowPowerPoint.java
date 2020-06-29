package com.github.tartaricacid.touhoulittlemaid.entity.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.PotionTypes;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class EntityThrowPowerPoint extends EntityThrowable {
    public EntityThrowPowerPoint(World worldIn) {
        super(worldIn);
    }

    public EntityThrowPowerPoint(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    public EntityThrowPowerPoint(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
    }

    @Override
    protected float getGravityVelocity() {
        return 0.07F;
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        if (!this.world.isRemote) {
            this.world.playEvent(2002, new BlockPos(this), PotionUtils.getPotionColor(PotionTypes.HEALING));
            int i = 30 + this.world.rand.nextInt(30) + this.world.rand.nextInt(30);

            while (i > 0) {
                int j = EntityPowerPoint.getPowerSplit(i);
                i -= j;
                this.world.spawnEntity(new EntityPowerPoint(this.world, this.posX, this.posY, this.posZ, j));
            }

            this.setDead();
        }
    }
}
