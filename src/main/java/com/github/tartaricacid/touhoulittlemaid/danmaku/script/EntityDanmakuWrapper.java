package com.github.tartaricacid.touhoulittlemaid.danmaku.script;

import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuColor;
import com.github.tartaricacid.touhoulittlemaid.danmaku.DanmakuType;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;

/**
 * @author TartaricAcid
 * @date 2019/11/26 14:41
 **/
public class EntityDanmakuWrapper {
    private EntityDanmaku danmaku;

    public EntityDanmakuWrapper(WorldWrapper worldIn, EntityLivingBaseWrapper throwerIn, DanmakuType type, DanmakuColor color) {
        this.danmaku = new EntityDanmaku(worldIn.getWorld(), throwerIn.getLivingBase(), type, color);
    }

    public EntityDanmakuWrapper(WorldWrapper worldIn, EntityLivingBaseWrapper throwerIn, float damage, float gravity, DanmakuType type, DanmakuColor color) {
        this.danmaku = new EntityDanmaku(worldIn.getWorld(), throwerIn.getLivingBase(), damage, gravity, type, color);
    }

    public void setTicksExisted(int ticksExisted) {
        this.danmaku.setDanmakuTicksExisted(ticksExisted);
    }

    public void setType(DanmakuType type) {
        this.danmaku.setType(type);
    }

    public void setColor(DanmakuColor color) {
        this.danmaku.setColor(color);
    }

    public void shoot(EntityLivingBaseWrapper entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy) {
        this.danmaku.shoot(entityThrower.getLivingBase(), rotationPitchIn, rotationYawIn, pitchOffset, velocity, inaccuracy);
    }

    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        this.danmaku.shoot(x, y, z, velocity, inaccuracy);
    }

    public void setPosition(Vec3dWrapper vec3d) {
        this.danmaku.setPosition(vec3d.getX(), vec3d.getY(), vec3d.getZ());
    }

    public EntityDanmaku getDanmaku() {
        return this.danmaku;
    }

    public void setDamagesTerrain(boolean canDamages) {
        this.danmaku.setDamagesTerrain(canDamages);
    }
}
