package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.network.message.FoxScrollPackage;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidInfo;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemFoxScroll extends Item {
    public ItemFoxScroll() {
        super((new Properties()).stacksTo(1));
    }

    public static void setTrackInfo(ItemStack scroll, String dimension, BlockPos pos) {
        scroll.set(InitDataComponent.TRACK_INFO, new TrackInfo(dimension, pos));
    }

    @Nullable
    public static Pair<String, BlockPos> getTrackInfo(ItemStack scroll) {
        TrackInfo trackInfo = scroll.get(InitDataComponent.TRACK_INFO);
        if (trackInfo != null) {
            return Pair.of(trackInfo.dimension(), trackInfo.position());
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

    public record TrackInfo(String dimension, BlockPos position) {
        public static final Codec<TrackInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("dimension").forGetter(TrackInfo::dimension),
                BlockPos.CODEC.fieldOf("position").forGetter(TrackInfo::position)
        ).apply(instance, TrackInfo::new));

        public static final StreamCodec<ByteBuf, TrackInfo> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, TrackInfo::dimension,
                BlockPos.STREAM_CODEC, TrackInfo::position,
                TrackInfo::new
        );
    }
}
