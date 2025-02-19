package com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.geckolayer;

import com.github.tartaricacid.touhoulittlemaid.api.entity.IMaid;
import com.github.tartaricacid.touhoulittlemaid.capability.GeckoMaidEntityCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.client.entity.GeckoMaidEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.GeoLayerRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntity;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.IGeoEntityRenderer;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.AnimatedGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.animated.IAnimatedModel;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

import java.util.function.Function;

public abstract class GeoLayerMaidRender<T extends Entity, R extends IGeoEntityRenderer<T>> extends GeoLayerRenderer<T, R> {
    //@Final
    private static Function<Mob, IGeoEntity> YSM_GEO_MOB_GET;

    public GeoLayerMaidRender(R entityRendererIn) {
        super(entityRendererIn);
    }

    // 本模组的
    @SuppressWarnings("unchecked")
    protected static GeckoMaidEntity<Mob> getGeoMob(Mob mob) {
        return mob.getCapability(GeckoMaidEntityCapabilityProvider.CAP)
                .map(e -> (GeckoMaidEntity<Mob>) e)
                .orElse(new GeckoMaidEntity<>(mob, IMaid.convert(mob)));
    }

    protected static AnimatedGeoModel getGeoMobModel(Mob mob) {
        return getGeoMob(mob).getCurrentModel();
    }

    // ysm的
    protected static IGeoEntity getYsmGeoMob(Mob mob) {
        return YSM_GEO_MOB_GET.apply(mob);
    }

    protected static IAnimatedModel getYsmGeoMobModel(Mob mob) {
        return getYsmGeoMob(mob).getGeoModel();
    }

    protected static void initYsmGeoMobGet(Function<Mob, IGeoEntity> ysmGeoMobGet) {
        if (YSM_GEO_MOB_GET == null) {
            YSM_GEO_MOB_GET = ysmGeoMobGet;
        }
    }

    @Override
    public abstract void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks,
                                float netHeadYaw, float headPitch);

    // 生成对应的YsmGeoLayerRenderer
    public abstract GeoLayerMaidRender<T, R> create(R geckoEntityMaidRenderer,
                                                    EntityRendererProvider.Context renderManager,
                                                    Function<Mob, IGeoEntity> ysmGeoMob);

}
