package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class ConditionalSwing {
    private static final String ID_PRE = "swing$";
    private static final String TAG_PRE = "swing#";
    private static final String EMPTY = "";
    private static final int PRE_SIZE = 6;
    private final List<ResourceLocation> idTest = Lists.newArrayList();
    private final List<ResourceLocation> tagTest = Lists.newArrayList();

    public void addTest(String name) {
        if (name.length() <= PRE_SIZE) {
            return;
        }
        String substring = name.substring(PRE_SIZE);
        if (name.startsWith(ID_PRE) && ResourceLocation.isValidResourceLocation(substring)) {
            idTest.add(new ResourceLocation(name.substring(PRE_SIZE)));
        }
        if (name.startsWith(TAG_PRE) && ResourceLocation.isValidResourceLocation(substring)) {
            ResourceLocation res = new ResourceLocation(substring);
            ITag<Item> tag = ItemTags.getAllTags().getTag(res);
            if (tag == null) {
                return;
            }
            tagTest.add(res);
        }
    }

    public String doTest(EntityMaid maid, Hand hand) {
        if (maid.getItemInHand(hand).isEmpty()) {
            return EMPTY;
        }
        String result = doIdTest(maid, hand);
        if (result.isEmpty()) {
            return doTagTest(maid, hand);
        }
        return result;
    }

    private String doIdTest(EntityMaid maid, Hand hand) {
        if (idTest.isEmpty()) {
            return EMPTY;
        }
        ItemStack itemInHand = maid.getItemInHand(hand);
        ResourceLocation registryName = itemInHand.getItem().getRegistryName();
        if (registryName == null) {
            return EMPTY;
        }
        if (idTest.contains(registryName)) {
            return ID_PRE + registryName;
        }
        return EMPTY;
    }

    private String doTagTest(EntityMaid maid, Hand hand) {
        if (tagTest.isEmpty()) {
            return EMPTY;
        }
        Item itemInHand = maid.getItemInHand(hand).getItem();
        return tagTest.stream().filter(itemTagKey -> {
            ITag<Item> tag = ItemTags.getAllTags().getTag(itemTagKey);
            if (tag != null) {
                return tag.contains(itemInHand);
            }
            return false;
        }).findFirst().map(itemTagKey -> TAG_PRE + itemTagKey).orElse(EMPTY);
    }
}
