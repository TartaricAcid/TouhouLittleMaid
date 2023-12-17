package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.FoxScrollMessage;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidInfo;
import com.github.tartaricacid.touhoulittlemaid.world.data.MaidWorldData;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemFoxScroll extends Item {
    private static final String TRACK_INFO = "TrackInfo";

    public ItemFoxScroll() {
        super((new Properties()).tab(MAIN_TAB).stacksTo(1));
    }

    public static boolean hasTrackInfo(ItemStack scroll) {
        return scroll.hasTag() && !Objects.requireNonNull(scroll.getTag()).getCompound(TRACK_INFO).isEmpty();
    }

    public static void setTrackInfo(ItemStack scroll, String dimension, BlockPos pos) {
        CompoundNBT tag = scroll.getOrCreateTagElement(TRACK_INFO);
        tag.putString("Dimension", dimension);
        tag.put("Position", NBTUtil.writeBlockPos(pos));
    }

    @Nullable
    public static Pair<String, BlockPos> getTrackInfo(ItemStack scroll) {
        if (hasTrackInfo(scroll)) {
            CompoundNBT tag = Objects.requireNonNull(scroll.getTag()).getCompound(TRACK_INFO);
            String dimension = tag.getString("Dimension");
            BlockPos position = NBTUtil.readBlockPos(tag.getCompound("Position"));
            return Pair.of(dimension, position);
        }
        return null;
    }

    @Override
    public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
        if (!level.isClientSide && hand == Hand.MAIN_HAND) {
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
            return ActionResult.success(item);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World pLevel, List<ITextComponent> components, ITooltipFlag pIsAdvanced) {
        if (stack.getItem() == InitItems.RED_FOX_SCROLL.get()) {
            components.add(new TranslationTextComponent("tooltips.touhou_little_maid.fox_scroll.red").withStyle(TextFormatting.GRAY));
        } else if (stack.getItem() == InitItems.WHITE_FOX_SCROLL.get()) {
            components.add(new TranslationTextComponent("tooltips.touhou_little_maid.fox_scroll.white").withStyle(TextFormatting.GRAY));
        }
        Pair<String, BlockPos> info = getTrackInfo(stack);
        if (info != null) {
            components.add(new TranslationTextComponent("tooltips.touhou_little_maid.fox_scroll.dimension", info.getLeft()).withStyle(TextFormatting.GRAY));
            components.add(new TranslationTextComponent("tooltips.touhou_little_maid.fox_scroll.position", info.getRight().toShortString()).withStyle(TextFormatting.GRAY));
        }
        super.appendHoverText(stack, pLevel, components, pIsAdvanced);
    }
}
