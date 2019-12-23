package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.capability.CapabilityPowerHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.BeaconAbsorbMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * 因为 P 点的逻辑和原版经验几乎一致，所以直接借用原版经验算法，省时省力
 *
 * @author TartaricAcid
 * @date 2019/8/29 16:24
 **/
public class EntityPowerPoint extends Entity {
    public int delayBeforeCanPickup;
    public int powerValue;
    private int powerColor;
    private int powerAge;
    private int powerHealthy = 5;
    private EntityPlayer closestPlayer;
    private int powerTargetColor;

    /**
     * 生成 P 点
     *
     * @param powerValue P 点值，传入数据将会 / 100.0f 转换为玩家的 Power 数
     */
    public EntityPowerPoint(World worldIn, double x, double y, double z, int powerValue) {
        super(worldIn);
        this.setSize(0.5F, 0.5F);
        this.setPosition(x, y, z);
        this.rotationYaw = (float) (Math.random() * 360.0);
        this.motionX = (Math.random() * 0.2 - 0.1) * 2.0;
        this.motionY = (Math.random() * 0.2) * 2.0;
        this.motionZ = (Math.random() * 0.2 - 0.1) * 2.0;
        this.powerValue = powerValue;
    }

    public EntityPowerPoint(World worldIn) {
        super(worldIn);
        this.setSize(0.25F, 0.25F);
    }

    /**
     * 划分 P 点数值
     */
    public static int getPowerSplit(int expValue) {
        if (expValue >= 485) {
            return 485;
        } else if (expValue >= 385) {
            return 385;
        } else if (expValue >= 285) {
            return 285;
        } else if (expValue >= 185) {
            return 185;
        } else if (expValue >= 89) {
            return 89;
        } else if (expValue >= 36) {
            return 34;
        } else if (expValue >= 17) {
            return 13;
        } else if (expValue >= 7) {
            return 7;
        } else if (expValue >= 5) {
            return 5;
        } else {
            return expValue >= 3 ? 3 : 1;
        }
    }

    /**
     * P 点可以向经验转换，转换比率为 4P = 1 XP
     */
    public static int transPowerValueToXpValue(int powerValue) {
        return powerValue / 4;
    }

