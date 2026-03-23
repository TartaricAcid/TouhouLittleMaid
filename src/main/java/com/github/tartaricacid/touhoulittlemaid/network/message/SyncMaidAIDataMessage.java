package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.ai.manager.site.ClientAvailableSitesSync;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.ai.AIChatScreen;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;
import java.util.function.Supplier;

public record SyncMaidAIDataMessage(int entityId, CompoundTag configData) {
    public SyncMaidAIDataMessage(EntityMaid maid) {
        this(maid.getId(), maid.getAiChatManager().writeToTag(new CompoundTag()));
    }

    public static void encode(SyncMaidAIDataMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.entityId);
        buf.writeNbt(message.configData);
        ClientAvailableSitesSync.writeToNetwork(buf);
    }

    public static SyncMaidAIDataMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readVarInt();
        CompoundTag configData = Objects.requireNonNullElse(buf.readNbt(), new CompoundTag());
        ClientAvailableSitesSync.readFromNetwork(buf);
        return new SyncMaidAIDataMessage(entityId, configData);
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
            return;
        }
        Entity entity = level.getEntity(message.entityId);
        if (entity instanceof EntityMaid maid && stillValid(player, maid)) {
            maid.getAiChatManager().readFromTag(message.configData);
            Minecraft.getInstance().setScreen(new AIChatScreen(maid));
        }
    }

    private static boolean stillValid(Player playerIn, EntityMaid maid) {
        return maid.isOwnedBy(playerIn) && !maid.isSleeping() && maid.isAlive() && maid.distanceTo(playerIn) < 5.0F;
    }
}
