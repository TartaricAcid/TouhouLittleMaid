package com.github.tartaricacid.touhoulittlemaid.entity.monster;

import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityPowerPoint;
import com.github.tartaricacid.touhoulittlemaid.network.simpleimpl.SyncPowerPointEntityData;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/8/30 14:20
 **/
public abstract class AbstractEntityTouhouMob extends EntityMob {
    public AbstractEntityTouhouMob(World worldIn) {
        super(worldIn);
    }

    /**
     * 获取 Power 点数值
     *
     * @return Power 点数值，与玩家 Power 数是 100 倍的关系
     */
    abstract public int getPowerPoint();

    @Override
    protected void onDeathUpdate() {
        super.onDeathUpdate();
        if (this.deathTime == 20) {
            if (!this.world.isRemote) {
                int totalPowerPoint = getPowerPoint();
                while (totalPowerPoint > 0) {
                    int powerSplit = EntityPowerPoint.getPowerSplit(totalPowerPoint);
                    totalPowerPoint -= powerSplit;
                    EntityPowerPoint powerPoint = new EntityPowerPoint(this.world, this.posX, this.posY, this.posZ, powerSplit);
                    this.world.spawnEntity(powerPoint);
                    CommonProxy.INSTANCE.sendToAll(new SyncPowerPointEntityData(powerPoint));
                }
            }
        }
    }
}
