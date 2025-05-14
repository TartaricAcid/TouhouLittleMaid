package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendUserChatMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageInfo;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

import java.net.http.HttpRequest;

@OnlyIn(Dist.CLIENT)
public class STTCallback implements ResponseCallback<String> {
    private final Player player;
    private final EntityMaid maid;

    public STTCallback(Player player, EntityMaid maid) {
        this.player = player;
        this.maid = maid;
    }

    @Override
    public void onFailure(HttpRequest request, Throwable throwable) {
        String cause = throwable.getLocalizedMessage();
        player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.stt.connect.fail")
                .append(cause).withStyle(ChatFormatting.RED));
    }

    @Override
    public void onSuccess(String chatText) {
        if (StringUtils.isNotBlank(chatText)) {
            LanguageManager languageManager = Minecraft.getInstance().getLanguageManager();
            LanguageInfo info = languageManager.getLanguage(languageManager.getSelected());
            String language;
            if (info != null) {
                language = info.toComponent().getString();
            } else {
                language = "English (US)";
            }
            NetworkHandler.CHANNEL.sendToServer(new SendUserChatMessage(maid.getId(), chatText, language));
            String name = player.getScoreboardName();
            String format = String.format("<%s> %s", name, chatText);
            player.sendSystemMessage(Component.literal(format).withStyle(ChatFormatting.GRAY));
        } else {
            player.sendSystemMessage(Component.translatable("ai.touhou_little_maid.stt.content.empty").withStyle(ChatFormatting.GRAY));
        }
    }
}
