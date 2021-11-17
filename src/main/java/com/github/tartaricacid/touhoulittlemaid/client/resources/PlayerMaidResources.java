package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.CustomJsAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@SideOnly(Side.CLIENT)
public class PlayerMaidResources {
    private static final Cache<String, GameProfile> GAME_PROFILE_CACHE = CacheBuilder.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES).build();
    private static final ExecutorService THREAD_POOL = new ThreadPoolExecutor(0, 2, 1, TimeUnit.MINUTES, new LinkedBlockingQueue());

    private static final GameProfile EMPTY_GAME_PROFILE = new GameProfile(null, "EMPTY");
    private static final ResourceLocation PLAYER_MAID_MODEL_RES = new ResourceLocation(TouhouLittleMaid.MOD_ID, "models/entity/player_maid.json");
    private static final ResourceLocation PLAYER_MAID_SLIM_MODEL_RES = new ResourceLocation(TouhouLittleMaid.MOD_ID, "models/entity/player_maid_slim.json");
    private static final List<ResourceLocation> PLAYER_MAID_ANIMATION_RES = Lists.newArrayList(
            new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/head/default.js"),
            new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/head/beg.js"),
            new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/leg/default.js"),
            new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/player/arm/default.js"),
            new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/default/arm/swing.js"),
            new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid/player/sit/default.js")
    );
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    private static final List<Object> PLAYER_MAID_ANIMATIONS = Lists.newArrayList();
    private static final String SLIM_NAME = "slim";
    private static EntityModelJson playerMaidModel;
    private static EntityModelJson playerMaidModelSlim;
    private static MaidModelInfo playerMaidInfo;
    private static ResourceLocation playerSkin;

    public static void reloadResources() {
        playerMaidModel = CustomResourcesLoader.loadModel(PLAYER_MAID_MODEL_RES);
        playerMaidModelSlim = CustomResourcesLoader.loadModel(PLAYER_MAID_SLIM_MODEL_RES);
        PLAYER_MAID_ANIMATIONS.clear();
        for (ResourceLocation res : PLAYER_MAID_ANIMATION_RES) {
            PLAYER_MAID_ANIMATIONS.add(CustomJsAnimationManger.getCustomAnimation(res));
        }
        playerMaidInfo = new MaidModelInfo() {
            @Override
            public ResourceLocation getTexture() {
                return playerSkin;
            }
        };
    }

    public static EntityModelJson getPlayerMaidModel(String name) {
        GameProfile newProfile = null;
        Minecraft minecraft = Minecraft.getMinecraft();
        try {
            newProfile = GAME_PROFILE_CACHE.get(name, () -> {
                THREAD_POOL.submit(() -> {
                    GameProfile profile = new GameProfile(null, name);
                    GameProfile profileNew = TileEntitySkull.updateGameProfile(profile);
                    minecraft.addScheduledTask(() -> GAME_PROFILE_CACHE.put(name, profileNew));
                });
                return EMPTY_GAME_PROFILE;
            });
        } catch (ExecutionException ignore) {
        }

        if (newProfile != null) {
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(newProfile);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                String skinModel = map.get(MinecraftProfileTexture.Type.SKIN).getMetadata("model");
                if (SLIM_NAME.equals(skinModel)) {
                    return playerMaidModelSlim;
                }
            } else {
                UUID uuid = EntityPlayer.getUUID(newProfile);
                String skinModel = DefaultPlayerSkin.getSkinType(uuid);
                if (SLIM_NAME.equals(skinModel)) {
                    return playerMaidModelSlim;
                }
            }
        }
        return playerMaidModel;
    }

    public static List<Object> getPlayerMaidAnimations() {
        return PLAYER_MAID_ANIMATIONS;
    }

    public static MaidModelInfo getPlayerMaidInfo(String name) {
        playerSkin = getPlayerSkin(name);
        return playerMaidInfo;
    }

    public static ResourceLocation getPlayerSkin(String name) {
        GameProfile newProfile = null;
        Minecraft minecraft = Minecraft.getMinecraft();

        try {
            newProfile = GAME_PROFILE_CACHE.get(name, () -> {
                THREAD_POOL.submit(() -> {
                    GameProfile profile = new GameProfile(null, name);
                    GameProfile profileNew = TileEntitySkull.updateGameProfile(profile);
                    minecraft.addScheduledTask(() -> GAME_PROFILE_CACHE.put(name, profileNew));
                });
                return EMPTY_GAME_PROFILE;
            });
        } catch (ExecutionException ignore) {
        }

        if (newProfile != null) {
            Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(newProfile);
            if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
                return minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            } else {
                UUID uuid = EntityPlayer.getUUID(newProfile);
                return DefaultPlayerSkin.getDefaultSkin(uuid);
            }
        }

        return TEXTURE_ALEX;
    }
}
