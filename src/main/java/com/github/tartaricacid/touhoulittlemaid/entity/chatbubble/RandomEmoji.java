package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDamageEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.tartaricacid.touhoulittlemaid.util.GetJarResources;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.schedule.Activity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@EventBusSubscriber
public final class RandomEmoji {
    private static final String FILE_PATH = String.format("/assets/%s/tlm_custom_pack/random_emoji.jsonc", TouhouLittleMaid.MOD_ID);
    private static final Gson GSON = new Gson();
    private static final String CORE = "core";
    private static final String WORK = "work";
    private static final String IDLE = "idle";
    private static final String SLEEP = "sleep";
    private static final String HURT = "hurt";
    /**
     * 检测间隔，60 秒
     */
    private static final int CHECK_RATE = 60 * 20;
    private static Map<String, String[]> EMOJI_MAP = Maps.newHashMap();

    static void init() {
        try (InputStream stream = GetJarResources.readTouhouLittleMaidFile(FILE_PATH)) {
            if (stream != null) {
                EMOJI_MAP = GSON.fromJson(new InputStreamReader(stream, StandardCharsets.UTF_8), new TypeToken<Map<String, String[]>>() {
                }.getType());

                // 工作和空闲模式的，添加上 core
                String[] core = EMOJI_MAP.get(CORE);
                if (core != null) {
                    String[] work = EMOJI_MAP.get(WORK);
                    if (work != null) {
                        String[] newWork = new String[core.length + work.length];
                        System.arraycopy(core, 0, newWork, 0, core.length);
                        System.arraycopy(work, 0, newWork, core.length, work.length);
                        EMOJI_MAP.put(WORK, newWork);
                    }
                    String[] idle = EMOJI_MAP.get(IDLE);
                    if (idle != null) {
                        String[] newIdle = new String[core.length + idle.length];
                        System.arraycopy(core, 0, newIdle, 0, core.length);
                        System.arraycopy(idle, 0, newIdle, core.length, idle.length);
                        EMOJI_MAP.put(IDLE, newIdle);
                    }
                    // 移除 core 部分
                    EMOJI_MAP.remove(CORE);
                }

                EMOJI_MAP = ImmutableMap.copyOf(EMOJI_MAP);
            }
        } catch (IOException e) {
            TouhouLittleMaid.LOGGER.error("Failed to load random emoji json file: {}", FILE_PATH, e);
        }
    }

    static void tick(EntityMaid maid) {
        long offset = maid.getUUID().getLeastSignificantBits() % CHECK_RATE;
        if ((maid.tickCount + offset) % CHECK_RATE != 0) {
            return;
        }
        ChatBubbleManager bubbleManager = maid.getChatBubbleManager();
        boolean empty = bubbleManager.getChatBubbleDataCollection().isEmpty();
        if (!empty) {
            return;
        }
        Activity activity = maid.getScheduleDetail();
        if (activity == Activity.REST) {
            String randomEmoji = getRandomEmoji(EMOJI_MAP.get(SLEEP));
            MutableComponent literal = Component.literal(randomEmoji);
            bubbleManager.addChatBubble(TextChatBubbleData.type2(literal));
            return;
        }
        if (activity == Activity.IDLE || maid.getTask() == TaskManager.getIdleTask()) {
            String randomEmoji = getRandomEmoji(EMOJI_MAP.get(IDLE));
            MutableComponent literal = Component.literal(randomEmoji);
            bubbleManager.addChatBubble(TextChatBubbleData.type2(literal));
            return;
        }
        if (activity == Activity.WORK) {
            String randomEmoji = getRandomEmoji(EMOJI_MAP.get(WORK));
            MutableComponent literal = Component.literal(randomEmoji);
            bubbleManager.addChatBubble(TextChatBubbleData.type2(literal));
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addHurtChatText(MaidDamageEvent event) {
        EntityMaid maid = event.getMaid();
        ChatBubbleManager bubbleManager = maid.getChatBubbleManager();
        boolean empty = bubbleManager.getChatBubbleDataCollection().isEmpty();
        if (empty) {
            String randomEmoji = getRandomEmoji(EMOJI_MAP.get(HURT));
            MutableComponent literal = Component.literal(randomEmoji);
            bubbleManager.addChatBubble(TextChatBubbleData.type2(literal));
        }
    }

    private static String getRandomEmoji(String[] emojis) {
        if (emojis == null || emojis.length == 0) {
            return StringUtils.EMPTY;
        }
        int index = (int) (Math.random() * emojis.length);
        return emojis[index];
    }
}
