package com.github.tartaricacid.touhoulittlemaid.proxy;

import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.particle.ParticleEnum;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityDanmakuRender;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRender;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMarisaBroomRender;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPackPOJO;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ClientProxy extends CommonProxy {
    /**
     * 实体缓存，在客户端会大量运用实体渲染，这个缓存可以减少重复创建实体带来的性能问题
     */
    public static final Cache<String, Entity> ENTITY_CACHE = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();
    public static List<CustomModelPackPOJO> MODEL_PACK_LIST = new ArrayList<>();
    public static HashMap<String, EntityModelJson> LOCATION_MODEL_MAP = new HashMap<>();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        RenderingRegistry.registerEntityRenderingHandler(EntityMaid.class, EntityMaidRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityDanmaku.class, EntityDanmakuRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityMarisaBroom.class, EntityMarisaBroomRender.FACTORY);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        Minecraft.getMinecraft().effectRenderer.registerParticle(ParticleEnum.FLAG.getId(), ParticleEnum.FLAG.getParticle());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    /**
     * 重新复写一遍原版本地化方法
     */
    @Override
    public String translate(String key, Object... format) {
        return I18n.format(key, format);
    }
}
