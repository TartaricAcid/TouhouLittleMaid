package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
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
    private static final int REMOVE_FIRE_AGE = 5;
    private List<MonsterEntity> cacheFireImmuneMonster = Lists.newArrayList();

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
        if (tickCount > MAX_AGE) {
            this.remove();
            return;
        }
        if (tickCount == REMOVE_FIRE_AGE) {
            this.removeBlockFire();
            this.removeEntityFire();
        }
        this.damageFireImmuneMonster();
        if (level.isClientSide) {
            this.spawnCloudParticle();
        }
        this.playSound(SoundEvents.WOOL_PLACE, 2.0f - (1.8f / MAX_AGE) * tickCount, 0.1f);
    }

    private void damageFireImmuneMonster() {
        if (tickCount % 5 == 0 && this.cacheFireImmuneMonster != null && !this.cacheFireImmuneMonster.isEmpty()) {
            this.cacheFireImmuneMonster.forEach(monster -> {
                if (monster.isAlive()) {
                    monster.hurt(DamageSource.MAGIC, 2);
                }
            });
        }
    }

    private void spawnCloudParticle() {
        int spawnNumber = 4;
        for (int i = 0; i < spawnNumber; i++) {
            double offsetX = 2 * random.nextDouble() - 1;
            double offsetY = random.nextDouble() / 2;
            double offsetZ = 2 * random.nextDouble() - 1;
            level.addParticle(ParticleTypes.CLOUD, false,
                    this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ,
                    0, 0.1, 0);
        }
    }

    private void removeEntityFire() {
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2, 1, 2));
        this.cacheFireImmuneMonster = level.getEntitiesOfClass(MonsterEntity.class, this.getBoundingBox().inflate(2, 1, 2), Entity::fireImmune);
        for (LivingEntity entity : list) {
            entity.clearFire();
        }
    }

    private void removeBlockFire() {
        int hRange = 2;
        int vRange = 1;
        for (int x = -hRange; x <= hRange; x++) {
            for (int y = -vRange; y <= vRange; y++) {
                for (int z = -hRange; z <= hRange; z++) {
                    BlockPos pos = this.blockPosition().offset(x, y, z);
                    BlockState state = level.getBlockState(pos);
                    if (state.is(Blocks.FIRE)) {
                        level.removeBlock(pos, false);
                    }
                }
            }
        }
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
