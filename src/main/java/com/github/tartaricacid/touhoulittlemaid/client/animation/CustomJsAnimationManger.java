package com.github.tartaricacid.touhoulittlemaid.client.animation;

import com.github.tartaricacid.touhoulittlemaid.client.animation.inner.InnerAnimation;
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
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SideOnly(Side.CLIENT)
public class CustomJsAnimationManger {
    private static final Map<ResourceLocation, Object> CUSTOM_ANIMATION_MAP = Maps.newHashMap();

    @Nullable
    public static List<Object> getCustomAnimation(ZipFile zipFile, IModelInfo item) {
        List<Object> animations = Lists.newArrayList();
        if (item.getAnimation() != null && item.getAnimation().size() > 0) {
            for (ResourceLocation res : item.getAnimation()) {
                Object animation = CustomJsAnimationManger.getCustomAnimation(zipFile, res);
                if (animation != null) {
                    animations.add(animation);
                }
            }
            return animations;
        }
        return null;
    }

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
    public static Object getCustomAnimation(ZipFile zipFile, @Nullable ResourceLocation resourceLocation) {
        if (resourceLocation == null) {
            return null;
        }
        if (CUSTOM_ANIMATION_MAP.containsKey(resourceLocation)) {
            return CUSTOM_ANIMATION_MAP.get(resourceLocation);
        }
        if (InnerAnimation.getInnerAnimation().containsKey(resourceLocation)) {
            return InnerAnimation.getInnerAnimation().get(resourceLocation);
        }
        ZipEntry entry = zipFile.getEntry(String.format("assets/%s/%s", resourceLocation.getNamespace(), resourceLocation.getPath()));
        if (entry == null) {
            return null;
        }
        try (InputStream stream = zipFile.getInputStream(entry)) {
            Bindings bindings = CommonProxy.NASHORN.createBindings();
            Object scriptObject = CommonProxy.NASHORN.eval(IOUtils.toString(stream, StandardCharsets.UTF_8), bindings);
            CUSTOM_ANIMATION_MAP.put(resourceLocation, scriptObject);
            return scriptObject;
        } catch (IOException | ScriptException e) {
            e.printStackTrace();
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
        if (InnerAnimation.getInnerAnimation().containsKey(resourceLocation)) {
            return InnerAnimation.getInnerAnimation().get(resourceLocation);
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

    public static void clearAll() {
        CUSTOM_ANIMATION_MAP.clear();
    }
}
