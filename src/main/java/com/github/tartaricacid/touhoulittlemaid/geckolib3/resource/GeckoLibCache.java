package com.github.tartaricacid.touhoulittlemaid.geckolib3.resource;

import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.TLMBinding;
import com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.molang.YSMBinding;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.core.molang.MolangParser;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.geo.render.built.GeoModel;
import com.github.tartaricacid.touhoulittlemaid.molang.runtime.binding.ObjectBinding;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class GeckoLibCache {
    private static final Map<String, ObjectBinding> EXTRA_BINDING = new HashMap<>();
    private static GeckoLibCache INSTANCE;
    public final MolangParser parser = createMolangParser();
    private final Map<ResourceLocation, AnimationFile> animations = Maps.newHashMap();
    private final Map<ResourceLocation, GeoModel> geoModels = Maps.newHashMap();

    public static GeckoLibCache getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GeckoLibCache();
            return INSTANCE;
        }
        return INSTANCE;
    }

    public Map<ResourceLocation, AnimationFile> getAnimations() {
        return animations;
    }

    public Map<ResourceLocation, GeoModel> getGeoModels() {
        return geoModels;
    }

    private static MolangParser createMolangParser() {
        EXTRA_BINDING.put("ysm", YSMBinding.INSTANCE);
        EXTRA_BINDING.put("tlm", TLMBinding.INSTANCE);
        return new MolangParser(EXTRA_BINDING);
    }
}
