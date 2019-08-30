package com.github.tartaricacid.touhoulittlemaid.entity.monster;

import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/8/30 14:38
 **/
public class EntityFairy extends AbstractEntityTouhouMob {
    public EntityFairy(World worldIn) {
        super(worldIn);
    }

    @Override
    public int getPowerPoint() {
        return 50;
    }
}
