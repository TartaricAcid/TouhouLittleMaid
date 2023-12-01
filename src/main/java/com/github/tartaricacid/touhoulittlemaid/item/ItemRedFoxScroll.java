package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.RedFoxScrollMessage;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidInfo;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Map;

public class ItemRedFoxScroll extends Item {
    public ItemRedFoxScroll() {
        super((new Properties()).stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && hand == InteractionHand.MAIN_HAND) {
            MaidWorldData maidWorldData = MaidWorldData.get(level);
            if (maidWorldData == null) {
                return super.use(level, player, hand);
            }
            Map<String, List<RedFoxScrollMessage.RedFoxScrollData>> data = Maps.newHashMap();
            List<MaidInfo> playerMaidInfos = maidWorldData.getPlayerMaidInfos(player);
            if (playerMaidInfos == null) {
                return super.use(level, player, hand);
            }
            playerMaidInfos.forEach(info -> {
                List<RedFoxScrollMessage.RedFoxScrollData> scrollData = data.computeIfAbsent(info.getDimension(), dim -> Lists.newArrayList());
                scrollData.add(new RedFoxScrollMessage.RedFoxScrollData(info.getChunkPos(), info.getName(), info.getTimestamp()));
            });
            NetworkHandler.sendToClientPlayer(new RedFoxScrollMessage(data), player);
            return InteractionResultHolder.success(player.getMainHandItem());
        }
        return super.use(level, player, hand);
    }
}
