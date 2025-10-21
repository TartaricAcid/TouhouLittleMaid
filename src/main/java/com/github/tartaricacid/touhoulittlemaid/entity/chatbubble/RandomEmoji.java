package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.api.event.MaidDamageEvent;
import com.github.tartaricacid.touhoulittlemaid.datapack.KaomojiData;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.EmojiChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public final class RandomEmoji {
    /**
     * 检测间隔，60 秒
     */
    private static final int CHECK_RATE = 60 * 20;

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
        // 有 1/2 概率显示图片表情，1/2 概率显示文字表情
        if (maid.getRandom().nextBoolean()) {
            bubbleManager.addChatBubble(EmojiChatBubbleData.create());
        } else {
            KaomojiData.showRoutineKaomoji(maid, bubbleManager);
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void addHurtChatText(MaidDamageEvent event) {
        EntityMaid maid = event.getMaid();
        ChatBubbleManager bubbleManager = maid.getChatBubbleManager();
        boolean empty = bubbleManager.getChatBubbleDataCollection().isEmpty();
        if (empty) {
            KaomojiData.showHurtKaomoji(maid, bubbleManager);
        }
    }
}
