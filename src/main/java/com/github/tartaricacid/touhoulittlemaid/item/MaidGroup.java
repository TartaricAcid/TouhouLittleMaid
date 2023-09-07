package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import static com.github.tartaricacid.touhoulittlemaid.init.InitItems.*;

public class MaidGroup extends CreativeModeTab {
    public static CreativeModeTab MAIN_TAB = new MaidGroup("main", HAKUREI_GOHEI);
    public static CreativeModeTab GARAGE_KIT_TAB = new MaidGroup("garage_kit", GARAGE_KIT);
    public static CreativeModeTab CHAIR_TAB = new MaidGroup("chair", CHAIR);

    private final Component displayName;
    private final RegistryObject<Item> iconItem;
    private ItemStack icon = null;

    public MaidGroup(String label, RegistryObject<Item> iconItem) {
        super(String.format("touhou_little_maid.%s", label));
        this.displayName = Component.translatable(String.format("item_group.%s.%s", TouhouLittleMaid.MOD_ID, label));
        this.iconItem = iconItem;
    }

    @Override
    public ItemStack makeIcon() {
        if (icon == null) {
            icon = iconItem.get().getDefaultInstance();
        }
        return icon;
    }

    @Override
    public Component getDisplayName() {
        return displayName;
    }
}
