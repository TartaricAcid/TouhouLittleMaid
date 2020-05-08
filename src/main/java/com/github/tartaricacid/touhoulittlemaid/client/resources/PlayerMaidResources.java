package com.github.tartaricacid.touhoulittlemaid.client.resources;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.CustomJsAnimationManger;
import com.github.tartaricacid.touhoulittlemaid.client.model.EntityModelJson;
import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.MaidModelInfo;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
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
    private static final ResourceLocation PLAYER_MAID_ANIMATION_RES = new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/player_maid.default.js");
    private static final ResourceLocation TEXTURE_ALEX = new ResourceLocation("textures/entity/alex.png");
    private static EntityModelJson playerMaidModel;
    private static MaidModelInfo playerMaidInfo;
    private static ResourceLocation playerSkin;
    private static List<Object> playerMaidAnimations;

    public static void reloadResources() {
        playerMaidModel = CustomResourcesLoader.loadModel(PLAYER_MAID_MODEL_RES);
        playerMaidAnimations = Collections.singletonList(CustomJsAnimationManger.getCustomAnimation(PLAYER_MAID_ANIMATION_RES));
        playerMaidInfo = new MaidModelInfo() {
            @Override
            public ResourceLocation getTexture() {
                return playerSkin;
            }
        };
    }

    public static EntityModelJson getPlayerMaidModel() {
        return playerMaidModel;
    }

    public static List<Object> getPlayerMaidAnimations() {
        return playerMaidAnimations;
    }

    public static MaidModelInfo getPlayerMaidInfo(String name) {
        playerSkin = TEXTURE_ALEX;
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
                playerSkin = minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN);
            } else {
                UUID uuid = EntityPlayer.getUUID(newProfile);
                playerSkin = DefaultPlayerSkin.getDefaultSkin(uuid);
            }
        }

        return playerMaidInfo;
    }
}
