package com.github.tartaricacid.touhoulittlemaid.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public record ItemBreakPackage(int id, ItemStack item) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ItemBreakPackage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("item_break"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ItemBreakPackage> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            ItemBreakPackage::id,
            ItemStack.STREAM_CODEC,
            ItemBreakPackage::item,
            ItemBreakPackage::new
    );

    public static void handle(ItemBreakPackage message, IPayloadContext context) {
        if (context.flow().isClientbound()) {
            context.enqueueWork(() -> handleBreakItem(message));
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleBreakItem(ItemBreakPackage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity e = mc.level.getEntity(message.id);
        if (e instanceof LivingEntity livingEntity && livingEntity.isAlive()) {
            livingEntity.breakItem(message.item);
        }
    }

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
