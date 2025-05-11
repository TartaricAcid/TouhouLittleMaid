package com.github.tartaricacid.touhoulittlemaid.init;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

public final class InitDamage {
    public static final ResourceKey<DamageType> DANMAKU = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(TouhouLittleMaid.MOD_ID, "danmaku"));
    public static final ResourceKey<DamageType> DANMAKU_ENDER_KILLER = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(TouhouLittleMaid.MOD_ID, "danmaku_ender_killer"));

    public static DamageSource danmakuDamage(Entity thrower, EntityDanmaku danmaku) {
        Registry<DamageType> damageTypes = thrower.level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        if (danmaku.isHurtEnderman()) {
            return new DamageSource(damageTypes.getHolderOrThrow(DANMAKU_ENDER_KILLER), danmaku, thrower);
        } else {
            return new DamageSource(damageTypes.getHolderOrThrow(DANMAKU), danmaku, thrower);
        }
    }
}
