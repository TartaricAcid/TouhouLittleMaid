package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerCapabilityProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SCollectItemPacket;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.NetworkHooks;

public class EntityPowerPoint extends Entity implements IEntityAdditionalSpawnData {
    public static final EntityType<EntityPowerPoint> TYPE = EntityType.Builder.<EntityPowerPoint>of(EntityPowerPoint::new, EntityClassification.MISC)
            .sized(0.5F, 0.5F).clientTrackingRange(6).updateInterval(20).build("power_point");
    private static final int MAX_AGE = 6000;
    public int tickCount;
    public int age;
    public int throwTime;
    public int value;
    private int health = 5;
    private PlayerEntity followingPlayer;
    private int followingTime;

    public EntityPowerPoint(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
    }

    public EntityPowerPoint(World worldIn, double x, double y, double z, int powerValue) {
        this(TYPE, worldIn);
        this.setPos(x, y, z);
        this.yRot = (float) (this.random.nextDouble() * 360.0);
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

    @Override
    protected boolean isMovementNoisy() {
        return false;
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
            this.remove();
        }
    }

    private void groundMovement() {
        double slipperiness = 0.98;
        if (this.onGround) {
            BlockPos pos = new BlockPos(this.getX(), this.getY() - 1.0, this.getZ());
            slipperiness = this.level.getBlockState(pos).getSlipperiness(this.level, pos, this) * 0.98;
        }
        this.setDeltaMovement(this.getDeltaMovement().multiply(slipperiness, 0.98, slipperiness));
        if (this.onGround) {
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
            Vector3d relativeVector = new Vector3d(this.followingPlayer.getX() - this.getX(),
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
            Vector3d movement = this.getDeltaMovement();
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
                this.remove();
            }
        }
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        compound.putShort("Health", (short) this.health);
        compound.putShort("Age", (short) this.age);
        compound.putShort("Value", (short) this.value);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        this.health = compound.getShort("Health");
        this.age = compound.getShort("Age");
        this.value = compound.getShort("Value");
    }

    @Override
    public void playerTouch(PlayerEntity player) {
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
                this.remove();
            });
        }
    }

    public void take(Entity player, int quantity) {
        if (this.isAlive() && !this.level.isClientSide) {
            ((ServerWorld) this.level).getChunkSource().broadcast(this, new SCollectItemPacket(this.getId(), player.getId(), quantity));
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
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void writeSpawnData(PacketBuffer buffer) {
        buffer.writeInt(value);
    }

    @Override
    public void readSpawnData(PacketBuffer additionalData) {
        this.value = additionalData.readInt();
    }
}
