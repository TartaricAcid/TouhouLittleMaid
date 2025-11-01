package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.api.game.gomoku.GomokuCodec;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.init.InitSounds;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityCChess;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityGomoku;
import com.github.tartaricacid.touhoulittlemaid.tileentity.TileEntityWChess;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBoardState extends Item {
    public static final String DATA_TAG = "BoardStateData";
    public static final String DESC_TAG = "BoardStateDesc";
    public static final String AUTHOR_TAG = "BoardStateAuthor";

    public ItemBoardState() {
        super((new Properties()).stacksTo(1));
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
    public InteractionResult useOn(UseOnContext context) {
        BlockPos clickedPos = context.getClickedPos();
        Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }
        ItemStack stack = context.getItemInHand();
        String[] state = getState(stack);
        if (state == null) {
            return InteractionResult.PASS;
        }
        String data = state[0];
        if (StringUtils.isEmpty(data)) {
            return InteractionResult.PASS;
        }
        BlockEntity be = level.getBlockEntity(clickedPos);

        if (stack.is(InitItems.GOMOKU_BOARD_STATE.get()) && be instanceof TileEntityGomoku gomoku) {
            gomoku.setStateData(GomokuCodec.decode(data));
            level.playSound(null, clickedPos, InitSounds.GOMOKU_RESET.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }

        if (stack.is(InitItems.CCHESS_BOARD_STATE.get()) && be instanceof TileEntityCChess chess) {
            chess.setEndgame(data);
            level.playSound(null, clickedPos, InitSounds.GOMOKU_RESET.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }

        if (stack.is(InitItems.WCHESS_BOARD_STATE.get()) && be instanceof TileEntityWChess chess) {
            chess.setEndgame(data);
            level.playSound(null, clickedPos, InitSounds.GOMOKU_RESET.get(), SoundSource.BLOCKS, 1.0f, 1.0f);
            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
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
    }
}
