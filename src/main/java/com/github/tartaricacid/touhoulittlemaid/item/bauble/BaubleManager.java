package com.github.tartaricacid.touhoulittlemaid.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.ILittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.bauble.IMaidBauble;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.Map;

public final class BaubleManager {
    private static Map<RegistryObject<Item>, IMaidBauble> BAUBLES;

    private BaubleManager() {
        BAUBLES = Maps.newHashMap();
    }

    public static void init() {
        BaubleManager manager = new BaubleManager();
        manager.bindBauble(InitItems.DROWN_PROTECT_BAUBLE, new DrownProtectBauble());
        for (ILittleMaid littleMaid : TouhouLittleMaid.EXTENSIONS) {
            littleMaid.bindMaidBauble(manager);
        }
        BAUBLES = ImmutableMap.copyOf(BAUBLES);
    }

    @Nullable
    public static IMaidBauble getBauble(RegistryObject<Item> item) {
        return BAUBLES.get(item);
    }

    @Nullable
    public static IMaidBauble getBauble(ItemStack stack) {
        Item item = stack.getItem();
        return getBauble(RegistryObject.of(item.getRegistryName(), item::getRegistryType));
    }

    public void bindBauble(RegistryObject<Item> item, IMaidBauble bauble) {
        BAUBLES.put(item, bauble);
    }

    public void bindBauble(Item item, IMaidBauble bauble) {
        BAUBLES.put(RegistryObject.of(item.getRegistryName(), item::getRegistryType), bauble);
    }
}
