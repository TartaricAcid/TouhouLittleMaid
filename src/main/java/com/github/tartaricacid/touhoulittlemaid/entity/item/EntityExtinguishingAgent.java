package com.github.tartaricacid.touhoulittlemaid.entity.item;


import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class EntityExtinguishingAgent extends Entity {
    public static final EntityType<EntityExtinguishingAgent> TYPE = EntityType.Builder.<EntityExtinguishingAgent>of(EntityExtinguishingAgent::new, MobCategory.MISC)
            .sized(0.2f, 0.2f).clientTrackingRange(10).build("extinguishing_agent");
    private static final int MAX_AGE = 3 * 20;
    private static final int REMOVE_FIRE_AGE = 5;
    private List<Monster> cacheFireImmuneMonster = Lists.newArrayList();

    public EntityExtinguishingAgent(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntityExtinguishingAgent(Level worldIn, Vec3 position) {
        this(TYPE, worldIn);
        this.setPos(position.x, position.y, position.z);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if (tickCount > MAX_AGE) {
            this.discard();
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

    private void damageFireImmuneMonster() {
        if (tickCount % 5 == 0 && this.cacheFireImmuneMonster != null && !this.cacheFireImmuneMonster.isEmpty()) {
            this.cacheFireImmuneMonster.forEach(monster -> {
                if (monster.isAlive()) {
                    monster.hurt(level.damageSources().magic(), 2);
                }
            });
        }
    }

    private void removeEntityFire() {
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2, 1, 2));
        this.cacheFireImmuneMonster = level.getEntitiesOfClass(Monster.class, this.getBoundingBox().inflate(2, 1, 2), Entity::fireImmune);
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
    protected void defineSynchedData(SynchedEntityData.Builder pBuilder) {
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity pEntity) {
        return new ClientboundAddEntityPacket(this, pEntity);
    }
}
