package com.github.tartaricacid.touhoulittlemaid.crafting;

import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.block.BlockGarageKit;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.init.MaidBlocks;
import com.github.tartaricacid.touhoulittlemaid.proxy.CommonProxy;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.entity.effect.EntityLightningBolt;
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
    private static final Map<ResourceLocation, AltarRecipe> ALTAR_RECIPES_MAP = Maps.newHashMap();

    public AltarRecipesManager() {
        addRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "SpawnMaid"),
                new AltarRecipe<EntityMaid>(
                        new ResourceLocation(TouhouLittleMaid.MOD_ID, "entity.passive.maid"),
                        (entity, itemStackList) -> {
                            entity.onInitialSpawn(entity.getEntityWorld()
                                    .getDifficultyForLocation(entity.getPosition()), null);
                            return entity;
                        }, 0.2f,
                        of(Items.DIAMOND), of(Items.DYE, 4), of(Items.GOLD_INGOT),
                        of(Items.REDSTONE), of(Items.IRON_INGOT), of(Items.COAL)));

        addRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "RebornMaid"),
                new AltarRecipe<EntityMaid>(
                        new ResourceLocation(TouhouLittleMaid.MOD_ID, "entity.passive.maid"),
                        (entity, itemStackList) -> {
                            ItemStack garageKit = itemStackList.stream().filter(stack ->
                                    stack.getItem() == Item.getItemFromBlock(MaidBlocks.GARAGE_KIT))
                                    .findFirst().orElseThrow(NullPointerException::new);
                            entity.readEntityFromNBT(BlockGarageKit.getEntityData(garageKit));
                            entity.setHealth(entity.getMaxHealth());
                            return entity;
                        }, 0.2f,
                        of(MaidBlocks.GARAGE_KIT), of(Items.DYE, 4), of(Items.GOLD_INGOT),
                        of(Items.REDSTONE), of(Items.IRON_INGOT), of(Items.COAL)));

        addRecipe(new ResourceLocation(TouhouLittleMaid.MOD_ID, "SpawnLightBolt"),
                new AltarRecipe<EntityLightningBolt>(
                        new ResourceLocation("lightning_bolt"),
                        (entity, itemStackList) -> entity, 0.2f,
                        of(Items.GUNPOWDER), of(Items.GUNPOWDER), of(Items.GUNPOWDER),
                        of(Items.BLAZE_POWDER), of(Items.BLAZE_POWDER), of(Items.BLAZE_POWDER)));
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

    private void addRecipe(ResourceLocation id, AltarRecipe altarRecipe) {
        ALTAR_RECIPES_MAP.put(id, altarRecipe);
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
}
