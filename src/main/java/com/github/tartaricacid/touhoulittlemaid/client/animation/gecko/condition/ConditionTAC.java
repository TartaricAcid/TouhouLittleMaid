package com.github.tartaricacid.touhoulittlemaid.client.animation.gecko.condition;

import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.github.tartaricacid.touhoulittlemaid.util.ResourceLoactionUtil.isValidResourceLocation;

public class ConditionTAC {
    private static final String EMPTY = "";
    private final List<String> nameTest = Lists.newArrayList();
    private final List<ResourceLocation> idTest = Lists.newArrayList();

    public void addTest(String name) {
        if (!name.startsWith("tac:") || !name.contains("$")) {
            return;
        }
        String[] split = StringUtils.split(name, "$", 2);
        if (split.length < 2) {
            return;
        }
        String itemId = split[1];
        if (isValidResourceLocation(itemId)) {
            nameTest.add(name);
            idTest.add(ResourceLocation.parse(itemId));
        }
    }

    public String doTest(ItemStack itemInHand, String prefix) {
        if (itemInHand.isEmpty()) {
            return EMPTY;
        }
        ResourceLocation gunId = TacCompat.getGunId(itemInHand);
        if (gunId == null) {
            return EMPTY;
        }
        if (idTest.contains(gunId)) {
            String animationName = prefix.substring(0, prefix.length() - 1) + "$" + gunId;
            if (nameTest.contains(animationName)) {
                return animationName;
            }
        }
        return EMPTY;
    }
}
