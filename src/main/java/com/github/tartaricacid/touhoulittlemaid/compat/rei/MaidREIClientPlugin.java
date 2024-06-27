package com.github.tartaricacid.touhoulittlemaid.compat.rei;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.CraftingTableBackpackContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.FurnaceBackpackContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.compat.rei.altar.ReiAltarRecipeCategory;
import com.github.tartaricacid.touhoulittlemaid.compat.rei.altar.ReiAltarRecipeDisplay;
import com.github.tartaricacid.touhoulittlemaid.compat.rei.altar.ReiAltarRecipeMaker;
import com.github.tartaricacid.touhoulittlemaid.compat.rei.transfer.BackpackTransferHandler;
import com.github.tartaricacid.touhoulittlemaid.init.InitItems;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.CraftingTableBackpackContainer;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.backpack.FurnaceBackpackContainer;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.entry.CollapsibleEntryRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.EntryStacks;
import me.shedaniel.rei.forge.REIPluginClient;
import me.shedaniel.rei.plugin.common.BuiltinPlugin;
import me.shedaniel.rei.plugin.common.displays.DefaultInformationDisplay;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

@REIPluginClient
public class MaidREIClientPlugin implements REIClientPlugin {

    public static final CategoryIdentifier<ReiAltarRecipeDisplay> ALTAR = CategoryIdentifier.of(TouhouLittleMaid.MOD_ID, "plugin/altar");

    /**
     * Registers entries to collapse on the entry panel.
     *
     * @param registry the collapsible entry registry
     */
    @Override
    @SuppressWarnings("UnstableApiUsage")
    public void registerCollapsibleEntries(CollapsibleEntryRegistry registry) {
        List<Item> groupItems = new ArrayList<>();
        groupItems.add(InitItems.GARAGE_KIT.get());
        groupItems.add(InitItems.CHAIR.get());
        for (Item item : groupItems) {
            ResourceLocation groupId = ForgeRegistries.ITEMS.getKey(item);
            registry.group(groupId, item.getDescription(), VanillaEntryTypes.ITEM, (entryStack) -> entryStack.getValue().is(item));
        }
    }

    /**
     * Registers new categories
     *
     * @param registry the category registry
     */
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new ReiAltarRecipeCategory());

        registry.addWorkstations(ALTAR, EntryStacks.of(InitItems.SANAE_GOHEI.get()), EntryStacks.of(InitItems.HAKUREI_GOHEI.get()));
    }

    /**
     * Registers new displays for categories
     *
     * @param registry the display registry
     */
    @Override
    public void registerDisplays(DisplayRegistry registry) {
        ReiAltarRecipeMaker.registerAltarRecipes(registry);
    }

    /**
     * Registers screen deciders
     *
     * @param registry the screen registry
     */
    @Override
    public void registerScreens(ScreenRegistry registry) {
        registry.registerContainerClickArea(new Rectangle(213, 121, 13, 12), CraftingTableBackpackContainerScreen.class, BuiltinPlugin.CRAFTING);
        registry.registerContainerClickArea(new Rectangle(183, 118, 28, 24), FurnaceBackpackContainerScreen.class, BuiltinPlugin.SMELTING, BuiltinPlugin.FUEL);
    }

    /**
     * Registers new transfer handlers
     *
     * @param registry the registry
     */
    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(new BackpackTransferHandler(CraftingTableBackpackContainer.class, BuiltinPlugin.CRAFTING, 71, 9, 0, 70));
        registry.register(new BackpackTransferHandler(FurnaceBackpackContainer.class, BuiltinPlugin.SMELTING, 70, 1, 0, 70));
        registry.register(new BackpackTransferHandler(FurnaceBackpackContainer.class, BuiltinPlugin.FUEL, 71, 1, 0, 70));
    }
}