package com.github.tartaricacid.touhoulittlemaid.danmaku.script;

import net.minecraft.world.World;

/**
 * @author TartaricAcid
 * @date 2019/11/26 14:45
 **/
public class WorldWrapper {
    private World world;

    public WorldWrapper(World world) {
        this.world = world;
    }

    public void spawnDanmaku(EntityDanmakuWrapper danmakuWrapper) {
        this.world.spawnEntity(danmakuWrapper.getDanmaku());
    }

    public World getWorld() {
        return this.world;
    }
}
