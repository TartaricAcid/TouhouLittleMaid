package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.BeaconAbsorbMessage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundTakeItemEntityPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;

import java.util.Random;

public class EntityPowerPoint extends Entity implements IEntityAdditionalSpawnData {
    public static final EntityType<EntityPowerPoint> TYPE = EntityType.Builder.<EntityPowerPoint>of(EntityPowerPoint::new, MobCategory.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(6).updateInterval(20).build("power_point");
    private static final int MAX_AGE = 6000;
    public int tickCount;
    public int age;
    public int throwTime;
    public int value;
    private int health = 5;
    private Player followingPlayer;
    private int followingTime;

    public EntityPowerPoint(EntityType<?> entityTypeIn, Level worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntityPowerPoint(Level worldIn, double x, double y, double z, int powerValue) {
        this(TYPE, worldIn);
        this.setPos(x, y, z);
        this.setYRot((float) (this.random.nextDouble() * 360.0));
        this.setDeltaMovement((this.random.nextDouble() * 0.2 - 0.1) * 2.0,
                this.random.nextDouble() * 0.2 * 2.0,
                (this.random.nextDouble() * 0.2 - 0.1) * 2.0);
        this.value = powerValue;
    }

    public static int getPowerValue(int powerValue) {
        if (powerValue >= 485) {
            return 485;
        } else if (powerValue >= 385) {
            return 385;
        } else if (powerValue >= 285) {
            return 285;
        } else if (powerValue >= 185) {
            return 185;
        } else if (powerValue >= 89) {
            return 89;
        } else if (powerValue >= 36) {
            return 34;
        } else if (powerValue >= 17) {
            return 13;
        } else if (powerValue >= 7) {
            return 7;
        } else if (powerValue >= 5) {
            return 5;
        } else {
            return powerValue >= 3 ? 3 : 1;
        }
    }

    /**
     * P 点可以向经验转换，转换比率为 4P = 1 XP
     */
    public static int transPowerValueToXpValue(int powerValue) {
        return powerValue / 4;
    }

    public static void spawnExplosionParticle(Level world, float x, float y, float z, RandomSource rand) {
        if (!world.isClientSide) {
            return;
        }
        for (int i = 0; i < 5; ++i) {
            float mx = (rand.nextFloat() - 0.5F) * 0.02F;
            float my = (rand.nextFloat() - 0.5F) * 0.02F;
            float mz = (rand.nextFloat() - 0.5F) * 0.02F;
            world.addParticle(ParticleTypes.CLOUD,
                    x + rand.nextFloat() - 0.5F,
                    y + rand.nextFloat() - 0.5F,
                    z + rand.nextFloat() - 0.5F,
                    mx, my, mz);
        }
    }

    public void spawnExplosionParticle() {
        float x = (float) position().x;
        float y = (float) position().y + 0.125F;
        float z = (float) position().z;
        if (level.isClientSide) {
            spawnExplosionParticle(level, x, y, z, random);
        } else {
            NetworkHandler.sendToNearby(level, blockPosition(), new BeaconAbsorbMessage(x, y, z));
        }
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    public void tick() {
        super.tick();

        if (this.throwTime > 0) {
            --this.throwTime;
        }

        this.xo = this.getX();
        this.yo = this.getY();
        this.zo = this.getZ();
        this.fluidMovement();
        if (!this.level.noCollision(this.getBoundingBox())) {
            this.moveTowardsClosestSpace(this.getX(), (this.getBoundingBox().minY + this.getBoundingBox().maxY) / 2.0, this.getZ());
        }
        this.followingMovement();
        this.move(MoverType.SELF, this.getDeltaMovement());
        this.groundMovement();

        ++this.tickCount;
        ++this.age;
        if (this.age >= MAX_AGE) {
            this.discard();
        }
    }

    private void groundMovement() {
        double slipperiness = 0.98;
        if (this.onGround()) {
            BlockPos pos = new BlockPos((int) this.getX(), (int) (this.getY() - 1.0), (int) this.getZ());
            slipperiness = this.level.getBlockState(pos).getFriction(this.level, pos, this) * 0.98;
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(slipperiness, 0.98, slipperiness));
        if (this.onGround()) {
            this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, -0.9, 1.0));
        }
    }

    private void followingMovement() {
        double distance = 8.0;
        if (this.followingTime < getRandomCheckTime()) {
            if (this.followingPlayer == null || this.followingPlayer.distanceTo(this) > distance) {
                this.followingPlayer = this.level.getNearestPlayer(this, distance);
            }
            this.followingTime = this.tickCount;
        }

        if (this.followingPlayer != null && this.followingPlayer.isSpectator()) {
            this.followingPlayer = null;
        }

        if (this.followingPlayer != null) {
            Vec3 relativeVector = new Vec3(this.followingPlayer.getX() - this.getX(),
                    this.followingPlayer.getY() + this.followingPlayer.getEyeHeight() / 2.0 - this.getY(),
                    this.followingPlayer.getZ() - this.getZ());
            double length = relativeVector.length();
            if (length < distance) {
                double factor = 1.0 - length / 8.0;
                this.setDeltaMovement(this.getDeltaMovement().add(relativeVector.normalize().scale(factor * factor * 0.1D)));
            }
        }
    }

    private int getRandomCheckTime() {
        return this.tickCount - 20 + this.getId() % 100;
    }

    private void fluidMovement() {
        if (this.isEyeInFluid(FluidTags.WATER)) {
            Vec3 movement = this.getDeltaMovement();
            this.setDeltaMovement(movement.x * 0.99, Math.min(movement.y + 0.0005, 0.06), movement.z * 0.99);
        } else if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.03, 0.0));
        }

        if (this.level.getFluidState(this.blockPosition()).is(FluidTags.LAVA)) {
            this.setDeltaMovement((this.random.nextDouble() - this.random.nextDouble()) * 0.2,
                    0.2, (this.random.nextDouble() - this.random.nextDouble()) * 0.2);
            this.playSound(SoundEvents.GENERIC_BURN, 0.4F, 2.0F + this.random.nextFloat() * 0.4F);
        }
    }

    @Override
    protected void doWaterSplashEffect() {
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.level.isClientSide || !this.isAlive()) {
            return false;
        }
        if (!this.isInvulnerableTo(source)) {
            this.markHurt();
            this.health = (int) ((float) this.health - amount);
            if (this.health <= 0) {
                this.discard();
            }
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        compound.putShort("Health", (short) this.health);
        compound.putShort("Age", (short) this.age);
        compound.putShort("Value", (short) this.value);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        this.health = compound.getShort("Health");
        this.age = compound.getShort("Age");
        this.value = compound.getShort("Value");
    }

    @Override
    public void playerTouch(Player player) {
        if (this.level.isClientSide) {
            return;
        }

        if (this.throwTime == 0 && player.takeXpDelay == 0) {
            LazyOptional<PowerCapability> powerCap = player.getCapability(PowerCapabilityProvider.POWER_CAP, null);
            powerCap.ifPresent((power) -> {
                player.takeXpDelay = 2;
                this.take(player, 1);
                if (this.value > 0) {
                    if (power.get() + value / 100.0f > PowerCapability.MAX_POWER) {
                        power.add(PowerCapability.MAX_POWER - power.get());
                        int residualValue = value - (int) (PowerCapability.MAX_POWER * 100) + (int) (power.get() * 100);
                        // 和原版设计不同，该数值过大，故缩小一些
                        player.giveExperiencePoints(transPowerValueToXpValue(residualValue));
                    } else {
                        power.add(value / 100.0f);
                    }
                }
                this.discard();
            });
        }
    }

    public void take(Entity player, int quantity) {
        if (this.isAlive() && !this.level.isClientSide) {
            ((ServerLevel) this.level).getChunkSource().broadcast(this, new ClientboundTakeItemEntityPacket(this.getId(), player.getId(), quantity));
        }
    }

    public int getValue() {
        return this.value;
    }

    @OnlyIn(Dist.CLIENT)
    public int getIcon() {
        if (this.value >= 485) {
            return 10;
        } else if (this.value >= 385) {
            return 9;
        } else if (this.value >= 285) {
            return 8;
        } else if (this.value >= 185) {
            return 7;
        } else if (this.value >= 89) {
            return 6;
        } else if (this.value >= 36) {
            return 5;
        } else if (this.value >= 17) {
            return 4;
        } else if (this.value >= 7) {
            return 3;
        } else if (this.value >= 5) {
            return 2;
        } else {
            return this.value >= 3 ? 1 : 0;
        }
    }

    @Override
    public boolean isAttackable() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(value);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.value = additionalData.readInt();
    }
}
