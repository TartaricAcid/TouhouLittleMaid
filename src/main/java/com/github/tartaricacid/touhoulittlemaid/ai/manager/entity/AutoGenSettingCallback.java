package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.response.ResponseChat;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMClient;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.LLMMessage;
import com.github.tartaricacid.touhoulittlemaid.ai.service.llm.openai.response.Message;
import com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.implement.TextChatBubbleData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData.DEFAULT_PRIORITY;
import static com.github.tartaricacid.touhoulittlemaid.entity.chatbubble.IChatBubbleData.TYPE_2;

public class AutoGenSettingCallback extends LLMCallback {
    public AutoGenSettingCallback(MaidAIChatManager chatManager, List<LLMMessage> messages) {
        super(chatManager, messages, true);
        this.needAddTools = false;
        // 添加自己的提示聊天气泡
        MutableComponent component = Component.translatable("ai.touhou_little_maid.chat.llm.role_no_setting_and_gen_setting");
        TextChatBubbleData bubbleData = TextChatBubbleData.create(30 * 20, component, TYPE_2, DEFAULT_PRIORITY);
        this.waitingChatBubbleId = this.getMaid().getChatBubbleManager().addChatBubble(bubbleData);
    }

    @Override
    public void onSuccess(ResponseChat responseChat) {
        String result = responseChat.getChatText();
        if (StringUtils.isBlank(result)) {
            onFailure(null, new Throwable("Error in Response Chat: %s".formatted(responseChat)), ErrorCode.CHAT_TEXT_IS_EMPTY);
            return;
        }
        chatManager.customSetting = result.replaceAll("\n+", "\n\n");

        // 有可能人设提示词会超过 4096 字符长度，故额外检查并适当截断
        // 留部分冗余，故意设置为 4000 而非 4096
        if (chatManager.customSetting.length() > 4000) {
            chatManager.customSetting = chatManager.customSetting.substring(0, 4000);
        }

        LivingEntity owner = maid.getOwner();
        if (owner instanceof Player player) {
            player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.chat.llm.auto_gen_setting").withStyle(ChatFormatting.GRAY));
        }
        if (maid.level instanceof ServerLevel serverLevel) {
            MinecraftServer server = serverLevel.getServer();
            server.submit(() -> {
                maid.getChatBubbleManager().removeChatBubble(waitingChatBubbleId);
                maid.getChatBubbleManager().addTextChatBubble("ai.touhou_little_maid.chat.llm.auto_gen_setting");
            });
        }
    }

    @Override
    public void onFunctionCall(Message choice, LLMClient client) {
        // 生成人设不处理函数调用，直接忽略（理论上也不会触发此回调）
    }
}
