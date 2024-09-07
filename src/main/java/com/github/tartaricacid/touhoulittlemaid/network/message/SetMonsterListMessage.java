package com.github.tartaricacid.touhoulittlemaid.network.message;

import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemMonsterList;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.Map;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.getResourceLocation;

public class SetMonsterListMessage implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<SetMonsterListMessage> TYPE = new CustomPacketPayload.Type<>(getResourceLocation("set_monster_list"));
    public static final StreamCodec<ByteBuf, SetMonsterListMessage> STREAM_CODEC = StreamCodec.composite(
            ItemMonsterList.STREAM_CODEC,
            SetMonsterListMessage::getMonsterList,
            SetMonsterListMessage::new
    );

    private final Map<ResourceLocation, MonsterType> monsterList;

    public SetMonsterListMessage(Map<ResourceLocation, MonsterType> monsterList) {
        this.monsterList = monsterList;
    }

    public Map<ResourceLocation, MonsterType> getMonsterList() {
        return monsterList;
    }

    public static void handle(SetMonsterListMessage message, IPayloadContext context) {
        if (context.flow().isServerbound()) {
            context.enqueueWork(() -> {
                ItemStack item = context.player().getMainHandItem();
                if (item.is(InitItems.MONSTER_LIST.get())) {
                    ItemMonsterList.addMonster(item, message.monsterList);
                }
            });
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
