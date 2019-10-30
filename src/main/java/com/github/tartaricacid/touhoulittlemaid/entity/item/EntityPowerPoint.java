package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.capability.CapabilityPowerHandler;
import com.github.tartaricacid.touhoulittlemaid.capability.PowerHandler;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.BeaconAbsorbMessage;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
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
public class EntityPowerPoint extends EntityXPOrb {
    public EntityPowerPoint(World worldIn) {
        super(worldIn);
    }

    /**
     * 生成 P 点
     *
     * @param powerValue P 点值，传入数据将会 / 100.0f 转换为玩家的 Power 数
     */
    public EntityPowerPoint(World worldIn, double x, double y, double z, int powerValue) {
        super(worldIn, x, y, z, powerValue);
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

    @Override
    public void onCollideWithPlayer(@Nonnull EntityPlayer player) {
        if (!this.world.isRemote) {
            PowerHandler power = player.getCapability(CapabilityPowerHandler.POWER_CAP, null);
            if (this.delayBeforeCanPickup == 0 && player.xpCooldown == 0 && power != null) {
                player.xpCooldown = 2;
                player.onItemPickup(this, 1);
                if (this.xpValue > 0) {
                    if (power.get() + xpValue / 100.0f > PowerHandler.MAX_POWER) {
                        power.add(PowerHandler.MAX_POWER - power.get());
                        int residualValue = xpValue - (int) (PowerHandler.MAX_POWER * 100) + (int) (power.get() * 100);
                        // 和原版设计不同，该数值过大，故缩小一些
                        player.addExperience(residualValue / 4);
                    } else {
                        power.add(xpValue / 100.0f);
                    }
                }
                this.setDead();
            }
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
    @SideOnly(Side.CLIENT)
    public int getTextureByXP() {
        if (this.xpValue >= 485) {
            return 10;
        } else if (this.xpValue >= 385) {
            return 9;
        } else if (this.xpValue >= 285) {
            return 8;
        } else if (this.xpValue >= 185) {
            return 7;
        } else if (this.xpValue >= 89) {
            return 6;
        } else if (this.xpValue >= 36) {
            return 5;
        } else if (this.xpValue >= 17) {
            return 4;
        } else if (this.xpValue >= 7) {
            return 3;
        } else if (this.xpValue >= 5) {
            return 2;
        } else {
            return this.xpValue >= 3 ? 1 : 0;
        }
    }
}
