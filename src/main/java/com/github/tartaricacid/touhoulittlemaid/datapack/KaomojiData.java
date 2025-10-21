package com.github.tartaricacid.touhoulittlemaid.datapack;

import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.ChatBubbleManager;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.schedule.Activity;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class KaomojiData {
    private static final Map<String, List<String>> KAOMOJI_MAP = Maps.newHashMap();

    private static final String CORE = "core";
    private static final String WORK = "work";
    private static final String IDLE = "idle";
    private static final String SLEEP = "sleep";
    private static final String HURT = "hurt";

    private static final Random RANDOM = new Random();

    public static void clear() {
        KAOMOJI_MAP.clear();
    }

    public static void merge(Map<String, List<String>> data) {
        // 工作和空闲模式的，添加上 core
        List<String> core = data.get(CORE);
        if (core != null) {
            if (data.containsKey(WORK)) {
                data.get(WORK).addAll(core);
            }
            if (data.containsKey(IDLE)) {
                data.get(IDLE).addAll(core);
            }
            data.remove(CORE);
        }
        data.keySet().forEach(key -> {
            var value = data.get(key);
            KAOMOJI_MAP.computeIfAbsent(key, k -> Lists.newArrayList()).addAll(value);
        });
    }

    public static void showRoutineKaomoji(EntityMaid maid, ChatBubbleManager bubbleManager) {
        Activity activity = maid.getScheduleDetail();
        if (activity == Activity.REST) {
            String randomEmoji = getRandomEmoji(KAOMOJI_MAP.get(SLEEP));
            MutableComponent literal = Component.literal(randomEmoji);
            bubbleManager.addChatBubble(TextChatBubbleData.type2(literal));
            return;
        }
        if (activity == Activity.IDLE || maid.getTask() == TaskManager.getIdleTask()) {
            String randomEmoji = getRandomEmoji(KAOMOJI_MAP.get(IDLE));
            MutableComponent literal = Component.literal(randomEmoji);
            bubbleManager.addChatBubble(TextChatBubbleData.type2(literal));
            return;
        }
        if (activity == Activity.WORK) {
            String randomEmoji = getRandomEmoji(KAOMOJI_MAP.get(WORK));
            MutableComponent literal = Component.literal(randomEmoji);
            bubbleManager.addChatBubble(TextChatBubbleData.type2(literal));
        }
    }

    public static void showHurtKaomoji(EntityMaid maid, ChatBubbleManager bubbleManager) {
        String randomEmoji = getRandomEmoji(KAOMOJI_MAP.get(HURT));
        MutableComponent literal = Component.literal(randomEmoji);
        bubbleManager.addChatBubble(TextChatBubbleData.type2(literal));
    }

    private static String getRandomEmoji(List<String> emojis) {
        if (emojis == null || emojis.isEmpty()) {
            return StringUtils.EMPTY;
        }
        int index = RANDOM.nextInt(emojis.size());
        return emojis.get(index);
    }
}
