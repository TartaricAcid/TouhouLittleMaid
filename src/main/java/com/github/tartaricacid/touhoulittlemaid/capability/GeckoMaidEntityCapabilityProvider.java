package com.github.tartaricacid.touhoulittlemaid.capability;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class GeckoMaidEntityCapabilityProvider<E extends Mob & IMaid> implements ICapabilityProvider {
    public static Capability<GeckoMaidEntity<?>> CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    private GeckoMaidEntity<E> instance = null;
    private E entity;

    @SuppressWarnings("unchecked")
    public GeckoMaidEntityCapabilityProvider(Entity entity) {
        this.entity = (E) entity;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAP.orEmpty(cap, LazyOptional.of(this::createCapability).cast()).cast();
    }

    @Nonnull
    private GeckoMaidEntity<E> createCapability() {
        if (this.instance == null) {
            this.instance = new GeckoMaidEntity<>(entity);
            this.entity = null;
        }
        return this.instance;
    }
}
