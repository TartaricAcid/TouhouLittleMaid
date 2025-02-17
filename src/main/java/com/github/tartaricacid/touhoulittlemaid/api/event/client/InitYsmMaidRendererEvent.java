package com.github.tartaricacid.touhoulittlemaid.api.event.client;

import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity2;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer2;
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
    @Nullable
    private IGeoEntityRenderer2<Mob> geoEntityRenderer2;
    @Nullable
    private Function<Mob, IGeoEntity2> ysmGeoEntityGet;

    public InitYsmMaidRendererEvent(EntityRendererProvider.Context manager) {
        this.manager = manager;
    }

    @Nullable
    public IGeoEntityRenderer2<Mob> getGeoEntityRenderer2() {
        return geoEntityRenderer2;
    }

    public void setGeoEntityRenderer2(IGeoEntityRenderer2<Mob> geoEntityRenderer2) {
        this.geoEntityRenderer2 = geoEntityRenderer2;
    }

    @Nullable
    public Function<Mob, IGeoEntity2> getYsmGeoEntityGet() {
        return ysmGeoEntityGet;
    }

    public void setYsmGeoEntityGet(Function<Mob, IGeoEntity2> ysmGeoEntityGet) {
        this.ysmGeoEntityGet = ysmGeoEntityGet;
    }

    public EntityRendererProvider.Context getManager() {
        return manager;
    }
}
