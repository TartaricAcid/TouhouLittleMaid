package com.github.tartaricacid.touhoulittlemaid.proxy;

import com.github.tartaricacid.touhoulittlemaid.client.command.ReloadHataCommand;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.particle.ParticleEnum;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityChairRender;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityDanmakuRender;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMaidRender;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.EntityMarisaBroomRender;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.HataTextureManager;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomHataTextureLoader;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.CustomModelPackPOJO;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.ModelItem;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityMarisaBroom;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class ClientProxy extends CommonProxy implements ISelectiveResourceReloadListener {
    /**
     * 实体缓存，在客户端会大量运用实体渲染，这个缓存可以减少重复创建实体带来的性能问题
     */
    public static final Cache<String, Entity> ENTITY_CACHE = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();
    /**
     * 当前所有模型列表，用于 GUI 显示
     */
    public static final List<CustomModelPackPOJO> MODEL_PACK_LIST = Lists.newArrayList();
    public static final List<CustomModelPackPOJO> CHAIR_PACK_LIST = Lists.newArrayList();
    /**
     * 用于渲染模型操作
     */
    public static final HashMap<String, EntityModelJson> ID_MODEL_MAP = Maps.newHashMap();
    public static final HashMap<String, EntityModelJson> ID_CHAIR_MAP = Maps.newHashMap();
    /**
     * 用于添加材质，显示模型名称等操作
     */
    public static final HashMap<String, ModelItem> ID_MODEL_INFO_MAP = Maps.newHashMap();
    public static final HashMap<String, ModelItem> ID_CHAIR_INFO_MAP = Maps.newHashMap();

    /**
     * 指物旗部分
     */
    public static final Map<Long, Integer> HATA_NAME_MAP = Maps.newHashMap();
    public static HataTextureManager HATA_TEXTURE_MANAGER;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
        RenderingRegistry.registerEntityRenderingHandler(EntityMaid.class, EntityMaidRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityDanmaku.class, EntityDanmakuRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityMarisaBroom.class, EntityMarisaBroomRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityChair.class, EntityChairRender.FACTORY);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        Minecraft.getMinecraft().effectRenderer.registerParticle(ParticleEnum.FLAG.getId(), ParticleEnum.FLAG.getParticle());
        HATA_TEXTURE_MANAGER = new HataTextureManager(Minecraft.getMinecraft().getResourceManager());
        CustomHataTextureLoader.onHataTextureReload(Objects.requireNonNull(getClass().getClassLoader().getResource("assets/touhou_little_maid/hata_texture")));
        ClientCommandHandler.instance.registerCommand(new ReloadHataCommand());
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

    /**
     * 重载服务端名称
     */
    @Override
    public void onResourceManagerReload(@Nonnull IResourceManager resourceManager, @Nonnull Predicate<IResourceType> resourcePredicate) {
        if (resourcePredicate.test(VanillaResourceType.LANGUAGES)) {
            initModelList();
        }
    }
}
