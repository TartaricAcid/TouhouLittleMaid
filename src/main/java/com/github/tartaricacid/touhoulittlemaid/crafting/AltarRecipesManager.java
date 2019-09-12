package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.init.MaidItems;
import com.github.tartaricacid.touhoulittlemaid.item.ItemHakureiGohei;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author TartaricAcid
 * @date 2019/9/7 14:26
 **/
@SuppressWarnings("unchecked")
public class AltarRecipesManager {
    private final Map<ResourceLocation, AltarRecipe> ALTAR_RECIPES_MAP = Maps.newHashMap();

    public AltarRecipesManager() {
        addEntitySpawnRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "spawn_maid"),
                new AltarRecipe<EntityMaid>(
                        new ResourceLocation(TouhouLittleMaid.MOD_ID, "entity.passive.maid"),
                        (entity, itemStackList) -> {
                            entity.onInitialSpawn(entity.getEntityWorld()
                                    .getDifficultyForLocation(entity.getPosition()), null);
                            return entity;
                        }, 0.5f,
                        of(Items.DIAMOND), of(Items.DYE, 4), of(Items.GOLD_INGOT),
                        of(Items.REDSTONE), of(Items.IRON_INGOT), of(Items.COAL)));

        addEntitySpawnRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "reborn_maid"),
                new AltarRecipe<EntityMaid>(
                        new ResourceLocation(TouhouLittleMaid.MOD_ID, "entity.passive.maid"),
                        (entity, itemStackList) -> {
                            ItemStack garageKit = itemStackList.stream().filter(stack ->
                                    stack.getItem() == Item.getItemFromBlock(MaidBlocks.GARAGE_KIT))
                                    .findFirst().orElseThrow(NullPointerException::new);
                            entity.readEntityFromNBT(BlockGarageKit.getEntityData(garageKit));
                            entity.setHealth(entity.getMaxHealth());
                            entity.setModelId(BlockGarageKit.getModelId(garageKit));
                            return entity;
                        }, 0.3f,
                        of(MaidBlocks.GARAGE_KIT), of(Items.DYE, 4), of(Items.GOLD_INGOT),
                        of(Items.REDSTONE), of(Items.IRON_INGOT), of(Items.COAL)));

        addEntitySpawnRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "spawn_light_bolt"),
                new AltarRecipe<EntityLightningBolt>(
                        new ResourceLocation("lightning_bolt"),
                        (entity, itemStackList) -> entity, 0.2f,
                        of(Items.GUNPOWDER), of(Items.GUNPOWDER), of(Items.GUNPOWDER),
                        of(Items.BLAZE_POWDER), of(Items.BLAZE_POWDER), of(Items.BLAZE_POWDER)));

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_album"),
                0.1f, of(MaidItems.ALBUM),
                of(Items.PAPER), of(Items.PAPER), of(Items.PAPER),
                of(Items.PAPER), of(Items.PAPER), of(Items.BOOK));

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_compass"),
                0.1f, of(MaidItems.KAPPA_COMPASS),
                of(Blocks.OBSIDIAN), of(Blocks.OBSIDIAN), of(Blocks.OBSIDIAN),
                of(Items.DYE, 4), of(Items.REDSTONE), of(Blocks.OBSIDIAN));

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_gohei"),
                0.15f, ((ItemHakureiGohei) MaidItems.HAKUREI_GOHEI).getDefaultItemStack(),
                of(Items.STICK), of(Items.STICK), of(Items.STICK),
                of(Items.PAPER), of(Items.PAPER));

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_camera"),
                0.1f, of(MaidItems.CAMERA),
                of(Blocks.QUARTZ_BLOCK), of(Blocks.QUARTZ_BLOCK), of(Blocks.QUARTZ_BLOCK),
                of(Blocks.QUARTZ_BLOCK), of(Blocks.OBSIDIAN), of(Blocks.OBSIDIAN));

        addItemCraftRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "craft_elixir"),
                0.3f, of(MaidItems.ULTRAMARINE_ORB_ELIXIR),
                of(Blocks.EMERALD_BLOCK), of(Items.ENDER_PEARL), of(Items.DYE, 6),
                of(Items.DYE, 6), of(Items.DYE, 6), of(Items.DYE, 6));
    }

    public static AltarRecipesManager instance() {
        return CommonProxy.ALTAR_RECIPES_MANAGER;
    }

    @Nullable
    public AltarRecipe getMatchRecipes(List<ItemStack> inputItemStack) {
        for (AltarRecipe altarRecipe : ALTAR_RECIPES_MAP.values()) {
            if (areRecipeEqual((LinkedList<ItemStack>) altarRecipe.getRecipe().clone(), inputItemStack)) {
                return altarRecipe;
            }
        }
        return null;
    }

    private boolean areRecipeEqual(LinkedList<ItemStack> recipe, List<ItemStack> inputItemStacks) {
        for (ItemStack input : inputItemStacks) {
            if (!removeOnceIf(recipe, itemStack -> areItemStackEqual(itemStack, input))) {
                return false;
            }
        }
        return true;
    }

    private boolean removeOnceIf(LinkedList<ItemStack> recipeIn, Predicate<ItemStack> filter) {
        final Iterator<ItemStack> each = recipeIn.iterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                return true;
            }
        }
        return false;
    }

    /**
     * 因为原版的判断相等的方法默认 ItemStack.EMPTY 为永远不相等，
     * 这很明显不符合我们的需求
     */
    private boolean areItemStackEqual(ItemStack stackFirst, ItemStack stackSecond) {
        // 一般 ItemStack.EMPTY 会触发这个
        if (stackFirst == stackSecond) {
            return true;
        }
        return stackFirst.isItemEqualIgnoreDurability(stackSecond);
    }

    private void addEntitySpawnRecipe(ResourceLocation id, AltarRecipe altarRecipe) {
        ALTAR_RECIPES_MAP.put(id, altarRecipe);
    }

    private void addItemCraftRecipe(ResourceLocation id, float powerCost, ItemStack outputItemStack, ItemStack... inputItemStack) {
        ItemStack[] recipe = new ItemStack[inputItemStack.length + 1];
        recipe[0] = outputItemStack;
        System.arraycopy(inputItemStack, 0, recipe, 1, inputItemStack.length);
        addEntitySpawnRecipe(id, new AltarRecipe<EntityItem>(
                new ResourceLocation("item"),
                (entity, itemStackList) -> {
                    entity.setItem(outputItemStack);
                    return entity;
                }, powerCost, recipe));
    }

    private ItemStack of(Item item) {
        return new ItemStack(item);
    }

    private ItemStack of(Block block) {
        return new ItemStack(Item.getItemFromBlock(block));
    }

    private ItemStack of(Item item, int meta) {
        return new ItemStack(item, 1, meta);
    }

    public Map<ResourceLocation, AltarRecipe> getAltarRecipesMap() {
        return ALTAR_RECIPES_MAP;
    }
}
