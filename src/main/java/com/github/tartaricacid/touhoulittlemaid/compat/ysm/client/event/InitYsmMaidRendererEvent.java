package com.github.tartaricacid.touhoulittlemaid.compat.ysm.client.event;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class InitYsmMaidRendererEvent extends Event implements IModBusEvent {
    private final EntityRendererProvider.Context manager;
    private IGeoEntityRenderer<Mob> geoEntityRenderer2;
    private Function<Mob, IGeoEntity> ysmGeoEntityGet;

    public InitYsmMaidRendererEvent(EntityRendererProvider.Context manager) {
        this.manager = manager;
    }

    @Nullable
    public IGeoEntityRenderer<Mob> getGeoEntityRenderer() {
        return geoEntityRenderer2;
    }

    public void setGeoEntityRenderer2(IGeoEntityRenderer<Mob> geoEntityRenderer) {
        this.geoEntityRenderer2 = geoEntityRenderer;
    }

    @Nullable
    public Function<Mob, IGeoEntity> getYsmGeoEntityGet() {
        return ysmGeoEntityGet;
    }

    public void setYsmGeoEntityGet(Function<Mob, IGeoEntity> ysmGeoEntityGet) {
        this.ysmGeoEntityGet = ysmGeoEntityGet;
    }

    public EntityRendererProvider.Context getManager() {
        return manager;
    }
}
