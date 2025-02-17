package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer.v2;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.GeckoMaidEntityCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity2;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer2;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.IAnimatedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

import java.util.function.Function;

public abstract class GeoLayerMaidRender2<T extends Entity, R extends IGeoEntityRenderer2<T>> extends GeoLayerRenderer<T, R> {
    //@Final
    private static Function<Mob, IGeoEntity2> YSM_GEO_MOB_GET;

    public GeoLayerMaidRender2(R entityRendererIn) {
        super(entityRendererIn);
    }

    @SuppressWarnings("unchecked")
    protected static GeckoMaidEntity<Mob> getGeoMob(Mob mob) {
        return mob.getCapability(GeckoMaidEntityCapabilityProvider.CAP)
                .map(e -> (GeckoMaidEntity<Mob>) e)
                .orElse(new GeckoMaidEntity<>(mob, IMaid.convert(mob)));
    }

    protected static AnimatedGeoModel getGeoMobModel(Mob mob) {
        return getGeoMob(mob).getCurrentModel();
    }

    protected static IGeoEntity2 getYsmGeoMob(Mob mob) {
        return YSM_GEO_MOB_GET.apply(mob);
    }

    protected static IAnimatedModel<?> getYsmGeoMobModel(Mob mob) {
        return getYsmGeoMob(mob).getGeoModel();
    }

    protected static void initYsmGeoMobGet(Function<Mob, IGeoEntity2> ysmGeoMobGet) {
        if (YSM_GEO_MOB_GET == null) {
            YSM_GEO_MOB_GET = ysmGeoMobGet;
        }
    }

    public abstract void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                                float netHeadYaw, float headPitch);

    public abstract void ysmRender(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn,
                                   T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                                   float netHeadYaw, float headPitch);

    public abstract GeoLayerMaidRender2<T, R> create(R geckoEntityMaidRenderer,
                                                     EntityRendererProvider.Context renderManager,
                                                     Function<Mob, IGeoEntity2> ysmGeoMob);

}
