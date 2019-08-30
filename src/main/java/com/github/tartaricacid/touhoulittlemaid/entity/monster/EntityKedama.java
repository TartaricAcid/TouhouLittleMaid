package com.github.tartaricacid.touhoulittlemaid.entity.monster;

import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/8/30 14:35
 **/
public class EntityKedama extends AbstractEntityTouhouMob {
    public EntityKedama(World worldIn) {
        super(worldIn);
    }

    @Override
    public int getPowerPoint() {
        return 50;
    }
}
