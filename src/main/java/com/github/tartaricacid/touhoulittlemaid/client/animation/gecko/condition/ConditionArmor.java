package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConditionArmor {
    private static final Pattern ID_PRE_REG = Pattern.compile("^(.+?)\\$(.*?)$");
    private static final Pattern TAG_PRE_REG = Pattern.compile("^(.+?)#(.*?)$");
    private static final String EMPTY = "";

    private final Map<EquipmentSlotType, List<ResourceLocation>> idTest = Maps.newHashMap();
    private final Map<EquipmentSlotType, List<ResourceLocation>> tagTest = Maps.newHashMap();

    public void addTest(String name) {
        Matcher matcherId = ID_PRE_REG.matcher(name);
        if (matcherId.find()) {
            EquipmentSlotType type = getType(matcherId.group(1));
            if (type == null) {
                return;
            }
            String id = matcherId.group(2);
            if (!ResourceLocation.isValidResourceLocation(id)) {
                return;
            }
            ResourceLocation res = new ResourceLocation(id);
            if (idTest.containsKey(type)) {
                idTest.get(type).add(res);
            } else {
                idTest.put(type, Lists.newArrayList(res));
            }
            return;
        }

        Matcher matcherTag = TAG_PRE_REG.matcher(name);
        if (matcherTag.find()) {
            EquipmentSlotType type = getType(matcherTag.group(1));
            if (type == null) {
                return;
            }
            String id = matcherTag.group(2);
            if (!ResourceLocation.isValidResourceLocation(id)) {
                return;
            }
            ResourceLocation res = new ResourceLocation(id);
            ITag<Item> tag = ItemTags.getAllTags().getTag(res);
            if (tag == null) {
                return;
            }
            if (tagTest.containsKey(type)) {
                tagTest.get(type).add(res);
            } else {
                tagTest.put(type, Lists.newArrayList(res));
            }
        }
    }

    public String doTest(EntityMaid maid, EquipmentSlotType slot) {
        ItemStack item = maid.getItemBySlot(slot);
        if (item.isEmpty()) {
            return EMPTY;
        }
        String result = doIdTest(maid, slot);
        if (result.isEmpty()) {
            return doTagTest(maid, slot);
        }
        return result;
    }

    private String doIdTest(EntityMaid maid, EquipmentSlotType slot) {
        if (idTest.isEmpty()) {
            return EMPTY;
        }
        if (!idTest.containsKey(slot) || idTest.get(slot).isEmpty()) {
            return EMPTY;
        }
        List<ResourceLocation> idListTest = idTest.get(slot);
        ItemStack item = maid.getItemBySlot(slot);
        ResourceLocation registryName = item.getItem().getRegistryName();
        if (registryName == null) {
            return EMPTY;
        }
        if (idListTest.contains(registryName)) {
            return slot.getName() + "$" + registryName;
        }
        return EMPTY;
    }

    private String doTagTest(EntityMaid maid, EquipmentSlotType slot) {
        if (tagTest.isEmpty()) {
            return EMPTY;
        }
        if (!tagTest.containsKey(slot) || tagTest.get(slot).isEmpty()) {
            return EMPTY;
        }
        List<ResourceLocation> tagListTest = tagTest.get(slot);
        Item item = maid.getItemBySlot(slot).getItem();
        return tagListTest.stream().filter(itemTagKey -> {
            ITag<Item> tag = ItemTags.getAllTags().getTag(itemTagKey);
            if (tag != null) {
                return tag.contains(item);
            }
            return false;
        }).findFirst().map(itemTagKey -> slot.getName() + "#" + itemTagKey).orElse(EMPTY);
    }


    @Nullable
    public static EquipmentSlotType getType(String type) {
        for (EquipmentSlotType slotType : EquipmentSlotType.values()) {
            if (slotType.getName().equals(type)) {
                return slotType;
            }
        }
        return null;
    }
}