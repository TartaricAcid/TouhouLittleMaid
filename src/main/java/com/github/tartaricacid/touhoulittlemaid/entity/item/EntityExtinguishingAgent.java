package com.github.tartaricacid.touhoulittlemaid.entity.item;


import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * @author TartaricAcid
 */
public class EntityExtinguishingAgent extends Entity {
    private static final int MAX_AGE = 3 * 20;
    private boolean isCheck = false;

    public EntityExtinguishingAgent(World worldIn) {
        super(worldIn);
        setSize(0.2f, 0.2f);
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted > MAX_AGE) {
            this.setDead();
            return;
        }
        if (!isCheck && ticksExisted == 5) {
            List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().grow(2, 1, 2));
            for (EntityLivingBase entity : list) {
                entity.extinguish();
            }
            for (int i = -2; i <= 2; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -2; k <= 2; k++) {
                        BlockPos pos = this.getPosition().add(i, j, k);
                        IBlockState state = world.getBlockState(pos);
                        if (state.getBlock() == Blocks.FIRE) {
                            world.setBlockToAir(pos);
                        } else if (state.getBlock() == Blocks.LAVA) {
                            world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
                            // TODO: 2020/6/22 熔岩转换待优化
                        }
                    }
                }
            }
            isCheck = true;
        }
        if (world.isRemote) {
            for (int i = 0; i < 9; i++) {
                double offsetX = 2 * rand.nextDouble() - 1;
                double offsetY = rand.nextDouble() / 2;
                double offsetZ = 2 * rand.nextDouble() - 1;
                world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, false,
                        this.posX + offsetX, this.posY + offsetY, this.posZ + offsetZ,
                        0, 0.1, 0);
            }
        }
    }

    @Override
    protected void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
    }

    @Override
    protected void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
    }
}
