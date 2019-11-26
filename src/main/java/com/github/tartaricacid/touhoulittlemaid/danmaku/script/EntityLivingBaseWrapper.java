package com.github.tartaricacid.touhoulittlemaid.danmaku.script;

import net.minecraft.entity.EntityLivingBase;

/**
 * @author TartaricAcid
 * @date 2019/11/26 14:43
 **/
public class EntityLivingBaseWrapper {
    private EntityLivingBase livingBase;

    public EntityLivingBaseWrapper(EntityLivingBase livingBase) {
        this.livingBase = livingBase;
    }

    public EntityLivingBase getLivingBase() {
        return this.livingBase;
    }

    public float getYaw() {
        return this.livingBase.rotationYaw;
    }

    public float getPitch() {
        return this.livingBase.rotationPitch;
    }

    public Vec3dWrapper getPos() {
        return new Vec3dWrapper(this.livingBase.getPositionVector());
    }
}
