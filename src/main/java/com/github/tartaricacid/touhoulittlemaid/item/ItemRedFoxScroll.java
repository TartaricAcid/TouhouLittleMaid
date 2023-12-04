package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.RedFoxScrollMessage;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidInfo;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemRedFoxScroll extends Item {
    private static final String TRACK_INFO = "TrackInfo";

    public ItemRedFoxScroll() {
        super((new Properties()).stacksTo(1));
    }

    public static boolean hasTrackInfo(ItemStack scroll) {
        return scroll.hasTag() && !Objects.requireNonNull(scroll.getTag()).getCompound(TRACK_INFO).isEmpty();
    }

    public static void setTrackInfo(ItemStack scroll, String dimension, BlockPos pos) {
        CompoundTag tag = scroll.getOrCreateTagElement(TRACK_INFO);
        tag.putString("Dimension", dimension);
        tag.put("Position", NbtUtils.writeBlockPos(pos));
    }

    @Nullable
    public static Pair<String, BlockPos> getTrackInfo(ItemStack scroll) {
        if (hasTrackInfo(scroll)) {
            CompoundTag tag = Objects.requireNonNull(scroll.getTag()).getCompound(TRACK_INFO);
            String dimension = tag.getString("Dimension");
            BlockPos position = NbtUtils.readBlockPos(tag.getCompound("Position"));
            return Pair.of(dimension, position);
        }
        return null;
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

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> components, TooltipFlag pIsAdvanced) {
        Pair<String, BlockPos> info = getTrackInfo(stack);
        if (info != null) {
            components.add(Component.translatable("tooltips.touhou_little_maid.fox_scroll.dimension", info.getLeft()).withStyle(ChatFormatting.GRAY));
            components.add(Component.translatable("tooltips.touhou_little_maid.fox_scroll.position", info.getRight().toShortString()).withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, pLevel, components, pIsAdvanced);
    }
}
