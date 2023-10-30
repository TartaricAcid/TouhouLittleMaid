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

public class ConditionalHold {
    private static final String EMPTY = "";
    private final int preSize;
    private final String idPre;
    private final String tagPre;
    private final List<ResourceLocation> idTest = Lists.newArrayList();
    private final List<ResourceLocation> tagTest = Lists.newArrayList();

    public ConditionalHold(Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            idPre = "hold_mainhand$";
            tagPre = "hold_mainhand#";
            preSize = 14;
        } else {
            idPre = "hold_offhand$";
            tagPre = "hold_offhand#";
            preSize = 13;
        }
    }

    public void addTest(String name) {
        if (name.length() <= preSize) {
            return;
        }
        String substring = name.substring(preSize);
        if (name.startsWith(idPre) && ResourceLocation.isValidResourceLocation(substring)) {
            idTest.add(new ResourceLocation(substring));
        }
        if (name.startsWith(tagPre) && ResourceLocation.isValidResourceLocation(substring)) {
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
            return idPre + registryName;
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
        }).findFirst().map(itemTagKey -> tagPre + itemTagKey).orElse(EMPTY);
    }
}