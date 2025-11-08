package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDamageEvent;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.MaidConfig;
import com.github.tartaricacid.touhoulittlemaid.datapack.KaomojiData;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.EmojiChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.apache.commons.lang3.StringUtils;

@EventBusSubscriber
public final class RandomEmoji {
    static void tick(EntityMaid maid) {
        if (!MaidConfig.ENABLE_EMOJI.get()) {
            return;
        }
        int checkRate = MaidConfig.EMOJI_CHECK_RATE.get();
        long offset = maid.getUUID().getLeastSignificantBits() % checkRate;
        if ((maid.tickCount + offset) % checkRate != 0) {
            return;
        }
        ChatBubbleManager bubbleManager = maid.getChatBubbleManager();
        boolean empty = bubbleManager.getChatBubbleDataCollection().isEmpty();
        if (!empty) {
            return;
        }
        // 依据权重随机选择表情包类型
        int imageWeight = MaidConfig.IMAGE_EMOJI_WEIGHT.get();
        int kaomojiWeight = MaidConfig.KAOMOJI_EMOJI_WEIGHT.get();
        int totalWeight = imageWeight + kaomojiWeight;
        int randomWeight = maid.getRandom().nextInt(totalWeight);
        if (randomWeight < imageWeight) {
            bubbleManager.addChatBubble(EmojiChatBubbleData.create());
        } else {
            KaomojiData.showRoutineKaomoji(maid, bubbleManager);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addHurtChatText(MaidDamageEvent event) {
        if (!MaidConfig.ENABLE_EMOJI.get()) {
            return;
        }
        EntityMaid maid = event.getMaid();
        ChatBubbleManager bubbleManager = maid.getChatBubbleManager();
        boolean empty = bubbleManager.getChatBubbleDataCollection().isEmpty();
        if (empty) {
            KaomojiData.showHurtKaomoji(maid, bubbleManager);
        }
    }
}
