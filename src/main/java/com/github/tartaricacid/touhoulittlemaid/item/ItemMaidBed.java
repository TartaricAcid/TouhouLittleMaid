package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.init.InitBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemMaidBed extends BlockItem {
    private static final String COLOR_TAG = "BedColor";

    public ItemMaidBed() {
        super(InitBlocks.MAID_BED.get(), (new Item.Properties()).stacksTo(1));
    }

    public static void setColor(DyeColor color, ItemStack bed) {
        bed.getOrCreateTag().putInt(COLOR_TAG, color.getId());
    }

    public static DyeColor getColor(ItemStack bed) {
        if (bed.hasTag() && bed.getTag().contains(COLOR_TAG)) {
            return DyeColor.byId(bed.getTag().getInt(COLOR_TAG));
        }
        return DyeColor.PINK;
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        return context.getLevel().setBlock(context.getClickedPos(), state, Block.UPDATE_KNOWN_SHAPE | Block.UPDATE_IMMEDIATE | Block.UPDATE_CLIENTS);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        DyeColor color = getColor(pStack);
        Component colorText = Component.translatable("color.minecraft." + color.getName());
        Component all = Component.translatable("item.color", colorText).withStyle(ChatFormatting.GRAY);
        pTooltip.add(all);
    }
}