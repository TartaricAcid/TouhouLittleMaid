package com.github.tartaricacid.touhoulittlemaid.api.event.client;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.geckolib3.file.AnimationFile;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;

import java.io.IOException;
import java.io.InputStream;

import static com.github.tartaricacid.touhoulittlemaid.client.resource.GeckoModelLoader.mergeAnimationFile;

/**
 * 在客户端加载额外的默认 Gecko 动画文件。
 */
public class DefaultGeckoAnimationEvent extends Event {
    private final AnimationFile maidAnimationFile;
    private final AnimationFile tacAnimationFile;
    private final AnimationFile chairAnimationFile;

    public DefaultGeckoAnimationEvent(AnimationFile maidAnimationFile, AnimationFile tacAnimationFile, AnimationFile chairAnimationFile) {
        this.maidAnimationFile = maidAnimationFile;
        this.tacAnimationFile = tacAnimationFile;
        this.chairAnimationFile = chairAnimationFile;
    }

    public AnimationFile getMaidAnimationFile() {
        return maidAnimationFile;
    }

    public AnimationFile getTacAnimationFile() {
        return tacAnimationFile;
    }

    public AnimationFile getChairAnimationFile() {
        return chairAnimationFile;
    }

    public void addAnimation(AnimationFile animationFile, ResourceLocation file) {
        try (InputStream stream = Minecraft.getInstance().getResourceManager().open(file)) {
            mergeAnimationFile(stream, animationFile);
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to load animation file", e);
        }
    }
}
