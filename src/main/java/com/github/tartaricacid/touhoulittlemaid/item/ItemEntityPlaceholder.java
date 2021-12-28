package com.github.tartaricacid.touhoulittlemaid.item;

import com.github.tartaricacid.touhoulittlemaid.crafting.AltarRecipe;
import com.github.tartaricacid.touhoulittlemaid.init.InitRecipes;
import com.github.tartaricacid.touhoulittlemaid.inventory.AltarRecipeInventory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

import static com.github.tartaricacid.touhoulittlemaid.item.MaidGroup.MAIN_TAB;

public class ItemEntityPlaceholder extends Item {
    private static final String RECIPES_ID_TAG = "RecipeId";

    public ItemEntityPlaceholder() {
        super(new Item.Properties().stacksTo(1).tab(MAIN_TAB));
    }

    @SuppressWarnings("all")
    public static ItemStack setRecipeId(ItemStack stack, ResourceLocation id) {
        stack.getOrCreateTag().putString(RECIPES_ID_TAG, id.toString());
        return stack;
    }

    @SuppressWarnings("all")
    @Nullable
    public static ResourceLocation getRecipeId(ItemStack stack) {
        if (stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            if (tag.contains(RECIPES_ID_TAG, Constants.NBT.TAG_STRING)) {
                return new ResourceLocation(tag.getString(RECIPES_ID_TAG));
            }
        }
        return null;
    }

    @Override
    public ActionResultType useOn(ItemUseContext context) {
        if (context.getClickedFace() == Direction.UP) {
            ResourceLocation id = getRecipeId(context.getItemInHand());
            World world = context.getLevel();
            if (id != null && world instanceof ServerWorld) {
                IRecipe<AltarRecipeInventory> recipe = context.getLevel().getRecipeManager().byType(InitRecipes.ALTAR_CRAFTING).get(id);
                if (recipe instanceof AltarRecipe) {
                    AltarRecipe altarRecipe = (AltarRecipe) recipe;
                    altarRecipe.spawnOutputEntity((ServerWorld) world, context.getClickedPos().above(), null);
                    context.getItemInHand().shrink(1);
                }
            }
        }
        return super.useOn(context);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            ClientWorld world = Minecraft.getInstance().level;
            if (world == null) {
                return;
            }
            world.getRecipeManager().getAllRecipesFor(InitRecipes.ALTAR_CRAFTING).forEach(recipe -> {
                if (!recipe.isItemCraft()) {
                    items.add(setRecipeId(getDefaultInstance(), recipe.getId()));
                }
            });
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ITextComponent getName(ItemStack stack) {
        ResourceLocation recipeId = getRecipeId(stack);
        if (recipeId != null) {
            Path path = Paths.get(recipeId.getPath().toLowerCase(Locale.US));
            String namespace = recipeId.getNamespace().toLowerCase(Locale.US);
            String langKey = String.format("jei.%s.altar_craft.%s.result", namespace, path.getFileName());
            return new TranslationTextComponent(langKey);
        }
        return new TranslationTextComponent("item.touhou_little_maid.entity_placeholder");
    }
}
