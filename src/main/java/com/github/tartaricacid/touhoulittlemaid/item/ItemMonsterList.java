package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.MonsterListScreen;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class ItemMonsterList extends Item {
    private static final String TAG_NAME = "MonsterList";

    public static CompoundTag getMonsterList(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(TAG_NAME, Tag.TAG_COMPOUND)) {
            return tag.getCompound(TAG_NAME);
        }
        return new CompoundTag();
    }

    public static void addMonster(ItemStack stack, Map<ResourceLocation, MonsterType> monsterList) {
        CompoundTag tag = stack.getOrCreateTagElement(TAG_NAME);
        monsterList.forEach((key, value) -> tag.putInt(key.toString(), value.ordinal()));
    }

    public ItemMonsterList() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (handIn == InteractionHand.MAIN_HAND) {
            ItemStack stack = playerIn.getMainHandItem();
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> openMonsterListSetScreen(stack, playerIn));
            return InteractionResultHolder.success(stack);
        }
        return super.use(worldIn, playerIn, handIn);
    }

    @OnlyIn(Dist.CLIENT)
    private void openMonsterListSetScreen(ItemStack stack, Player playerIn) {
        if (playerIn.level.isClientSide) {
            Minecraft.getInstance().setScreen(new MonsterListScreen(stack));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("tooltips.touhou_little_maid.monster_list.desc").withStyle(ChatFormatting.GRAY));
    }
}