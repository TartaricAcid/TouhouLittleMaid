package com.github.tartaricacid.touhoulittlemaid.entity.item;

import com.github.tartaricacid.touhoulittlemaid.api.AbstractEntityMaid;
import com.github.tartaricacid.touhoulittlemaid.api.IEntityRidingMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2020/2/2 21:24
 **/
public abstract class AbstractEntityTrolley extends AbstractEntityFromItem implements IEntityRidingMaid {
    AbstractEntityTrolley(World worldIn) {
        super(worldIn);
    }

    @Override
    public void updatePassenger(AbstractEntityMaid maid) {
        if (maid.isPassenger(this)) {
            // 视线也必须同步，因为拉杆箱的朝向受视线限制
            // 只能以视线方向为中心左右各 90 度，不同步就会导致朝向错误
            this.rotationYawHead = maid.rotationYawHead;
            // 旋转方向同步，包括渲染的旋转方向
            this.rotationPitch = maid.rotationPitch;
            this.rotationYaw = maid.rotationYaw;
            this.renderYawOffset = maid.renderYawOffset;
            Vec3d vec3d = maid.getPositionVector().add(new Vec3d(0.4, 0, -1).rotateYaw(maid.renderYawOffset * -0.01745329251f));
            this.setPosition(vec3d.x, vec3d.y, vec3d.z);
        }
    }

    @Override
    public void dismountEntity(Entity entityIn) {
        if (!(entityIn instanceof EntityMaid)) {
            super.dismountEntity(entityIn);
        }
    }
}
