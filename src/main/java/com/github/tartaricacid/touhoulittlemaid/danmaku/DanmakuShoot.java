package com.github.tartaricacid.touhoulittlemaid.danmaku;

import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public final class DanmakuShoot {
    private static Random RANDOM = new Random();

    /**
     * 自机狙弹幕
     *
     * @param worldIn    发射者所处世界
     * @param thrower    发射者
     * @param target     发射朝向的目标
     * @param damage     弹幕伤害数值
     * @param gravity    弹幕重力数值，很小
     * @param velocity   弹幕速度
     * @param inaccuracy 弹幕不准确度，数值越大弹幕越散
     * @param type       弹幕类型
     * @param color      弹幕颜色
     */
    public static void aimedShot(World worldIn, EntityLivingBase thrower, EntityLivingBase target, float damage, float gravity, Float velocity, Float inaccuracy, DanmakuType type, DanmakuColor color) {
        if (!worldIn.isRemote) {
            EntityDanmaku danmaku = new EntityDanmaku(worldIn, thrower, damage, gravity, type, color);
            float offset = 0.3f / target.height;
            danmaku.shoot(target.posX - thrower.posX, target.posY - thrower.posY - offset, target.posZ - thrower.posZ, velocity, inaccuracy);
            worldIn.spawnEntity(danmaku);
        }
        worldIn.playSound(null, thrower.posX, thrower.posY, thrower.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, thrower.getSoundCategory(), 1.0f, 0.8f);
    }


    /**
     * 扇形弹弹幕
     *
     * @param worldIn    发射者所处世界
     * @param thrower    发射者
     * @param target     发射朝向的目标
     * @param damage     弹幕伤害数值
     * @param gravity    弹幕重力数值，很小
     * @param velocity   弹幕速度
     * @param inaccuracy 弹幕不准确度，数值越大弹幕越散
     * @param type       弹幕类型
     * @param color      弹幕颜色
     * @param yawTotal   偏航总角度，弧度表示（以发射者和发射目标为中心左右对称）
     * @param fanNum     扇形弹链数（fanNum >=2）
     */
    public static void fanShapedShot(World worldIn, EntityLivingBase thrower, EntityLivingBase target, float damage, float gravity, Float velocity, Float inaccuracy, DanmakuType type, DanmakuColor color, double yawTotal, int fanNum) {
        if (yawTotal < 0 || yawTotal > 2 * Math.PI || fanNum < 2) {
            return;
        }

        if (!worldIn.isRemote) {
            float offset = 0.3f / target.height;
            Vec3d v = new Vec3d(target.posX - thrower.posX, target.posY - thrower.posY - offset, target.posZ - thrower.posZ);
            double yaw = -(yawTotal / 2);
            double addYaw = yawTotal / (fanNum - 1);
            for (int i = 1; i <= fanNum; i++) {
                Vec3d v1 = v.rotateYaw((float) yaw);
                yaw = yaw + addYaw;

                EntityDanmaku danmaku = new EntityDanmaku(worldIn, thrower, damage, gravity, type, color);
                danmaku.shoot(v1.x, v1.y, v1.z, velocity, inaccuracy);
                worldIn.spawnEntity(danmaku);
            }
        }
        worldIn.playSound(null, thrower.posX, thrower.posY, thrower.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, thrower.getSoundCategory(), 1.0f, 0.8f);
    }
}
