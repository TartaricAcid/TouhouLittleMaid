package com.github.tartaricacid.touhoulittlemaid.client.particle;

import net.minecraft.client.particle.IParticleFactory;

/**
 * @author TartaricAcid
 * @date 2019/7/5 21:53
 **/
public enum ParticleEnum {
    // 旗帜粒子效果
    FLAG("flag", 943999, ParticleFlag.FACTORY);

    private String name;
    private int id;
    private IParticleFactory particle;

    ParticleEnum(String name, int id, IParticleFactory particle) {
        this.name = name;
        this.id = id;
        this.particle = particle;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public IParticleFactory getParticle() {
        return particle;
    }
}
