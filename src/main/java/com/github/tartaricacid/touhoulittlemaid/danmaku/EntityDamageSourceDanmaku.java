package com.github.tartaricacid.touhoulittlemaid.danmaku;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class EntityDamageSourceDanmaku extends EntityDamageSourceIndirect {
    public EntityDamageSourceDanmaku(Entity source, @Nullable Entity indirectEntityIn) {
        super("magic", source, indirectEntityIn);
        this.setDamageBypassesArmor();
        this.setMagicDamage();
        this.setProjectile();
    }

    @Nonnull
    @Override
    public ITextComponent getDeathMessage(EntityLivingBase victim) {
        if (getTrueSource() != null) {
            int index = victim.world.rand.nextInt(3) + 1;
            return new TextComponentTranslation(String.format("death.touhou_little_maid.attack.danmaku.%d", index),
                    victim.getDisplayName(), getTrueSource().getDisplayName());
        }
        return new TextComponentString("");
    }
}
