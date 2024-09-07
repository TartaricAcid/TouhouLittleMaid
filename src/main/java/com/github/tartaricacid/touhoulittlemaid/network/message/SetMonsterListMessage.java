package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMonsterList;
import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Map;
import java.util.function.Supplier;

public class SetMonsterListMessage {
    private final Map<ResourceLocation, MonsterType> monsterList;

    public SetMonsterListMessage(Map<ResourceLocation, MonsterType> monsterList) {
        this.monsterList = monsterList;
    }

    public static void encode(SetMonsterListMessage message, FriendlyByteBuf buf) {
        buf.writeVarInt(message.monsterList.size());
        for (ResourceLocation id : message.monsterList.keySet()) {
            buf.writeResourceLocation(id);
            buf.writeEnum(message.monsterList.get(id));
        }
    }

    public static SetMonsterListMessage decode(FriendlyByteBuf buf) {
        Map<ResourceLocation, MonsterType> monsterListOutput = Maps.newHashMap();
        int size = buf.readVarInt();
        for (int i = 0; i < size; i++) {
            ResourceLocation id = buf.readResourceLocation();
            MonsterType type = buf.readEnum(MonsterType.class);
            monsterListOutput.put(id, type);
        }
        return new SetMonsterListMessage(monsterListOutput);
    }

    public static void handle(SetMonsterListMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer()) {
            context.enqueueWork(() -> writeList(message, context));
        }
        context.setPacketHandled(true);
    }

    private static void writeList(SetMonsterListMessage message, NetworkEvent.Context context) {
        ServerPlayer sender = context.getSender();
        if (sender == null) {
            return;
        }
        ItemStack item = sender.getMainHandItem();
        if (item.is(InitItems.MONSTER_LIST.get())) {
            ItemMonsterList.addMonster(item, message.monsterList);
        }
    }
}
