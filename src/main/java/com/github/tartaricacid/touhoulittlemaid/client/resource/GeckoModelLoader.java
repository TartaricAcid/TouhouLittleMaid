package com.github.tartaricacid.touhoulittlemaid.client.resource;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition.ConditionManager;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.builder.Animation;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.Converter;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.FormatVersion;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.pojo.RawGeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.raw.tree.RawGeometryTree;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.GeoBuilder;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.resource.GeckoLibCache;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.util.json.JsonAnimationUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ChainedJsonException;
import net.minecraft.util.GsonHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class GeckoModelLoader {
    public static final ResourceLocation DEFAULT_MAID_ANIMATION = new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/maid.animation.json");
    public static final ResourceLocation DEFAULT_CHAIR_ANIMATION = new ResourceLocation(TouhouLittleMaid.MOD_ID, "animation/chair.animation.json");
    public static AnimationFile DEFAULT_MAID_ANIMATION_FILE = new AnimationFile();
    public static AnimationFile DEFAULT_CHAIR_ANIMATION_FILE = new AnimationFile();

    public static void reload() {
        clearAllCache();
        loadDefaultAnimation();
    }

    public static void registerGeo(ResourceLocation id, InputStream inputStream) {
        Map<ResourceLocation, GeoModel> geoModels = GeckoLibCache.getInstance().getGeoModels();
        RawGeoModel rawModel = Converter.fromInputStream(inputStream);
        if (rawModel.getFormatVersion() == FormatVersion.VERSION_1_12_0) {
            RawGeometryTree rawGeometryTree = RawGeometryTree.parseHierarchy(rawModel);
            GeoModel geoModel = GeoBuilder.getGeoBuilder(id.getNamespace()).constructGeoModel(rawGeometryTree);
            geoModels.put(id, geoModel);
        }
    }

    public static void registerMaidAnimations(ResourceLocation id, AnimationFile animationFile) {
        DEFAULT_MAID_ANIMATION_FILE.animations().forEach((name, action) -> {
            if (!animationFile.animations().containsKey(name)) {
                animationFile.putAnimation(name, action);
            }
        });
        animationFile.animations().forEach((name, animation) -> ConditionManager.addTest(id, name));
        GeckoLibCache.getInstance().getAnimations().put(id, animationFile);
    }

    public static void registerChairAnimations(ResourceLocation id, AnimationFile animationFile) {
        DEFAULT_CHAIR_ANIMATION_FILE.animations().forEach((name, action) -> {
            if (!animationFile.animations().containsKey(name)) {
                animationFile.putAnimation(name, action);
            }
        });
        animationFile.animations().forEach((name, animation) -> ConditionManager.addTest(id, name));
        GeckoLibCache.getInstance().getAnimations().put(id, animationFile);
    }

    public static void mergeAnimationFile(InputStream inputStream, AnimationFile animationFile) {
        mergeAnimationFile(animationFile, getAnimationFile(inputStream));
    }

    private static AnimationFile getAnimationFile(InputStream stream) {
        AnimationFile animationFile = new AnimationFile();
        MolangParser parser = GeckoLibCache.getInstance().parser;
        JsonObject jsonObject = GsonHelper.fromJson(CustomPackLoader.GSON, new InputStreamReader(stream, StandardCharsets.UTF_8), JsonObject.class);
        for (Map.Entry<String, JsonElement> entry : JsonAnimationUtils.getAnimations(jsonObject)) {
            String animationName = entry.getKey();
            Animation animation;
            try {
                animation = JsonAnimationUtils.deserializeJsonToAnimation(JsonAnimationUtils.getAnimation(jsonObject, animationName), parser);
                animationFile.putAnimation(animationName, animation);
            } catch (ChainedJsonException e) {
                e.printStackTrace();
            }
        }
        return animationFile;
    }

    private static AnimationFile mergeAnimationFile(AnimationFile main, AnimationFile other) {
        other.animations().forEach(main::putAnimation);
        return main;
    }

    private static void clearAllCache() {
        GeckoLibCache.getInstance().getGeoModels().clear();
        GeckoLibCache.getInstance().getAnimations().clear();
    }

    private static void loadDefaultAnimation() {
        try (InputStream stream = Minecraft.getInstance().getResourceManager().open(DEFAULT_MAID_ANIMATION)) {
            DEFAULT_MAID_ANIMATION_FILE = getAnimationFile(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (InputStream stream = Minecraft.getInstance().getResourceManager().open(DEFAULT_CHAIR_ANIMATION)) {
            DEFAULT_CHAIR_ANIMATION_FILE = getAnimationFile(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