    public static void spawnExplosionParticle(World world, float x, float y, float z, Random rand) {
        if (!world.isRemote) {
            return;
        }
        for (int i = 0; i < 20; ++i) {
            float mx = (rand.nextFloat() - 0.5F) * 0.02F;
            float my = (rand.nextFloat() - 0.5F) * 0.02F;
            float mz = (rand.nextFloat() - 0.5F) * 0.02F;
            world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL,
                    x + rand.nextFloat() - 0.5F,
                    y + rand.nextFloat() - 0.5F,
                    z + rand.nextFloat() - 0.5F,
                    mx, my, mz);
        }
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    protected void entityInit() {
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getBrightnessForRender() {
        float f = 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        int i = super.getBrightnessForRender();
        int j = i & 255;
        int k = i >> 16 & 255;
        j = j + (int) (f * 15.0F * 16.0F);
        if (j > 240) {
            j = 240;
        }
        return j | k << 16;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (this.delayBeforeCanPickup > 0) {
            --this.delayBeforeCanPickup;
        }

        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (!this.hasNoGravity()) {
            this.motionY -= 0.03;
        }

        if (this.world.getBlockState(new BlockPos(this)).getMaterial() == Material.LAVA) {
            this.motionY = 0.2;
            this.motionX = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.motionZ = (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
            this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
        }

        this.pushOutOfBlocks(this.posX, (this.getEntityBoundingBox().minY + this.getEntityBoundingBox().maxY) / 2.0, this.posZ);
        double distance = 8.0;

        if (this.powerTargetColor < this.powerColor - 20 + this.getEntityId() % 100) {
            if (this.closestPlayer == null || this.closestPlayer.getDistanceSq(this) > Math.sqrt(distance)) {
                this.closestPlayer = this.world.getClosestPlayerToEntity(this, distance);
            }
            this.powerTargetColor = this.powerColor;
        }

        if (this.closestPlayer != null && this.closestPlayer.isSpectator()) {
            this.closestPlayer = null;
        }

        if (this.closestPlayer != null) {
            double distanceX = (this.closestPlayer.posX - this.posX) / 8.0;
            double distanceY = (this.closestPlayer.posY + (double) this.closestPlayer.getEyeHeight() / 2.0 - this.posY) / 8.0;
            double distanceZ = (this.closestPlayer.posZ - this.posZ) / 8.0;
            double distanceSqrt = Math.sqrt(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
            double negativeDistanceSqrt = 1.0 - distanceSqrt;
            if (negativeDistanceSqrt > 0) {
                negativeDistanceSqrt = negativeDistanceSqrt * negativeDistanceSqrt;
                double coefficient = closestPlayer.isSneaking() ? 3.0 : 0.1;
                this.motionX += distanceX / distanceSqrt * negativeDistanceSqrt * coefficient;
                this.motionY += distanceY / distanceSqrt * negativeDistanceSqrt * coefficient;
                this.motionZ += distanceZ / distanceSqrt * negativeDistanceSqrt * coefficient;
            }
        }

        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        float slipperiness = 0.98F;

        if (this.onGround) {
            BlockPos underPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
            IBlockState underState = this.world.getBlockState(underPos);
            slipperiness = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.98F;
        }

        this.motionX *= (double) slipperiness;
        this.motionY *= 0.98;
        this.motionZ *= (double) slipperiness;

        if (this.onGround) {
            this.motionY *= -0.9;
        }

        ++this.powerColor;
        ++this.powerAge;

        int maxAge = 6000;
        if (this.powerAge >= maxAge) {
            this.setDead();
        }
    }

    @Override
    public boolean handleWaterMovement() {
        return this.world.handleMaterialAcceleration(this.getEntityBoundingBox(), Material.WATER, this);
    }

    @Override
    protected void dealFireDamage(int amount) {
        this.attackEntityFrom(DamageSource.IN_FIRE, (float) amount);
    }

    @Override
    public boolean attackEntityFrom(@Nonnull DamageSource source, float amount) {
        if (this.world.isRemote || this.isDead) {
            return false;
        }
        if (this.isEntityInvulnerable(source)) {
            return false;
        } else {
            this.markVelocityChanged();
            this.powerHealthy = (int) ((float) this.powerHealthy - amount);
            if (this.powerHealthy <= 0) {
                this.setDead();
            }
            return false;
        }
    }

    @Override
    public void writeEntityToNBT(@Nonnull NBTTagCompound compound) {
        compound.setShort("Health", (short) this.powerHealthy);
        compound.setShort("Age", (short) this.powerAge);
        compound.setShort("Value", (short) this.powerValue);
    }

    @Override
    public void readEntityFromNBT(@Nonnull NBTTagCompound compound) {
        this.powerHealthy = compound.getShort("Health");
        this.powerAge = compound.getShort("Age");
        this.powerValue = compound.getShort("Value");
    }

    @Override
    public boolean canBeAttackedWithItem() {
        return false;
    }

    @Override
    public void onCollideWithPlayer(@Nonnull EntityPlayer player) {
        if (!this.world.isRemote) {
            PowerHandler power = player.getCapability(CapabilityPowerHandler.POWER_CAP, null);
            if (this.delayBeforeCanPickup == 0 && player.xpCooldown == 0 && power != null) {
                player.xpCooldown = 2;
                this.onPickup(player, 1);
                if (this.powerValue > 0) {
                    if (power.get() + powerValue / 100.0f > PowerHandler.MAX_POWER) {
                        power.add(PowerHandler.MAX_POWER - power.get());
                        int residualValue = powerValue - (int) (PowerHandler.MAX_POWER * 100) + (int) (power.get() * 100);
                        // 和原版设计不同，该数值过大，故缩小一些
                        player.addExperience(transPowerValueToXpValue(residualValue));
                    } else {
                        power.add(powerValue / 100.0f);
                    }
                }
                this.setDead();
            }
        }
    }

    public void onPickup(EntityLivingBase base, int quantity) {
        if (!this.isDead && !this.world.isRemote) {
            EntityTracker entitytracker = ((WorldServer) this.world).getEntityTracker();
            entitytracker.sendToTracking(this, new SPacketCollectItem(this.getEntityId(), base.getEntityId(), quantity));
        }
    }

    public void spawnExplosionParticle() {
        float x = (float) posX;
        float y = (float) posY + 0.125F;
        float z = (float) posZ;
        if (world.isRemote) {
            spawnExplosionParticle(world, x, y, z, rand);
        } else {
            CommonProxy.INSTANCE.sendToAllAround(new BeaconAbsorbMessage(x, y, z), new TargetPoint(dimension, x, y, z, 16));
        }
    }

    @SideOnly(Side.CLIENT)
    public int getTextureByXP() {
        if (this.powerValue >= 485) {
            return 10;
        } else if (this.powerValue >= 385) {
            return 9;
        } else if (this.powerValue >= 285) {
            return 8;
        } else if (this.powerValue >= 185) {
            return 7;
        } else if (this.powerValue >= 89) {
            return 6;
        } else if (this.powerValue >= 36) {
            return 5;
        } else if (this.powerValue >= 17) {
            return 4;
        } else if (this.powerValue >= 7) {
            return 3;
        } else if (this.powerValue >= 5) {
            return 2;
        } else {
            return this.powerValue >= 3 ? 1 : 0;
        }
    }
}
