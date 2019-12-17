package com.github.tartaricacid.touhoulittlemaid.danmaku.script;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

/**
 * @author TartaricAcid
 * @date 2019/11/26 15:08
 **/
public class Vec3dWrapper {
    private Vec3d vec3d;

    public Vec3dWrapper(Vec3d vec3d) {
        this.vec3d = vec3d;
    }

    public Vec3dWrapper(double x, double y, double z) {
        this.vec3d = new Vec3d(x, y, z);
    }

    public static Vec3dWrapper getRotationVector(double x, double y, double z, float yawIn, double yOffset, EntityLivingBaseWrapper entityWrapper) {
        EntityLivingBase entity = entityWrapper.getLivingBase();
        float yaw = (entity.rotationYaw + yawIn) * -0.01745329251f;
        Vec3d pos = entity.getPositionVector();
        Vec3d vec3d = (new Vec3d(x, y, z)).rotateYaw(yaw).add(pos.x, pos.y + entity.getEyeHeight() + yOffset, pos.z);
        return new Vec3dWrapper(vec3d.x, vec3d.y, vec3d.z);
    }

    public Vec3d getVec3d() {
        return vec3d;
    }

    public double getX() {
        return vec3d.x;
    }

    public double getY() {
        return vec3d.y;
    }

    public double getZ() {
        return vec3d.z;
    }
}
