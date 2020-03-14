package com.github.tartaricacid.touhoulittlemaid.client.animation;

import com.github.tartaricacid.touhoulittlemaid.client.resources.pojo.IModelInfo;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import javax.script.Bindings;
import javax.script.ScriptException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@SideOnly(Side.CLIENT)
public class CustomJsAnimationManger {
    private static final Map<ResourceLocation, Object> CUSTOM_ANIMATION_MAP = Maps.newHashMap();

    @Nullable
    public static List<Object> getCustomAnimation(IModelInfo item) {
        List<Object> animations = Lists.newArrayList();
        if (item.getAnimation() != null && item.getAnimation().size() > 0) {
            for (ResourceLocation res : item.getAnimation()) {
                Object animation = CustomJsAnimationManger.getCustomAnimation(res);
                if (animation != null) {
                    animations.add(animation);
                }
            }
            return animations;
        }
        return null;
    }

    @Nullable
    public static Object getCustomAnimation(@Nullable ResourceLocation resourceLocation) {
        if (resourceLocation == null) {
            return null;
        }
        if (CUSTOM_ANIMATION_MAP.containsKey(resourceLocation)) {
            return CUSTOM_ANIMATION_MAP.get(resourceLocation);
        }
        InputStream stream = null;
        try {
            Bindings bindings = CommonProxy.NASHORN.createBindings();
            stream = Minecraft.getMinecraft().getResourceManager().getResource(resourceLocation).getInputStream();
            Object scriptObject = CommonProxy.NASHORN.eval(IOUtils.toString(stream, StandardCharsets.UTF_8), bindings);
            CUSTOM_ANIMATION_MAP.put(resourceLocation, scriptObject);
            return scriptObject;
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(stream);
        }
        return null;
    }

    public static void loadDebugAnimation(File file, Consumer<Object> consumer) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(file);
            Bindings bindings = CommonProxy.NASHORN.createBindings();
            Object scriptObject = CommonProxy.NASHORN.eval(IOUtils.toString(stream, StandardCharsets.UTF_8), bindings);
            if (scriptObject != null) {
                consumer.accept(scriptObject);
            }
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public static void clearAll() {
        CUSTOM_ANIMATION_MAP.clear();
    }
}
