package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.data.CompoundData;
import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import com.github.tartaricacid.touhoulittlemaid.network.pack.FoxScrollPackage;
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
import java.util.*;

public class ItemFoxScroll extends Item {
    private static final String TRACK_INFO = "TrackInfo";

    public ItemFoxScroll() {
        super((new Properties()).stacksTo(1));
    }

    public static void setTrackInfo(ItemStack scroll, String dimension, BlockPos pos) {
        CompoundData compoundData = scroll.get(InitDataComponent.MAID_INFO);
        if (compoundData != null) {
            CompoundTag tag = compoundData.nbt();
            tag.putString("Dimension", dimension);
            tag.put("Position", NbtUtils.writeBlockPos(pos));
        }
    }

    @Nullable
    public static Pair<String, BlockPos> getTrackInfo(ItemStack scroll) {
        CompoundData compoundData = scroll.get(InitDataComponent.MAID_INFO);
        if (compoundData != null) {
            CompoundTag tag = compoundData.nbt();
            String dimension = tag.getString("Dimension");
            Optional<BlockPos> position = NbtUtils.readBlockPos(tag,"Position");
            if (position.isPresent()) {
                BlockPos pos = position.get();
                return Pair.of(dimension, pos);
            }
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
            Map<String, List<FoxScrollPackage.FoxScrollData>> data = Maps.newHashMap();
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
                List<FoxScrollPackage.FoxScrollData> scrollData = data.computeIfAbsent(info.getDimension(), dim -> Lists.newArrayList());
                scrollData.add(new FoxScrollPackage.FoxScrollData(info.getChunkPos(), info.getName(), info.getTimestamp()));
            });
            PacketDistributor.sendToPlayer((ServerPlayer) player,new FoxScrollPackage(data));
            return InteractionResultHolder.success(item);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Item.TooltipContext worldIn, List<Component> components, TooltipFlag flagIn) {
        Pair<String, BlockPos> info = getTrackInfo(stack);
        if (info != null) {
            components.add(Component.translatable("tooltips.touhou_little_maid.fox_scroll.dimension", info.getLeft()).withStyle(ChatFormatting.GOLD));
            components.add(Component.translatable("tooltips.touhou_little_maid.fox_scroll.position", info.getRight().toShortString()).withStyle(ChatFormatting.RED));
            components.add(Component.empty());
        }
        if (stack.getItem() == InitItems.RED_FOX_SCROLL.get()) {
            components.add(Component.translatable("tooltips.touhou_little_maid.fox_scroll.red").withStyle(ChatFormatting.GRAY));
        } else if (stack.getItem() == InitItems.WHITE_FOX_SCROLL.get()) {
            components.add(Component.translatable("tooltips.touhou_little_maid.fox_scroll.white").withStyle(ChatFormatting.GRAY));
        }
        super.appendHoverText(stack, worldIn, components, flagIn);
    }
}
