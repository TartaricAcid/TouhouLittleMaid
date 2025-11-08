package com.github.tartaricacid.touhoulittlemaid.client.resource.listener;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Mth;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

public class EmojiReloadListener implements ResourceManagerReloadListener {
    private static final List<EmojiResource> EMOJI_RESOURCES = Lists.newArrayList();
    private static final String EMOJI_PATH = "textures/chat_bubble/maid_emoji";
    private static final Random RANDOM = new Random();
    private static final int MIN_SIZE = 8;
    private static final int MAX_SIZE = 256;

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        var paths = resourceManager.listResources(EMOJI_PATH, EmojiReloadListener::filterEmojiResource).keySet();
        // 遍历获知道有哪些表情资源被加载进来了，但是不需要加载（在需要时再加载）
        EMOJI_RESOURCES.clear();
        paths.forEach(res -> EMOJI_RESOURCES.add(EmojiResource.parse(res)));
    }

    public static boolean filterEmojiResource(ResourceLocation res) {
        String path = res.getPath();
        return path.endsWith(".png") || path.endsWith(".gif");
    }

    public static Optional<EmojiResource> getRandomEmojis() {
        if (EMOJI_RESOURCES.isEmpty()) {
            return Optional.empty();
        }
        int index = RANDOM.nextInt(EMOJI_RESOURCES.size());
        return Optional.ofNullable(EMOJI_RESOURCES.get(index));
    }

    public record EmojiResource(ResourceLocation location, Format format, int width, int height) {
        private static final Pattern SIZE_PATTERN = Pattern.compile("^.*?-(\\d+)x(\\d+)\\.(png|gif)$");

        public static EmojiResource parse(ResourceLocation res) {
            String path = res.getPath();
            var matcher = SIZE_PATTERN.matcher(path);
            Format format = path.endsWith(".gif") ? Format.GIF : Format.PNG;

            if (matcher.matches()) {
                int width = Integer.parseInt(matcher.group(1));
                int height = Integer.parseInt(matcher.group(2));
                width = Mth.clamp(width, MIN_SIZE, MAX_SIZE);
                height = Mth.clamp(height, MIN_SIZE, MAX_SIZE);
                return new EmojiResource(res, format, width, height);
            } else {
                // 默认大小 24x24
                return new EmojiResource(res, format, 24, 24);
            }
        }

        public boolean isGif() {
            return this.format == Format.GIF;
        }
    }

    enum Format {
        PNG,
        GIF
    }
}
