package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.data.inner.AttackListData;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.InitTaskData;
import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class SetAttackListMessage {
    private final int entityId;
    private final Map<ResourceLocation, MonsterType> attackGroups;

    public SetAttackListMessage(int entityId, Map<ResourceLocation, MonsterType> attackGroups) {
        this.entityId = entityId;
        this.attackGroups = attackGroups;
    }

    public static void encode(SetAttackListMessage message, FriendlyByteBuf buf) {
        buf.writeInt(message.entityId);
        buf.writeVarInt(message.attackGroups.size());
        for (ResourceLocation id : message.attackGroups.keySet()) {
            buf.writeResourceLocation(id);
            buf.writeEnum(message.attackGroups.get(id));
        }
    }

    public static SetAttackListMessage decode(FriendlyByteBuf buf) {
        int entityId = buf.readInt();
        Map<ResourceLocation, MonsterType> attackGroupsOutput = Maps.newHashMap();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            ResourceLocation id = buf.readResourceLocation();
            MonsterType type = buf.readEnum(MonsterType.class);
            attackGroupsOutput.put(id, type);
        }
        return new SetAttackListMessage(entityId, attackGroupsOutput);
    }

    public static void handle(SetAttackListMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> writeList(message, context));
        }
        context.setPacketHandled(true);
    }

    private static void writeList(SetAttackListMessage message, NetworkEvent.Context context) {
        ServerPlayer sender = context.getSender();
        if (sender == null) {
            return;
        }
        Entity entity = sender.level.getEntity(message.entityId);
        if (entity instanceof EntityMaid maid && maid.isOwnedBy(sender)) {
            maid.setAndSyncData(InitTaskData.ATTACK_LIST, new AttackListData(message.attackGroups));
        }
    }
}
