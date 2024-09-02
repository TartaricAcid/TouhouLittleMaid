package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.FoxScrollMessage;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidInfo;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemFoxScroll extends Item {
    private static final String TRACK_INFO = "TrackInfo";

    public ItemFoxScroll() {
        super((new Properties()).stacksTo(1).tab(MAIN_TAB));
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
            ItemStack item = player.getMainHandItem();
            MaidWorldData maidWorldData = MaidWorldData.get(level);
            if (maidWorldData == null) {
                return super.use(level, player, hand);
            }
            Map<String, List<FoxScrollMessage.FoxScrollData>> data = Maps.newHashMap();
            List<MaidInfo> maidInfos = null;
            if (item.getItem() == InitItems.RED_FOX_SCROLL.get()) {
                maidInfos = maidWorldData.getPlayerMaidInfos(player);
            } else if (item.getItem() == InitItems.WHITE_FOX_SCROLL.get()) {
                maidInfos = maidWorldData.getPlayerMaidTombstones(player);
            }
            if (maidInfos == null) {
                maidInfos = Collections.emptyList();
            }
            maidInfos.forEach(info -> {
                List<FoxScrollMessage.FoxScrollData> scrollData = data.computeIfAbsent(info.getDimension(), dim -> Lists.newArrayList());
                scrollData.add(new FoxScrollMessage.FoxScrollData(info.getChunkPos(), info.getName(), info.getTimestamp()));
            });
            NetworkHandler.sendToClientPlayer(new FoxScrollMessage(data), player);
            return InteractionResultHolder.success(item);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> components, TooltipFlag pIsAdvanced) {
        Pair<String, BlockPos> info = getTrackInfo(stack);
        if (info != null) {
            components.add(new TranslatableComponent("tooltips.touhou_little_maid.fox_scroll.dimension", info.getFirst()).withStyle(ChatFormatting.GRAY));
            components.add(new TranslatableComponent("tooltips.touhou_little_maid.fox_scroll.position", info.getSecond().toShortString()).withStyle(ChatFormatting.GRAY));
        }
        if (stack.getItem() == InitItems.RED_FOX_SCROLL.get()) {
            components.add(new TranslatableComponent("tooltips.touhou_little_maid.fox_scroll.red").withStyle(ChatFormatting.GRAY));
        } else if (stack.getItem() == InitItems.WHITE_FOX_SCROLL.get()) {
            components.add(new TranslatableComponent("tooltips.touhou_little_maid.fox_scroll.white").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, pLevel, components, pIsAdvanced);
    }
}
