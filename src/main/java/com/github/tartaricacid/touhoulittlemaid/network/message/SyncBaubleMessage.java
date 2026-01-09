package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectRBTreeMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectSortedMap;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncBaubleMessage {
    /**
     * 全量同步还是增量同步
     */
    private final boolean isFull;
    private final int entityId;
    private final Int2ObjectSortedMap<ItemStack> baubles;

    public static SyncBaubleMessage fullSync(int entityId, Int2ObjectSortedMap<ItemStack> baubles) {
        return new SyncBaubleMessage(true, entityId, baubles);
    }

    public static SyncBaubleMessage partialSync(int entityId, int slot, ItemStack stack) {
        Int2ObjectSortedMap<ItemStack> baubles = new Int2ObjectRBTreeMap<>();
        baubles.put(slot, stack);
        return new SyncBaubleMessage(false, entityId, baubles);
    }

    public static SyncBaubleMessage partialDel(int entityId, int slot) {
        Int2ObjectSortedMap<ItemStack> baubles = new Int2ObjectRBTreeMap<>();
        baubles.put(slot, ItemStack.EMPTY);
        return new SyncBaubleMessage(false, entityId, baubles);
    }

    private SyncBaubleMessage(boolean isFull, int entityId, Int2ObjectSortedMap<ItemStack> baubles) {
        this.isFull = isFull;
        this.entityId = entityId;
        this.baubles = baubles;
    }

    public static void encode(SyncBaubleMessage message, FriendlyByteBuf buf) {
        buf.writeBoolean(message.isFull);
        buf.writeVarInt(message.entityId);
        buf.writeVarInt(message.baubles.size());
        message.baubles.forEach((slot, stack) -> {
            buf.writeVarInt(slot);
            buf.writeItem(stack);
        });
    }

    public static SyncBaubleMessage decode(FriendlyByteBuf buf) {
        boolean action = buf.readBoolean();
        int entityId = buf.readVarInt();
        Int2ObjectSortedMap<ItemStack> baubles = new Int2ObjectRBTreeMap<>();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            int slot = buf.readVarInt();
            ItemStack stack = buf.readItem();
            baubles.put(slot, stack);
        }
        return new SyncBaubleMessage(action, entityId, baubles);
    }

    public static void handle(SyncBaubleMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isClient()) {
            context.enqueueWork(() -> handleClient(message));
        }
        context.setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(SyncBaubleMessage message) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return;
        }
        Entity entity = mc.level.getEntity(message.entityId);
        if (entity instanceof EntityMaid maid) {
            BaubleItemHandler maidBauble = maid.getMaidBauble();
            // 全量同步前需要清空
            if (message.isFull) {
                maidBauble.clearAll();
            }
            message.baubles.forEach(maidBauble::setStackInSlot);
        }
    }
}
