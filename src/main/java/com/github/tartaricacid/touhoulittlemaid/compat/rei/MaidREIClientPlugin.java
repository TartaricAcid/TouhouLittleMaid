package com.github.tartaricacid.touhoulittlemaid.compat.rei;

import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.forge.REIPluginClient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@REIPluginClient
public class MaidREIClientPlugin implements REIClientPlugin {

    /**
     * Registers entries to collapse on the entry panel.
     *
     * @param registry the collapsible entry registry
     */
    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        List<Item> groupItems = new ArrayList<>();
        groupItems.add(InitItems.GARAGE_KIT.get());
        groupItems.add(InitItems.CHAIR.get());
        for (Item item : groupItems) {
            ResourceLocation groupId = ForgeRegistries.ITEMS.getKey(item);
            registry.group(groupId, item.getDescription(), VanillaEntryTypes.ITEM, (entryStack) -> entryStack.getValue().is(item));
        }
    }
}