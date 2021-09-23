package com.github.tartaricacid.touhoulittlemaid.entity.item;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.List;

public class EntityExtinguishingAgent extends Entity {
    public static final EntityType<EntityExtinguishingAgent> TYPE = EntityType.Builder.<EntityExtinguishingAgent>of(EntityExtinguishingAgent::new, EntityClassification.MISC)
            .sized(0.2f, 0.2f).clientTrackingRange(10).build("extinguishing_agent");
    private static final int MAX_AGE = 3 * 20;
    private boolean isCheck = false;

    public EntityExtinguishingAgent(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntityExtinguishingAgent(World worldIn, Vector3d position) {
        this(TYPE, worldIn);
        this.setPos(position.x, position.y, position.z);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (this.tickCount > MAX_AGE) {
            this.remove();
            return;
        }
        if (!isCheck && tickCount == 5) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -2; k <= 2; k++) {
                        BlockPos pos = this.blockPosition().offset(i, j, k);
                        BlockState state = level.getBlockState(pos);
                        if (state.is(Blocks.FIRE)) {
                            level.removeBlock(pos, false);
                        }
                    }
                }
            }

            List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2, 1, 2));
            for (LivingEntity entity : list) {
                entity.clearFire();
            }

            isCheck = true;
        }
        if (level.isClientSide) {
            for (int i = 0; i < 4; i++) {
                double offsetX = 2 * random.nextDouble() - 1;
                double offsetY = random.nextDouble() / 2;
                double offsetZ = 2 * random.nextDouble() - 1;
                level.addParticle(ParticleTypes.CLOUD, false,
                        this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                        0, 0.1, 0);
            }
        }
        this.playSound(SoundEvents.WOOL_PLACE, 2.0f - (1.8f / MAX_AGE) * tickCount, 0.1f);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected void readAdditionalSaveData(CompoundNBT compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundNBT compound) {
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
