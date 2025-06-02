package com.github.tartaricacid.touhoulittlemaid.ai.manager.entity;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ErrorCode;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ResponseCallback;
import com.github.tartaricacid.touhoulittlemaid.ai.service.ServiceType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.message.SendUserChatPackage;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
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
    public void onFailure(HttpRequest request, Throwable throwable, int errorCode) {
        String cause = throwable.getLocalizedMessage();
        MutableComponent errorMessage = ErrorCode.getErrorMessage(ServiceType.STT, errorCode, cause);
        player.sendSystemMessage(errorMessage.withStyle(ChatFormatting.RED));
        TouhouLittleMaid.LOGGER.error("STT request failed: {}, error is {}", request, throwable.getMessage());
    }

    @Override
    public void onSuccess(String chatText) {
        if (StringUtils.isNotBlank(chatText)) {
            ChatClientInfo clientInfo = ChatClientInfo.fromMaid(this.maid);
            PacketDistributor.sendToServer(new SendUserChatPackage(maid.getId(), chatText, clientInfo));
            String name = player.getScoreboardName();
            String format = String.format("<%s> %s", name, chatText);
            player.sendSystemMessage(Component.literal(format).withStyle(ChatFormatting.GRAY));
        } else {
            MutableComponent component = Component.translatable("ai.touhou_little_maid.chat.stt.content_is_empty");
            player.sendSystemMessage(component.withStyle(ChatFormatting.GRAY));
        }
    }
}
