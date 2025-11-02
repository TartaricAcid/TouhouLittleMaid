package com.github.tartaricacid.touhoulittlemaid.item;


import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.tooltip.BoardStateTooltip;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
        CompoundTag tag = stack.getOrCreateTag();

        tag.putString(DATA_TAG, data);
        tag.putString(DESC_TAG, desc);
        tag.putString(AUTHOR_TAG, author);
    }

    @Nullable
    public static String[] getState(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null) {
            return null;
        }
        if (!tag.contains(DATA_TAG, CompoundTag.TAG_STRING)) {
            return null;
        }
        return new String[]{
                tag.getString(DATA_TAG),
                tag.getString(DESC_TAG),
                tag.getString(AUTHOR_TAG)
        };
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
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
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
}
