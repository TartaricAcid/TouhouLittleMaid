package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import javax.annotation.Nullable;

public class EntityDamageSourceDanmaku extends IndirectEntityDamageSource {
    private static final String DAMAGE_TYPE = "magic";

    public EntityDamageSourceDanmaku(Entity source, @Nullable Entity indirectEntityIn) {
        super(DAMAGE_TYPE, source, indirectEntityIn);
        this.bypassArmor();
        this.setMagic();
        this.setProjectile();
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity victim) {
        if (getEntity() != null) {
            int index = victim.level.random.nextInt(3) + 1;
            return Component.translatable(String.format("death.touhou_little_maid.attack.danmaku.%d", index),
                    victim.getDisplayName(), getEntity().getDisplayName());
        }
        return Component.empty();
    }
}
