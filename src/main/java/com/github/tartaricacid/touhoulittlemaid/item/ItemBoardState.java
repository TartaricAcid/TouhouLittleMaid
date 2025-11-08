package com.github.tartaricacid.touhoulittlemaid.item;


import com.github.tartaricacid.touhoulittlemaid.init.InitDataComponent;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.BoardStateTooltip;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class ItemBoardState extends Item {
    public static final String DATA_TAG = "BoardStateData";
    public static final String DESC_TAG = "BoardStateDesc";
    public static final String AUTHOR_TAG = "BoardStateAuthor";

    public ItemBoardState() {
        super((new Properties()));
    }

    public static void setState(ItemStack stack, String data, String desc, String author) {
        BoardStateInfo info = new BoardStateInfo(data, desc, author);
        stack.set(InitDataComponent.BOARD_STATE_TAG, info);
    }

    @Nullable
    public static String[] getState(ItemStack stack) {
        BoardStateInfo info = stack.get(InitDataComponent.BOARD_STATE_TAG);
        if (info == null) {
            return null;
        }
        return new String[]{info.data(), info.description(), info.author()};
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
        if (!Screen.hasShiftDown()) {
            return Optional.empty();
        }

        String[] state = getState(stack);
        if (state == null) {
            return Optional.empty();
        }
        String stateData = state[0];
        if (StringUtils.isBlank(stateData)) {
            return Optional.empty();
        }

        if (stack.is(InitItems.GOMOKU_BOARD_STATE.get())) {
            return Optional.of(BoardStateTooltip.ofGomoku(stateData));
        } else if (stack.is(InitItems.CCHESS_BOARD_STATE.get())) {
            return Optional.of(BoardStateTooltip.ofXiangqi(stateData));
        } else if (stack.is(InitItems.WCHESS_BOARD_STATE.get())) {
            return Optional.of(BoardStateTooltip.ofChess(stateData));
        }

        return Optional.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
        String[] state = getState(stack);

        if (state == null) {
            tooltip.add(Component.translatable("tooltips.touhou_little_maid.board_state.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        String descKey = state[1];
        if (StringUtils.isNotBlank(descKey)) {
            tooltip.add(Component.translatable(descKey).withStyle(ChatFormatting.GRAY));
        }

        String author = state[2];
        if (StringUtils.isNotBlank(author)) {
            tooltip.add(Component.translatable("tooltips.touhou_little_maid.board_state.author", author).withStyle(ChatFormatting.GRAY));
        }

        if (!Screen.hasShiftDown()) {
            tooltip.add(Component.translatable("board_state.touhou_little_maid.show_picture")
                    .withStyle(ChatFormatting.DARK_GRAY).withStyle(ChatFormatting.ITALIC));
        }
    }

    public record BoardStateInfo(String data, String description, String author) {
        public static final Codec<BoardStateInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.STRING.fieldOf("data").forGetter(BoardStateInfo::data),
                Codec.STRING.fieldOf("description").forGetter(BoardStateInfo::description),
                Codec.STRING.fieldOf("author").forGetter(BoardStateInfo::author)
        ).apply(instance, BoardStateInfo::new));

        public static final StreamCodec<ByteBuf, BoardStateInfo> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.STRING_UTF8, BoardStateInfo::data,
                ByteBufCodecs.STRING_UTF8, BoardStateInfo::description,
                ByteBufCodecs.STRING_UTF8, BoardStateInfo::author,
                BoardStateInfo::new
        );
    }
}
