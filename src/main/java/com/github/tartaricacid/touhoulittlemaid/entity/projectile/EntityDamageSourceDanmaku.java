package com.github.tartaricacid.touhoulittlemaid.entity.projectile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.IndirectEntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
    public ITextComponent getLocalizedDeathMessage(LivingEntity victim) {
        if (getEntity() != null) {
            int index = victim.level.random.nextInt(3) + 1;
            return new TranslationTextComponent(String.format("death.touhou_little_maid.attack.danmaku.%d", index),
                    victim.getDisplayName(), getEntity().getDisplayName());
        }
        return StringTextComponent.EMPTY;
    }
}
