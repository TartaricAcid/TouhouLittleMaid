package com.github.tartaricacid.touhoulittlemaid.capability;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@OnlyIn(Dist.CLIENT)
public class GeckoMaidEntityCapabilityProvider<E extends Mob> implements ICapabilityProvider {
    public static Capability<GeckoMaidEntity<?>> CAP = CapabilityManager.get(new CapabilityToken<>() {
    });
    private GeckoMaidEntity<E> instance = null;
    private E entity;
    private IMaid maid;

    public GeckoMaidEntityCapabilityProvider(E mob, IMaid maid) {
        this.entity = mob;
        this.maid = maid;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return CAP.orEmpty(cap, LazyOptional.of(this::createCapability).cast()).cast();
    }

    @Nonnull
    private GeckoMaidEntity<E> createCapability() {
        if (this.instance == null) {
            this.instance = new GeckoMaidEntity<>(this.entity, this.maid);
            this.entity = null;
            this.maid = null;
        }
        return this.instance;
    }
}
