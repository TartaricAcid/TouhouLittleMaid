package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.util.ItemDefinition;
import com.github.tartaricacid.touhoulittlemaid.api.util.OreDictDefinition;
import com.github.tartaricacid.touhoulittlemaid.api.util.ProcessingInput;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Maps;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * @author TartaricAcid
 * @date 2019/9/7 14:26
 **/
@SuppressWarnings("unchecked")
public class AltarRecipesManager {
    private final Map<ResourceLocation, AltarRecipe> ALTAR_RECIPES_MAP = Maps.newHashMap();

    public AltarRecipesManager() {

        ProcessingInput gold = OreDictDefinition.of("ingotGold");
        ProcessingInput iron = OreDictDefinition.of("ingotIron");
        ProcessingInput lapis = OreDictDefinition.of("gemLapis");
        ProcessingInput coal = ItemDefinition.of(Items.COAL);
        ProcessingInput gunpowder = OreDictDefinition.of("gunpowder");
        ProcessingInput blaze_powder = ItemDefinition.of(Items.BLAZE_POWDER);
        ProcessingInput paper = OreDictDefinition.of("paper");
        ProcessingInput stick = OreDictDefinition.of("stickWood");
        ProcessingInput obsidian = OreDictDefinition.of("obsidian");
        ProcessingInput redstone = OreDictDefinition.of("dustRedstone");
        ProcessingInput quartz_block = OreDictDefinition.of("blockQuartz");
        ProcessingInput dye6 = OreDictDefinition.of("dyeCyan");

        addRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "spawn_maid"),
                new AltarRecipe(
                        new ResourceLocation(TouhouLittleMaid.MOD_ID, "entity.passive.maid"),
                        0.5f, ItemStack.EMPTY,
                        OreDictDefinition.of("gemDiamond"), lapis, gold,
                        redstone, iron, coal));

        addRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "reborn_maid"),
                new ReviveMaidAltarRecipe(
                        0.3f,
                        ItemDefinition.of(MaidBlocks.GARAGE_KIT, OreDictionary.WILDCARD_VALUE), lapis, gold,
                        redstone, iron, coal));
        
        addRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "spawn_light_bolt"),
                new AltarRecipe(
                        new ResourceLocation("lightning_bolt"),
                        0.2f, ItemStack.EMPTY,
                        gunpowder, gunpowder, gunpowder,
                        blaze_powder, blaze_powder, blaze_powder));

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_album"),
                0.1f, new ItemStack(MaidItems.ALBUM),
                paper, paper, paper,
                paper, paper, ItemDefinition.of(Items.BOOK));

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_compass"),
                0.1f, new ItemStack(MaidItems.KAPPA_COMPASS),
                obsidian, obsidian, obsidian,
                OreDictDefinition.of("dyeBlue"), redstone, redstone);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_gohei"),
                0.15f, ((ItemHakureiGohei) MaidItems.HAKUREI_GOHEI).getDefaultItemStack(),
                stick, stick, stick,
                paper, paper);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_camera"),
                0.1f, new ItemStack(MaidItems.CAMERA),
                quartz_block, quartz_block, quartz_block,
                quartz_block, obsidian, obsidian);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_elixir"),
                0.3f, new ItemStack(MaidItems.ULTRAMARINE_ORB_ELIXIR),
                OreDictDefinition.of("blockEmerald"), OreDictDefinition.of("enderpearl"), dye6,
                dye6, dye6, dye6);
    }

    public static AltarRecipesManager instance() {
        return CommonProxy.ALTAR_RECIPES_MANAGER;
    }

    @Nullable
    public AltarRecipe getMatchRecipe(List<ItemStack> inputStacks) {
    	for (AltarRecipe recipe : ALTAR_RECIPES_MAP.values())
    	{
    		if (recipe.matches(inputStacks))
    		{
    			return recipe;
    		}
    	}
        return null;
    }

    public void addRecipe(ResourceLocation id, AltarRecipe altarRecipe) {
        ALTAR_RECIPES_MAP.put(id, altarRecipe);
    }

    private void addItemCraftRecipe(ResourceLocation id, float powerCost, ItemStack output, ProcessingInput... inputs) {
        addRecipe(id, new AltarRecipe(AltarRecipe.ENTITY_ITEM_ID, powerCost, output, inputs));
    }

    public Map<ResourceLocation, AltarRecipe> getAltarRecipesMap() {
        return ALTAR_RECIPES_MAP;
    }
}
