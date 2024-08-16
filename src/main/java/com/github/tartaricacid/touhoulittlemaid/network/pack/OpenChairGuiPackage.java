package com.github.tartaricacid.touhoulittlemaid.network.pack;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.model.ChairModelGui;
import com.github.tartaricacid.touhoulittlemaid.entity.item.EntityChair;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record OpenChairGuiPackage(int id) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<OpenChairGuiPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("open_chair_gui"));
    public static final StreamCodec<ByteBuf, OpenChairGuiPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            OpenChairGuiPackage::id,
            OpenChairGuiPackage::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(OpenChairGuiPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> handleOpenGui(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleOpenGui(OpenChairGuiPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(message.id);
        if (mc.player != null && mc.player.isAlive() && e instanceof EntityChair && e.isAlive()) {
            mc.setScreen(new ChairModelGui((EntityChair) e));
        }
    }
}
