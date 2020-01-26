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
import net.minecraft.init.Blocks;
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
        ProcessingInput blockGold = OreDictDefinition.of("blockGold");
        ProcessingInput blockIron = OreDictDefinition.of("blockIron");
        ProcessingInput blockLapis = OreDictDefinition.of("blockLapis");
        ProcessingInput blockCoal = OreDictDefinition.of("blockCoal");
        ProcessingInput gunpowder = OreDictDefinition.of("gunpowder");
        ProcessingInput blazePowder = ItemDefinition.of(Items.BLAZE_POWDER);
        ProcessingInput paper = OreDictDefinition.of("paper");
        ProcessingInput stick = OreDictDefinition.of("stickWood");
        ProcessingInput obsidian = OreDictDefinition.of("obsidian");
        ProcessingInput redstone = OreDictDefinition.of("dustRedstone");
        ProcessingInput blockRedstone = OreDictDefinition.of("blockRedstone");
        ProcessingInput quartzBlock = OreDictDefinition.of("blockQuartz");
        ProcessingInput dyeCyan = OreDictDefinition.of("dyeCyan");
        ProcessingInput dyeOrange = OreDictDefinition.of("dyeOrange");
        ProcessingInput dyeRed = OreDictDefinition.of("dyeRed");
        ProcessingInput dyeBlue = OreDictDefinition.of("dyeBlue");
        ProcessingInput dyeYellow = OreDictDefinition.of("dyeYellow");
        ProcessingInput dyeLime = OreDictDefinition.of("dyeLime");
        ProcessingInput shield = ItemDefinition.of(Items.SHIELD);
        ProcessingInput sugar = ItemDefinition.of(Items.SUGAR);
        ProcessingInput feather = ItemDefinition.of(Items.FEATHER);
        ProcessingInput cobblestone = OreDictDefinition.of("cobblestone");
        ProcessingInput puffer = ItemDefinition.of(Items.FISH, 3);
        ProcessingInput plankWood = OreDictDefinition.of("plankWood");
        ProcessingInput ingotGold = OreDictDefinition.of("ingotGold");
        ProcessingInput ingotIron = OreDictDefinition.of("ingotIron");
        ProcessingInput blockGlass = OreDictDefinition.of("blockGlass");
        ProcessingInput blockRedWool = ItemDefinition.of(Blocks.WOOL, 14);
        ProcessingInput blockPinkWool = ItemDefinition.of(Blocks.WOOL, 6);
        ProcessingInput blockGrayWool = ItemDefinition.of(Blocks.WOOL, 8);
        ProcessingInput blockYellowWool = ItemDefinition.of(Blocks.WOOL, 4);

        addRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "spawn_maid"),
                new SpawnMaidRecipe(
                        0.5f,
                        OreDictDefinition.of("gemDiamond"), blockLapis, blockGold,
                        blockRedstone, blockIron, blockCoal));

        addRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "reborn_maid"),
                new ReviveMaidAltarRecipe(
                        0.3f,
                        ItemDefinition.of(MaidBlocks.GARAGE_KIT, OreDictionary.WILDCARD_VALUE), blockLapis, blockGold,
                        blockRedstone, blockIron, blockCoal));

        addRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "spawn_light_bolt"),
                new AltarRecipe(
                        new ResourceLocation("lightning_bolt"),
                        0.2f, ItemStack.EMPTY,
                        gunpowder, gunpowder, gunpowder,
                        blazePowder, blazePowder, blazePowder));

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
                quartzBlock, quartzBlock, quartzBlock,
                quartzBlock, obsidian, obsidian);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_elixir"),
                0.3f, new ItemStack(MaidItems.ULTRAMARINE_ORB_ELIXIR),
                OreDictDefinition.of("blockEmerald"), OreDictDefinition.of("enderpearl"), dyeCyan,
                dyeCyan, dyeCyan, dyeCyan);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_explosion_protect_bauble"),
                0.2f, new ItemStack(MaidItems.EXPLOSION_PROTECT_BAUBLE),
                ItemDefinition.of(Blocks.NETHER_WART_BLOCK), dyeOrange, obsidian,
                obsidian, obsidian, obsidian);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_fire_protect_bauble"),
                0.2f, new ItemStack(MaidItems.FIRE_PROTECT_BAUBLE),
                ItemDefinition.of(Blocks.NETHER_WART_BLOCK), dyeRed, blazePowder,
                blazePowder, blazePowder, blazePowder);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_projectile_protect_bauble"),
                0.2f, new ItemStack(MaidItems.PROJECTILE_PROTECT_BAUBLE),
                ItemDefinition.of(Blocks.NETHER_WART_BLOCK), dyeBlue, shield,
                shield, shield, shield);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_magic_protect_bauble"),
                0.2f, new ItemStack(MaidItems.MAGIC_PROTECT_BAUBLE),
                ItemDefinition.of(Blocks.NETHER_WART_BLOCK), dyeCyan, sugar,
                sugar, sugar, sugar);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_fall_protect_bauble"),
                0.2f, new ItemStack(MaidItems.FALL_PROTECT_BAUBLE),
                ItemDefinition.of(Blocks.NETHER_WART_BLOCK), dyeYellow, feather,
                feather, feather, feather);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_drown_protect_bauble"),
                0.2f, new ItemStack(MaidItems.DROWN_PROTECT_BAUBLE),
                ItemDefinition.of(Blocks.NETHER_WART_BLOCK), dyeLime, puffer,
                puffer, puffer, puffer);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_tombstone_bauble"),
                0.3f, new ItemStack(MaidItems.TOMBSTONE_BAUBLE),
                ItemDefinition.of(Blocks.CHEST), ItemDefinition.of(Blocks.RED_FLOWER), OreDictDefinition.of("enderpearl"),
                cobblestone, cobblestone, cobblestone);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_maid_beacon"),
                0.3f, new ItemStack(MaidItems.MAID_BEACON),
                plankWood, dyeRed, plankWood,
                obsidian, OreDictDefinition.of("gemDiamond"), obsidian);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_gashapon_machines"),
                0.3f, new ItemStack(MaidBlocks.GASHAPON_MACHINES),
                blockGlass, blockGlass, ingotGold,
                ingotGold, ingotGold, ingotGold);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_maid_backpack_small"),
                0.1f, new ItemStack(MaidItems.MAID_BACKPACK, 1, 1),
                blockRedWool, blockRedWool, blockRedWool,
                blockRedWool, ingotIron, blockRedWool);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_maid_backpack_middle"),
                0.2f, new ItemStack(MaidItems.MAID_BACKPACK, 1, 2),
                blockPinkWool, blockPinkWool, blockPinkWool,
                blockPinkWool, ingotGold, blockPinkWool);

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_maid_backpack_big"),
                0.3f, new ItemStack(MaidItems.MAID_BACKPACK, 1, 3),
                blockGrayWool, blockGrayWool, blockGrayWool,
                ItemDefinition.of(Blocks.WOOL, 5), OreDictDefinition.of("gemDiamond"), ItemDefinition.of(Blocks.WOOL, 4));

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_suitcase"),
                0.2f, new ItemStack(MaidItems.SUITCASE),
                blockYellowWool, blockYellowWool, blockYellowWool,
                blockYellowWool, ingotGold, blockYellowWool);
    }

    public static AltarRecipesManager instance() {
        return CommonProxy.ALTAR_RECIPES_MANAGER;
    }

    @Nullable
    public AltarRecipe getMatchRecipe(List<ItemStack> inputStacks) {
        for (AltarRecipe recipe : ALTAR_RECIPES_MAP.values()) {
            if (recipe.matches(inputStacks)) {
                return recipe;
            }
        }
        return null;
    }

    public void addRecipe(ResourceLocation id, AltarRecipe altarRecipe) {
        ALTAR_RECIPES_MAP.put(id, altarRecipe);
    }

    public void addItemCraftRecipe(ResourceLocation id, float powerCost, ItemStack output, ProcessingInput... inputs) {
        addRecipe(id, new AltarRecipe(AltarRecipe.ENTITY_ITEM_ID, powerCost, output, inputs));
    }

    public void removeRecipe(ResourceLocation id) {
        ALTAR_RECIPES_MAP.remove(id);
    }

    public Map<ResourceLocation, AltarRecipe> getAltarRecipesMap() {
        return ALTAR_RECIPES_MAP;
    }
}
