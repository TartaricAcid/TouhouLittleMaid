package com.github.tartaricacid.touhoulittlemaid.proxy;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.audio.music.MusicManger;
import com.github.tartaricacid.touhoulittlemaid.client.command.ReloadHataCommand;
import com.github.tartaricacid.touhoulittlemaid.client.command.ReloadMaidResCommand;
import com.github.tartaricacid.touhoulittlemaid.client.download.InfoGetManager;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.entity.*;
import com.github.tartaricacid.touhoulittlemaid.client.renderer.texture.HataTextureManager;
import com.github.tartaricacid.touhoulittlemaid.client.resources.CustomHataTextureLoader;
import com.github.tartaricacid.touhoulittlemaid.config.GeneralConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.item.*;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityFairy;
import com.github.tartaricacid.touhoulittlemaid.entity.monster.EntityRinnosuke;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.projectile.EntityDanmaku;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.IResourceManager;
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
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements ISelectiveResourceReloadListener {
    /**
     * 指物旗部分
     */
    public static final Map<Long, Integer> HATA_NAME_MAP = Maps.newHashMap();
    public static HataTextureManager HATA_TEXTURE_MANAGER;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);

        // 加载音乐歌单
        MusicManger.loadMusicList();
        // 加载游戏内资源包下载
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
        RenderingRegistry.registerEntityRenderingHandler(EntityScarecrow.class, EntityScarecrowRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityPortableAudio.class, EntityPortableAudioRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityExtinguishingAgent.class, EntityExtinguishingAgentRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityThrowPowerPoint.class, EntityThrowPowerPointRender.FACTORY);
        RenderingRegistry.registerEntityRenderingHandler(EntityMaidJoy.class, EntityMaidJoyRender.FACTORY);
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        HATA_TEXTURE_MANAGER = new HataTextureManager(Minecraft.getMinecraft().getResourceManager());
        CustomHataTextureLoader.onHataTextureReload();
        ClientCommandHandler.instance.registerCommand(new ReloadHataCommand());
        ClientCommandHandler.instance.registerCommand(new ReloadMaidResCommand());
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
