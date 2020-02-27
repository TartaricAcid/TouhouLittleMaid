package com.github.tartaricacid.touhoulittlemaid.proxy;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.command.ReloadHataCommand;
import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.particle.ParticleEnum;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.*;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.HataTextureManager;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomHataTextureLoader;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.danmaku.CustomSpellCardEntry;
import com.github.tartaricacid.touhoulittlemaid.entity.item.*;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityRinnosuke;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.client.resource.VanillaResourceType;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import noppes.npcs.client.model.ModelPlayerAlt;
import noppes.npcs.entity.EntityCustomNpc;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements ISelectiveResourceReloadListener {
    /**
     * 实体缓存，在客户端会大量运用实体渲染，这个缓存可以减少重复创建实体带来的性能问题
     */
    public static final Cache<String, Entity> ENTITY_CACHE = CacheBuilder.newBuilder().expireAfterAccess(5, TimeUnit.MINUTES).build();
    /**
     * 指物旗部分
     */
    public static final Map<Long, Integer> HATA_NAME_MAP = Maps.newHashMap();
    public static HataTextureManager HATA_TEXTURE_MANAGER;
    /**
     * 仅用于客户端显示文本提示
     * 初始化时，先让其等于服务端数据，规避因为初始化物品此 Map 为空导致的崩溃问题
     */
    public static final Map<String, CustomSpellCardEntry> CUSTOM_SPELL_CARD_MAP_CLIENT = Maps.newHashMap();

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        InfoGetManager.checkInfoJsonFile();
        OBJLoader.INSTANCE.addDomain(TouhouLittleMaid.MOD_ID);
        ((IReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(this);
        RenderingRegistry.registerEntityRenderingHandler(EntityMaid.class, EntityMaidRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityDanmaku.class, EntityDanmakuRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityMarisaBroom.class, EntityMarisaBroomRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityChair.class, EntityChairRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityRinnosuke.class, EntityRinnosukeRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityPowerPoint.class, EntityPowerPointRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityXPOrb.class, EntityChangeXPRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntitySlime.class, EntityYukkuriSlimeRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityFairy.class, EntityFairyRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityBox.class, EntityBoxRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntitySuitcase.class, EntitySuitcaseRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityBackpack.class, EntityBackpackRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityTrolleyAudio.class, EntityTrolleyAudioRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityMaidVehicle.class, EntityMaidVehicleRender.FACTORY);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        Minecraft.getMinecraft().effectRenderer.registerParticle(ParticleEnum.FLAG.getId(), ParticleEnum.FLAG.getParticle());
        HATA_TEXTURE_MANAGER = new HataTextureManager(Minecraft.getMinecraft().getResourceManager());
        CustomHataTextureLoader.onHataTextureReload();
        ClientCommandHandler.instance.registerCommand(new ReloadHataCommand());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
        if (CommonProxy.isNpcModLoad() && GeneralConfig.MISC_CONFIG.overrideNpcRender) {
            changeNpcRender();
        }
    }

    @Optional.Method(modid = "customnpcs")
    private void changeNpcRender() {
        RenderingRegistry.registerEntityRenderingHandler(EntityCustomNpc.class, new EntityCustomNpcChangeRender<>(new ModelPlayerAlt(0.0F, false)));
    }

    /**
     * 重新复写一遍原版本地化方法
     */
    @Override
    public String translate(String key, Object... format) {
        return I18n.format(key, format);
    }

    @Override
    public Map<String, CustomSpellCardEntry> getSpellCard() {
        return CUSTOM_SPELL_CARD_MAP_CLIENT;
    }

    @Override
    public void setSpellCard(Map<String, CustomSpellCardEntry> entryMap) {
        CUSTOM_SPELL_CARD_MAP_CLIENT.clear();
        CUSTOM_SPELL_CARD_MAP_CLIENT.putAll(entryMap);
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
