package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.client.gui.item.MonsterListScreen;
import com.github.tartaricacid.touhoulittlemaid.entity.misc.MonsterType;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;

public class ItemMonsterList extends Item {
    public static ListTag getMonsterList(ItemStack stack, MonsterType type) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains(type.getTagName(), Tag.TAG_LIST)) {
            return tag.getList(type.getTagName(), Tag.TAG_STRING);
        }
        return new ListTag();
    }

    public static void addMonster(ItemStack stack, MonsterType type, ResourceLocation entityId) {
        CompoundTag tag = stack.getOrCreateTag();
        ListTag tagList;
        if (tag.contains(type.getTagName(), Tag.TAG_LIST)) {
            tagList = tag.getList(type.getTagName(), Tag.TAG_STRING);
        } else {
            tagList = new ListTag();
            tag.put(type.getTagName(), tagList);
        }
        StringTag tagId = StringTag.valueOf(entityId.toString());
        tagList.add(tagId);
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
}