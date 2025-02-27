package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.compat.ysm.event.UpdateRemoteStructEvent;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ByteBufUtils;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncYsmMaidDataMessage {
    private final int entityId;
    private final String rouletteAnim;
    private final boolean isRouletteAnimPlaying;
    private final Object2FloatOpenHashMap<String> roamingVars;

    public SyncYsmMaidDataMessage(int entityId, String rouletteAnim, boolean isRouletteAnimPlaying, Object2FloatOpenHashMap<String> roamingVars) {
        this.entityId = entityId;
        this.rouletteAnim = rouletteAnim;
        this.isRouletteAnimPlaying = isRouletteAnimPlaying;
        this.roamingVars = roamingVars;
    }

    public static void encode(SyncYsmMaidDataMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeUtf(message.rouletteAnim);
        buf.writeBoolean(message.isRouletteAnimPlaying);
        ByteBufUtils.writeObject2FloatOpenHashMap(message.roamingVars, buf);
    }

    public static SyncYsmMaidDataMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        String rouletteAnim = buf.readUtf();
        boolean isRouletteAnimPlaying = buf.readBoolean();
        Object2FloatOpenHashMap<String> roamingVars = ByteBufUtils.readObject2FloatOpenHashMap(buf);
        return new SyncYsmMaidDataMessage(entityId, rouletteAnim, isRouletteAnimPlaying, roamingVars);
    }

    public static void handle(SyncYsmMaidDataMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> onHandle(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void onHandle(SyncYsmMaidDataMessage message) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        Entity entity = level.getEntity(message.entityId);
        if (!(entity instanceof EntityMaid maid)) {
            return;
        }
        MinecraftForge.EVENT_BUS.post(new UpdateRemoteStructEvent(maid, message.roamingVars));
        if (message.isRouletteAnimPlaying) {
            maid.playRouletteAnim(message.rouletteAnim);
        } else {
            maid.stopRouletteAnim();
        }
    }
}
