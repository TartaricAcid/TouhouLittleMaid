package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.BaubleItemHandler;
import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;

import java.util.Map;

public final class BaubleManager {
    private static final Map<RegistryObject<Item>, IMaidBauble> BAUBLES = Maps.newHashMap();

    public static IMaidBauble registerBauble(RegistryObject<Item> item, IMaidBauble bauble) {
        return BAUBLES.put(item, bauble);
    }

    public static IMaidBauble getBauble(RegistryObject<Item> item) {
        return BAUBLES.get(item);
    }

    public static IMaidBauble getBauble(ItemStack stack) {
        Item item = stack.getItem();
        return getBauble(RegistryObject.of(item.getRegistryName(), item::getRegistryType));
    }

    public static int getBaubleSlotInMaid(EntityMaid maid, IMaidBauble bauble) {
        BaubleItemHandler handler = maid.getMaidBauble();
        if (handler != null) {
            for (int i = 0; i < handler.getSlots(); i++) {
                IMaidBauble baubleIn = handler.getBaubleInSlot(i);
                if (baubleIn == bauble) {
                    return i;
                }
            }
        }
        return -1;
    }
}
