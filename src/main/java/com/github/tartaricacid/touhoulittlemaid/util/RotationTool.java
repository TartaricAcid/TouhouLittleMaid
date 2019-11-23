package com.github.tartaricacid.touhoulittlemaid.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.Vec3d;

import javax.vecmath.Vector3d;

/**
 * @author TartaricAcid
 * @date 2019/11/23 19:34
 **/
public final class RotationTool {
    public static Vector3d getRotationVector(double x, double y, double z, float yaw, double yOffset, EntityLivingBase entity) {
        Vec3d vec3d = (new Vec3d(x, y, z)).rotateYaw((entity.rotationYaw + yaw) * -0.01745329251f)
                .add(entity.getPositionVector().x, entity.getPositionVector().y + entity.getEyeHeight() + yOffset, entity.getPositionVector().z);
        return new Vector3d(vec3d.x, vec3d.y, vec3d.z);
    }
}
