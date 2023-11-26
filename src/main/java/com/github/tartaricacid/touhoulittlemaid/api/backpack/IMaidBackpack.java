package com.github.tartaricacid.touhoulittlemaid.api.backpack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public interface IMaidBackpack {
    ResourceLocation getId();

    Item getItem();
}
