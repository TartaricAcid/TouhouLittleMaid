package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record ToggleSideTabPackage(int entityId, int tabId, ResourceLocation taskUid) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ToggleSideTabPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("toggle_side_tab"));
    public static final StreamCodec<ByteBuf, ToggleSideTabPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ToggleSideTabPackage::entityId,
            ByteBufCodecs.VAR_INT, ToggleSideTabPackage::tabId,
            ResourceLocation.STREAM_CODEC, ToggleSideTabPackage::taskUid,
            ToggleSideTabPackage::new
    );

    public static void handle(ToggleSideTabPackage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                Player sender = context.player();
                Entity entity = sender.level.getEntity(message.entityId);
                if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
                    IMaidTask task = TaskManager.findTask(message.taskUid).orElse(TaskManager.getIdleTask());
                    if (!task.isEnable(maid)) {
                        return;
                    }
                    maid.openMaidGuiFromSideTab(sender, message.tabId);
                }
            });
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}