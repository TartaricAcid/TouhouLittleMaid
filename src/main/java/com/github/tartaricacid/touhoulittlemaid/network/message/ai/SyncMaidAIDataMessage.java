package com.github.tartaricacid.touhoulittlemaid.network.message.ai;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.ClientAvailableSitesSync;
import com.github.tartaricacid.touhoulittlemaid.capability.ChatTokensCapability;
import com.github.tartaricacid.touhoulittlemaid.capability.ChatTokensCapabilityProvider;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.AIChatScreen;
import com.github.tartaricacid.touhoulittlemaid.config.subconfig.AIConfig;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public record SyncMaidAIDataMessage(int entityId, CompoundTag configData, int currentTokens, int maxTokens) {
    public SyncMaidAIDataMessage(EntityMaid maid, ServerPlayer player) {
        this(maid.getId(), maid.getAiChatManager().writeToTag(new CompoundTag()),
                player.getCapability(ChatTokensCapabilityProvider.CHAT_TOKENS_CAP).map(ChatTokensCapability::getCount).orElse(0),
                AIConfig.MAX_TOKENS_PER_PLAYER.get()
        );
    }

    public static void encode(SyncMaidAIDataMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityId);
        buf.writeNbt(message.configData);
        ClientAvailableSitesSync.writeToNetwork(buf);
        buf.writeVarInt(message.currentTokens);
        buf.writeVarInt(message.maxTokens);
    }

    public static SyncMaidAIDataMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readVarInt();
        CompoundTag configData = Objects.requireNonNullElse(buf.readNbt(), new CompoundTag());
        ClientAvailableSitesSync.readFromNetwork(buf);
        int currentTokens = buf.readVarInt();
        int maxTokens = buf.readVarInt();
        return new SyncMaidAIDataMessage(entityId, configData, currentTokens, maxTokens);
    }

    public static void handle(SyncMaidAIDataMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> handle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handle(SyncMaidAIDataMessage message) {
        ClientLevel level = Minecraft.getInstance().level;
        LocalPlayer player = Minecraft.getInstance().player;
        if (level == null || player == null) {
            Minecraft.getInstance().setScreen(null);
            return;
        }
        Entity entity = level.getEntity(message.entityId);
        if (entity instanceof EntityMaid maid) {
            maid.getAiChatManager().readFromTag(message.configData);

            AIChatScreen chatScreen = new AIChatScreen(maid);
            chatScreen.updateTokens(message.currentTokens, message.maxTokens);
            Minecraft.getInstance().setScreen(chatScreen);
        } else {
            Minecraft.getInstance().setScreen(null);
        }
    }
}
