package com.github.tartaricacid.touhoulittlemaid.entity.chatbubble;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.WaitingChatBubbleData;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;

import static com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData.DEFAULT_PRIORITY;
import static com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData.TYPE_2;
import static com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid.getChatBubbleKey;

public class ChatBubbleManager {
    private final EntityMaid maid;

    public ChatBubbleManager(EntityMaid maid) {
        this.maid = maid;
    }

    public void tick() {
        // 随机添加表情
        RandomEmoji.tick(maid);
        // 每 5 tick 检查一次
        if (this.maid.tickCount % 5 != 0) {
            return;
        }
        boolean update = this.getChatBubbleDataCollection().update();
        if (update) {
            this.forceUpdateChatBubble();
        }
    }

    public ChatBubbleDataCollection getChatBubbleDataCollection() {
        return maid.getEntityData().get(getChatBubbleKey());
    }

    @Nullable
    public IChatBubbleData getChatBubble(long key) {
        return this.getChatBubbleDataCollection().get(key);
    }

    public void removeChatBubble(long key) {
        this.getChatBubbleDataCollection().remove(key);
        this.forceUpdateChatBubble();
    }

    public void forceUpdateChatBubble() {
        maid.getEntityData().set(getChatBubbleKey(), this.getChatBubbleDataCollection(), true);
    }

    /**
     * 返回存入的 key
     *
     * @param bubble 聊天气泡
     * @return 如果存入失败则返回 -1
     */
    public long addChatBubble(IChatBubbleData bubble) {
        long key = this.getChatBubbleDataCollection().add(bubble);
        this.forceUpdateChatBubble();
        return key;
    }

    public long addTextChatBubble(String langKey) {
        MutableComponent component = Component.translatable(langKey);
        return this.addChatBubble(TextChatBubbleData.type2(component));
    }

    /**
     * 只有在前一个聊天气泡超时后才会添加新的聊天气泡
     */
    public long addTextChatBubbleIfTimeout(String langKey, long previousChatBubbleId) {
        ChatBubbleDataCollection collection = this.getChatBubbleDataCollection();
        if (previousChatBubbleId < 0 || !collection.containsKey(previousChatBubbleId)) {
            return addTextChatBubble(langKey);
        }
        return previousChatBubbleId;
    }

    public long addThinkingText(String langKey) {
        MutableComponent component = Component.translatable(langKey).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC);
        ResourceLocation icon = ResourceLocation.fromNamespaceAndPath(TouhouLittleMaid.MOD_ID, "textures/entity/chat_bubble/yinyang_orb.png");
        return this.addChatBubble(WaitingChatBubbleData.create(30 * 20, TYPE_2, DEFAULT_PRIORITY, component, icon));
    }

    public void addLLMChatText(String message, long waitingChatBubbleId) {
        Component component = Component.literal(message);
        TextChatBubbleData textChatBubble = TextChatBubbleData.type2(component);
        this.getChatBubbleDataCollection().remove(waitingChatBubbleId);
        this.getChatBubbleDataCollection().add(textChatBubble);
        this.forceUpdateChatBubble();

        // 给主人发送聊天栏信息
        if (maid.getOwner() instanceof ServerPlayer player) {
            Component name = maid.getName();
            MutableComponent msg = Component.literal("<").append(name).append(">").append(CommonComponents.SPACE).append(message);
            player.sendSystemMessage(msg.withStyle(ChatFormatting.GRAY));
        }
    }
}
